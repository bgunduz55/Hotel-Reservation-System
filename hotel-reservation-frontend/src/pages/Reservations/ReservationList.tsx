import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link } from 'react-router-dom';
import { RootState, AppDispatch } from '../../store';
import { fetchReservations, clearError } from '../../store/slices/reservationSlice';
import './Reservations.css';

const ReservationList: React.FC = () => {
  const dispatch = useDispatch<AppDispatch>();
  const { reservations, loading, error } = useSelector((state: RootState) => state.reservation);
  const [statusFilter, setStatusFilter] = useState<string>('all');

  useEffect(() => {
    dispatch(fetchReservations());
  }, [dispatch]);

  useEffect(() => {
    return () => {
      dispatch(clearError());
    };
  }, [dispatch]);

  const filteredReservations = reservations.filter(reservation => {
    if (statusFilter === 'all') return true;
    return reservation.status.toLowerCase() === statusFilter.toLowerCase();
  });

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

  if (loading) {
    return (
      <div className="loading">
        <div className="spinner"></div>
      </div>
    );
  }

  return (
    <div className="reservation-list">
      <div className="reservation-list-header">
        <h1 className="page-title">Reservations</h1>
        <div className="reservation-actions">
          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="status-filter"
          >
            <option value="all">All Status</option>
            <option value="pending">Pending</option>
            <option value="confirmed">Confirmed</option>
            <option value="cancelled">Cancelled</option>
          </select>
          <Link to="/reservations/create" className="btn btn-primary">
            New Reservation
          </Link>
        </div>
      </div>

      {error && (
        <div className="alert alert-error">
          {error}
        </div>
      )}

      <div className="reservations-grid">
        {filteredReservations.map((reservation) => (
          <div key={reservation.id} className="reservation-card">
            <div className="reservation-header">
              <h3 className="reservation-id">#{reservation.id}</h3>
              <span className={`status-badge ${getStatusBadgeClass(reservation.status)}`}>
                {reservation.status}
              </span>
            </div>
            
            <div className="reservation-content">
              <div className="reservation-info">
                <p className="reservation-hotel">
                  <strong>Hotel:</strong> {reservation.hotel?.name || 'N/A'}
                </p>
                <p className="reservation-room">
                  <strong>Room:</strong> {reservation.room?.roomNumber || 'N/A'}
                </p>
                <p className="reservation-dates">
                  <strong>Check-in:</strong> {new Date(reservation.checkInDate).toLocaleDateString()}
                </p>
                <p className="reservation-dates">
                  <strong>Check-out:</strong> {new Date(reservation.checkOutDate).toLocaleDateString()}
                </p>
                <p className="reservation-price">
                  <strong>Total Price:</strong> ${reservation.totalPrice}
                </p>
              </div>
              
              <div className="reservation-actions">
                <Link to={`/reservations/${reservation.id}`} className="btn btn-secondary">
                  View Details
                </Link>
              </div>
            </div>
          </div>
        ))}
      </div>

      {filteredReservations.length === 0 && !loading && (
        <div className="empty-state">
          <p>No reservations found.</p>
          <Link to="/reservations/create" className="btn btn-primary">
            Create Your First Reservation
          </Link>
        </div>
      )}
    </div>
  );
};

export default ReservationList; 
