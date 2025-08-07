import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useParams, useNavigate } from 'react-router-dom';
import { RootState, AppDispatch } from '../../store';
import { fetchHotelById, updateHotel, clearError } from '../../store/slices/hotelSlice';
import './Hotels.css';

const EditHotel: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const dispatch = useDispatch<AppDispatch>();
  const navigate = useNavigate();
  const { selectedHotel, loading, error } = useSelector((state: RootState) => state.hotel);

  const [formData, setFormData] = useState({
    name: '',
    address: '',
    city: '',
    country: '',
    rating: 3,
    description: ''
  });

  useEffect(() => {
    if (id) {
      dispatch(fetchHotelById(parseInt(id)));
    }
  }, [dispatch, id]);

  useEffect(() => {
    if (selectedHotel) {
      setFormData({
        name: selectedHotel.name,
        address: selectedHotel.address,
        city: selectedHotel.city,
        country: selectedHotel.country,
        rating: selectedHotel.rating,
        description: selectedHotel.description
      });
    }
  }, [selectedHotel]);

  useEffect(() => {
    return () => {
      dispatch(clearError());
    };
  }, [dispatch]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: name === 'rating' ? parseInt(value) : value
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!id) return;

    try {
      await dispatch(updateHotel({ id: parseInt(id), hotelData: formData })).unwrap();
      navigate(`/hotels/${id}`);
    } catch (error) {
      console.error('Failed to update hotel:', error);
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
        <button onClick={() => navigate('/hotels')} className="btn btn-secondary">
          Back to Hotels
        </button>
      </div>
    );
  }

  if (!selectedHotel) {
    return (
      <div className="error-container">
        <p>Hotel not found.</p>
        <button onClick={() => navigate('/hotels')} className="btn btn-secondary">
          Back to Hotels
        </button>
      </div>
    );
  }

  return (
    <div className="edit-hotel">
      <div className="edit-hotel-header">
        <h1 className="page-title">Edit Hotel</h1>
      </div>

      <form onSubmit={handleSubmit} className="hotel-form">
        <div className="form-group">
          <label htmlFor="name">Hotel Name *</label>
          <input
            type="text"
            id="name"
            name="name"
            value={formData.name}
            onChange={handleInputChange}
            required
            className="form-input"
          />
        </div>

        <div className="form-group">
          <label htmlFor="address">Address *</label>
          <input
            type="text"
            id="address"
            name="address"
            value={formData.address}
            onChange={handleInputChange}
            required
            className="form-input"
          />
        </div>

        <div className="form-row">
          <div className="form-group">
            <label htmlFor="city">City *</label>
            <input
              type="text"
              id="city"
              name="city"
              value={formData.city}
              onChange={handleInputChange}
              required
              className="form-input"
            />
          </div>

          <div className="form-group">
            <label htmlFor="country">Country *</label>
            <input
              type="text"
              id="country"
              name="country"
              value={formData.country}
              onChange={handleInputChange}
              required
              className="form-input"
            />
          </div>
        </div>

        <div className="form-group">
          <label htmlFor="rating">Rating</label>
          <select
            id="rating"
            name="rating"
            value={formData.rating}
            onChange={handleInputChange}
            className="form-input"
          >
            <option value={1}>1 Star</option>
            <option value={2}>2 Stars</option>
            <option value={3}>3 Stars</option>
            <option value={4}>4 Stars</option>
            <option value={5}>5 Stars</option>
          </select>
        </div>

        <div className="form-group">
          <label htmlFor="description">Description</label>
          <textarea
            id="description"
            name="description"
            value={formData.description}
            onChange={handleInputChange}
            rows={4}
            className="form-input"
          />
        </div>

        <div className="form-actions">
          <button
            type="button"
            onClick={() => navigate(`/hotels/${id}`)}
            className="btn btn-secondary"
            disabled={loading}
          >
            Cancel
          </button>
          <button
            type="submit"
            className="btn btn-primary"
            disabled={loading}
          >
            {loading ? 'Updating...' : 'Update Hotel'}
          </button>
        </div>
      </form>
    </div>
  );
};

export default EditHotel;
