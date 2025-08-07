import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useParams, Link } from 'react-router-dom';
import { RootState, AppDispatch } from '../../store';
import { fetchReservationById, clearError, cancelReservation } from '../../store/slices/reservationSlice';
import './Reservations.css';

const ReservationDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const dispatch = useDispatch<AppDispatch>();
  const { selectedReservation, loading, error } = useSelector((state: RootState) => state.reservation);

  useEffect(() => {
    if (id) {
      dispatch(fetchReservationById(parseInt(id)));
    }
  }, [dispatch, id]);

  useEffect(() => {
    return () => {
      dispatch(clearError());
    };
  }, [dispatch]);

  const handleCancelReservation = () => {
    if (id && selectedReservation?.status === 'PENDING') {
      dispatch(cancelReservation(parseInt(id)));
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
        <Link to="/reservations" className="btn btn-secondary">
          Back to Reservations
        </Link>
      </div>
    );
  }

  if (!selectedReservation) {
    return (
      <div className="error-container">
        <p>Reservation not found.</p>
        <Link to="/reservations" className="btn btn-secondary">
          Back to Reservations
        </Link>
      </div>
    );
  }

  const getStatusBadgeClass = (status: string) => {
    switch (status.toLowerCase()) {
      case 'pending':
        return 'status-pending';
      case 'confirmed':
        return 'status-confirmed';
      case 'cancelled':
        return 'status-cancelled';
      default:
        return 'status-pending';
    }
  };

  return (
    <div className="reservation-detail">
      <div className="reservation-detail-header">
        <Link to="/reservations" className="btn btn-secondary">
          â† Back to Reservations
        </Link>
        <h1 className="page-title">Reservation #{selectedReservation.id}</h1>
      </div>

      <div className="reservation-detail-content">
        <div className="reservation-info-card">
          <div className="reservation-status-section">
            <h3>Status</h3>
            <span className={`status-badge-large ${getStatusBadgeClass(selectedReservation.status)}`}>
              {selectedReservation.status}
            </span>
          </div>

          <div className="reservation-details-section">
            <h3>Reservation Details</h3>
            <div className="detail-grid">
              <div className="detail-item">
                <label>Reservation ID:</label>
                <span>#{selectedReservation.id}</span>
              </div>
              <div className="detail-item">
                <label>Hotel:</label>
                <span>{selectedReservation.hotel?.name || 'N/A'}</span>
              </div>
              <div className="detail-item">
                <label>Room:</label>
                <span>{selectedReservation.room?.roomNumber || 'N/A'}</span>
              </div>
              <div className="detail-item">
                <label>Room Type:</label>
                <span>{selectedReservation.room?.type || 'N/A'}</span>
              </div>
              <div className="detail-item">
                <label>Check-in Date:</label>
                <span>{new Date(selectedReservation.checkInDate).toLocaleDateString()}</span>
              </div>
              <div className="detail-item">
                <label>Check-out Date:</label>
                <span>{new Date(selectedReservation.checkOutDate).toLocaleDateString()}</span>
              </div>
              <div className="detail-item">
                <label>Total Price:</label>
                <span>${selectedReservation.totalPrice}</span>
              </div>
              <div className="detail-item">
                <label>Created:</label>
                <span>{new Date(selectedReservation.createdAt).toLocaleString()}</span>
              </div>
              <div className="detail-item">
                <label>Last Updated:</label>
                <span>{new Date(selectedReservation.updatedAt).toLocaleString()}</span>
              </div>
            </div>
          </div>

          {selectedReservation.status === 'PENDING' && (
            <div className="reservation-actions-section">
              <h3>Actions</h3>
              <button
                onClick={handleCancelReservation}
                className="btn btn-danger"
              >
                Cancel Reservation
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ReservationDetail; 
