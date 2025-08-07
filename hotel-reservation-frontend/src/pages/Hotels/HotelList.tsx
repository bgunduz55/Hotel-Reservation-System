import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link } from 'react-router-dom';
import { RootState, AppDispatch } from '../../store';
import { fetchHotels, clearError, deleteHotel } from '../../store/slices/hotelSlice';
import './Hotels.css';

const HotelList: React.FC = () => {
  const dispatch = useDispatch<AppDispatch>();
  const { hotels, loading, error } = useSelector((state: RootState) => state.hotel);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    dispatch(fetchHotels());
  }, [dispatch]);

  useEffect(() => {
    return () => {
      dispatch(clearError());
    };
  }, [dispatch]);

  const filteredHotels = hotels.filter(hotel =>
    hotel.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    hotel.city.toLowerCase().includes(searchTerm.toLowerCase()) ||
    hotel.country.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleDeleteHotel = async (hotelId: number) => {
    if (window.confirm('Are you sure you want to delete this hotel?')) {
      try {
        await dispatch(deleteHotel(hotelId)).unwrap();
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

  return (
    <div className="hotel-list">
      <div className="hotel-list-header">
        <h1 className="page-title">Hotels</h1>
        <div className="header-actions">
          <div className="search-container">
            <input
              type="text"
              placeholder="Search hotels..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="search-input"
            />
          </div>
          <Link to="/hotels/create" className="btn btn-primary">
            Add New Hotel
          </Link>
        </div>
      </div>

      {error && (
        <div className="alert alert-error">
          {error}
        </div>
      )}

      <div className="hotels-grid">
        {filteredHotels.map((hotel) => (
          <div key={hotel.id} className="hotel-card">
            <div className="hotel-image">
              <div className="hotel-placeholder">ðŸ¨</div>
            </div>
            <div className="hotel-content">
              <h3 className="hotel-name">{hotel.name}</h3>
              <p className="hotel-location">{hotel.city}, {hotel.country}</p>
              <p className="hotel-address">{hotel.address}</p>
              <div className="hotel-rating">
                {'â­'.repeat(hotel.rating)}
              </div>
              <p className="hotel-description">{hotel.description}</p>
              <div className="hotel-actions">
                <Link to={`/hotels/${hotel.id}`} className="btn btn-primary">
                  View Details
                </Link>
                <Link to={`/hotels/${hotel.id}/edit`} className="btn btn-secondary">
                  Edit
                </Link>
                <button
                  onClick={() => handleDeleteHotel(hotel.id)}
                  className="btn btn-danger"
                >
                  Delete
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>

      {filteredHotels.length === 0 && !loading && (
        <div className="empty-state">
          <p>No hotels found.</p>
        </div>
      )}
    </div>
  );
};

export default HotelList; 
