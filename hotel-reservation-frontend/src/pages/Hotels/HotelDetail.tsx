import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { RootState, AppDispatch } from '../../store';
import { fetchHotelById, clearError, deleteHotel } from '../../store/slices/hotelSlice';
import './Hotels.css';

const HotelDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const dispatch = useDispatch<AppDispatch>();
  const navigate = useNavigate();
  const { selectedHotel, loading, error } = useSelector((state: RootState) => state.hotel);

  useEffect(() => {
    if (id) {
      dispatch(fetchHotelById(parseInt(id)));
    }
  }, [dispatch, id]);

  useEffect(() => {
    return () => {
      dispatch(clearError());
    };
  }, [dispatch]);

  const handleDeleteHotel = async () => {
    if (!id) return;
    
    if (window.confirm('Are you sure you want to delete this hotel?')) {
      try {
        await dispatch(deleteHotel(parseInt(id))).unwrap();
        navigate('/hotels');
      } catch (error) {
        console.error('Failed to delete hotel:', error);
      }
    }
  };

  if (loading) {
    return (
      <div className="loading">
        <div className="spinner"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="error-container">
        <div className="alert alert-error">
          {error}
        </div>
        <Link to="/hotels" className="btn btn-secondary">
          Back to Hotels
        </Link>
      </div>
    );
  }

  if (!selectedHotel) {
    return (
      <div className="error-container">
        <p>Hotel not found.</p>
        <Link to="/hotels" className="btn btn-secondary">
          Back to Hotels
        </Link>
      </div>
    );
  }

  return (
    <div className="hotel-detail">
      <div className="hotel-detail-header">
        <Link to="/hotels" className="btn btn-secondary">
          â† Back to Hotels
        </Link>
        <h1 className="page-title">{selectedHotel.name}</h1>
        <div className="hotel-actions">
          <Link to={`/hotels/${id}/edit`} className="btn btn-secondary">
            Edit Hotel
          </Link>
          <button onClick={handleDeleteHotel} className="btn btn-danger">
            Delete Hotel
          </button>
        </div>
      </div>

      <div className="hotel-detail-content">
        <div className="hotel-info">
          <div className="hotel-image-large">
            <div className="hotel-placeholder-large">ðŸ¨</div>
          </div>
          
          <div className="hotel-details">
            <div className="hotel-location-info">
              <h3>Location</h3>
              <p>{selectedHotel.address}</p>
              <p>{selectedHotel.city}, {selectedHotel.country}</p>
            </div>

            <div className="hotel-rating-info">
              <h3>Rating</h3>
              <div className="hotel-rating-large">
                {'â­'.repeat(selectedHotel.rating)}
              </div>
            </div>

            <div className="hotel-description-info">
              <h3>Description</h3>
              <p>{selectedHotel.description}</p>
            </div>
          </div>
        </div>

        <div className="hotel-rooms">
          <h2>Available Rooms</h2>
          <div className="rooms-grid">
            {selectedHotel.rooms.map((room) => (
              <div key={room.id} className="room-card">
                <div className="room-header">
                  <h3 className="room-number">{room.roomNumber}</h3>
                  <span className={`room-status ${room.isAvailable ? 'available' : 'occupied'}`}>
                    {room.isAvailable ? 'Available' : 'Occupied'}
                  </span>
                </div>
                <div className="room-details">
                  <p className="room-type">Type: {room.type}</p>
                  <p className="room-capacity">Capacity: {room.capacity} persons</p>
                  <p className="room-price">Price: ${room.price}/night</p>
                </div>
                {room.isAvailable && (
                  <Link 
                    to={`/reservations/create?hotelId=${selectedHotel.id}&roomId=${room.id}`}
                    className="btn btn-primary"
                  >
                    Book Now
                  </Link>
                )}
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default HotelDetail; 
