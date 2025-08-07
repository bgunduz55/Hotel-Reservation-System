import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { RootState, AppDispatch } from '../../store';
import { fetchHotels } from '../../store/slices/hotelSlice';
import { fetchReservations } from '../../store/slices/reservationSlice';
import './Dashboard.css';

const Dashboard: React.FC = () => {
  const dispatch = useDispatch<AppDispatch>();
  const { user } = useSelector((state: RootState) => state.auth);
  const { hotels, loading: hotelsLoading } = useSelector((state: RootState) => state.hotel);
  const { reservations, loading: reservationsLoading } = useSelector((state: RootState) => state.reservation);

  useEffect(() => {
    dispatch(fetchHotels());
    dispatch(fetchReservations());
  }, [dispatch]);

  const getReservationStats = () => {
    const total = reservations.length;
    const pending = reservations.filter(r => r.status === 'PENDING').length;
    const confirmed = reservations.filter(r => r.status === 'CONFIRMED').length;
    const cancelled = reservations.filter(r => r.status === 'CANCELLED').length;

    return { total, pending, confirmed, cancelled };
  };

  const stats = getReservationStats();

  return (
    <div className="dashboard">
      <h1 className="dashboard-title">Welcome back, {user?.username}!</h1>
      
      <div className="dashboard-stats">
        <div className="stat-card">
          <div className="stat-icon">ðŸ¨</div>
          <div className="stat-content">
            <h3 className="stat-number">{hotels.length}</h3>
            <p className="stat-label">Total Hotels</p>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon">ðŸ“…</div>
          <div className="stat-content">
            <h3 className="stat-number">{stats.total}</h3>
            <p className="stat-label">Total Reservations</p>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon">â³</div>
          <div className="stat-content">
            <h3 className="stat-number">{stats.pending}</h3>
            <p className="stat-label">Pending Reservations</p>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon">âœ…</div>
          <div className="stat-content">
            <h3 className="stat-number">{stats.confirmed}</h3>
            <p className="stat-label">Confirmed Reservations</p>
          </div>
        </div>
      </div>

      <div className="dashboard-sections">
        <div className="section">
          <h2 className="section-title">Recent Hotels</h2>
          {hotelsLoading ? (
            <div className="loading">
              <div className="spinner"></div>
            </div>
          ) : (
            <div className="hotels-grid">
              {hotels.slice(0, 4).map((hotel) => (
                <div key={hotel.id} className="hotel-card">
                  <h3 className="hotel-name">{hotel.name}</h3>
                  <p className="hotel-location">{hotel.city}, {hotel.country}</p>
                  <div className="hotel-rating">
                    {'â­'.repeat(hotel.rating)}
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="section">
          <h2 className="section-title">Recent Reservations</h2>
          {reservationsLoading ? (
            <div className="loading">
              <div className="spinner"></div>
            </div>
          ) : (
            <div className="reservations-list">
              {reservations.slice(0, 5).map((reservation) => (
                <div key={reservation.id} className="reservation-item">
                  <div className="reservation-info">
                    <h4 className="reservation-hotel">
                      {reservation.hotel?.name || 'Hotel'}
                    </h4>
                    <p className="reservation-dates">
                      {new Date(reservation.checkInDate).toLocaleDateString()} - {new Date(reservation.checkOutDate).toLocaleDateString()}
                    </p>
                  </div>
                  <div className="reservation-status">
                    <span className={`status-badge status-${reservation.status.toLowerCase()}`}>
                      {reservation.status}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Dashboard; 
