import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { RootState, AppDispatch } from '../../store';
import { createReservation, clearError } from '../../store/slices/reservationSlice';
import { fetchHotels } from '../../store/slices/hotelSlice';
import { hotelApi } from '../../services/hotelApi';
import './Reservations.css';

interface Room {
  id: number;
  roomNumber: string;
  type: string;
  capacity: number;
  price: number;
  isAvailable: boolean;
}

const CreateReservation: React.FC = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const dispatch = useDispatch<AppDispatch>();
  
  const { hotels } = useSelector((state: RootState) => state.hotel);
  const { loading, error } = useSelector((state: RootState) => state.reservation);
  
  const [formData, setFormData] = useState({
    hotelId: parseInt(searchParams.get('hotelId') || '0'),
    roomId: parseInt(searchParams.get('roomId') || '0'),
    checkInDate: '',
    checkOutDate: '',
  });
  
  const [availableRooms, setAvailableRooms] = useState<Room[]>([]);
  const [selectedHotel, setSelectedHotel] = useState<any>(null);
  const [selectedRoom, setSelectedRoom] = useState<Room | null>(null);
  const [totalPrice, setTotalPrice] = useState(0);

  useEffect(() => {
    dispatch(fetchHotels());
  }, [dispatch]);

  useEffect(() => {
    return () => {
      dispatch(clearError());
    };
  }, [dispatch]);

  useEffect(() => {
    if (formData.hotelId && formData.checkInDate && formData.checkOutDate) {
      fetchAvailableRooms();
    }
  }, [formData.hotelId, formData.checkInDate, formData.checkOutDate]);

  useEffect(() => {
    if (selectedRoom && formData.checkInDate && formData.checkOutDate) {
      calculateTotalPrice();
    }
  }, [selectedRoom, formData.checkInDate, formData.checkOutDate]);

  const fetchAvailableRooms = async () => {
    try {
      const rooms = await hotelApi.getAvailableRooms(
        formData.hotelId,
        formData.checkInDate,
        formData.checkOutDate
      );
      setAvailableRooms(rooms);
    } catch (error) {
      console.error('Error fetching available rooms:', error);
    }
  };

  const calculateTotalPrice = () => {
    if (!selectedRoom || !formData.checkInDate || !formData.checkOutDate) return;

    const checkIn = new Date(formData.checkInDate);
    const checkOut = new Date(formData.checkOutDate);
    const nights = Math.ceil((checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24));
    
    setTotalPrice(selectedRoom.price * nights);
  };

  const handleChange = (e: React.ChangeEvent<HTMLSelectElement | HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));

    if (name === 'hotelId') {
      setSelectedHotel(hotels.find(h => h.id === parseInt(value)));
      setSelectedRoom(null);
      setFormData(prev => ({ ...prev, roomId: 0 }));
    }
  };

  const handleRoomSelect = (room: Room) => {
    setSelectedRoom(room);
    setFormData(prev => ({ ...prev, roomId: room.id }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!formData.hotelId || !formData.roomId || !formData.checkInDate || !formData.checkOutDate) {
      alert('Please fill in all fields');
      return;
    }

    dispatch(createReservation(formData));
    navigate('/reservations');
  };

  const validateDates = () => {
    if (!formData.checkInDate || !formData.checkOutDate) return true;
    
    const checkIn = new Date(formData.checkInDate);
    const checkOut = new Date(formData.checkOutDate);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    
    return checkIn >= today && checkOut > checkIn;
  };

  return (
    <div className="create-reservation">
      <div className="create-reservation-header">
        <h1 className="page-title">Create New Reservation</h1>
      </div>

      {error && (
        <div className="alert alert-error">
          {error}
        </div>
      )}

      <form onSubmit={handleSubmit} className="reservation-form">
        <div className="form-section">
          <h3>Hotel Selection</h3>
          <div className="form-group">
            <label htmlFor="hotelId" className="form-label">Hotel</label>
            <select
              id="hotelId"
              name="hotelId"
              value={formData.hotelId}
              onChange={handleChange}
              className="form-input"
              required
            >
              <option value="">Select a hotel</option>
              {hotels.map(hotel => (
                <option key={hotel.id} value={hotel.id}>
                  {hotel.name} - {hotel.city}, {hotel.country}
                </option>
              ))}
            </select>
          </div>
        </div>

        <div className="form-section">
          <h3>Dates</h3>
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="checkInDate" className="form-label">Check-in Date</label>
              <input
                type="date"
                id="checkInDate"
                name="checkInDate"
                value={formData.checkInDate}
                onChange={handleChange}
                className="form-input"
                required
                min={new Date().toISOString().split('T')[0]}
              />
            </div>
            <div className="form-group">
              <label htmlFor="checkOutDate" className="form-label">Check-out Date</label>
              <input
                type="date"
                id="checkOutDate"
                name="checkOutDate"
                value={formData.checkOutDate}
                onChange={handleChange}
                className="form-input"
                required
                min={formData.checkInDate || new Date().toISOString().split('T')[0]}
              />
            </div>
          </div>
          {!validateDates() && (
            <div className="alert alert-error">
              Please select valid dates. Check-in must be today or later, and check-out must be after check-in.
            </div>
          )}
        </div>

        {formData.hotelId && formData.checkInDate && formData.checkOutDate && validateDates() && (
          <div className="form-section">
            <h3>Available Rooms</h3>
            {availableRooms.length > 0 ? (
              <div className="rooms-selection">
                {availableRooms.map(room => (
                  <div
                    key={room.id}
                    className={`room-option ${selectedRoom?.id === room.id ? 'selected' : ''}`}
                    onClick={() => handleRoomSelect(room)}
                  >
                    <div className="room-info">
                      <h4>{room.roomNumber}</h4>
                      <p>Type: {room.type}</p>
                      <p>Capacity: {room.capacity} persons</p>
                      <p>Price: ${room.price}/night</p>
                    </div>
                    <div className="room-selection-indicator">
                      {selectedRoom?.id === room.id && 'âœ“'}
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <p>No available rooms for the selected dates.</p>
            )}
          </div>
        )}

        {selectedRoom && totalPrice > 0 && (
          <div className="form-section">
            <h3>Reservation Summary</h3>
            <div className="reservation-summary">
              <div className="summary-item">
                <label>Hotel:</label>
                <span>{selectedHotel?.name}</span>
              </div>
              <div className="summary-item">
                <label>Room:</label>
                <span>{selectedRoom.roomNumber} - {selectedRoom.type}</span>
              </div>
              <div className="summary-item">
                <label>Check-in:</label>
                <span>{new Date(formData.checkInDate).toLocaleDateString()}</span>
              </div>
              <div className="summary-item">
                <label>Check-out:</label>
                <span>{new Date(formData.checkOutDate).toLocaleDateString()}</span>
              </div>
              <div className="summary-item">
                <label>Total Price:</label>
                <span className="total-price">${totalPrice}</span>
              </div>
            </div>
          </div>
        )}

        <div className="form-actions">
          <button
            type="button"
            onClick={() => navigate('/reservations')}
            className="btn btn-secondary"
          >
            Cancel
          </button>
          <button
            type="submit"
            className="btn btn-primary"
            disabled={loading || !selectedRoom || !validateDates()}
          >
            {loading ? 'Creating...' : 'Create Reservation'}
          </button>
        </div>
      </form>
    </div>
  );
};

export default CreateReservation; 
