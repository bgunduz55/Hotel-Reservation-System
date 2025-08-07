import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { RootState } from './store';
import Layout from './components/Layout/Layout';
import Login from './pages/Auth/Login';
import Register from './pages/Auth/Register';
import Dashboard from './pages/Dashboard/Dashboard';
import HotelList from './pages/Hotels/HotelList';
import HotelDetail from './pages/Hotels/HotelDetail';
import CreateHotel from './pages/Hotels/CreateHotel';
import EditHotel from './pages/Hotels/EditHotel';
import ReservationList from './pages/Reservations/ReservationList';
import ReservationDetail from './pages/Reservations/ReservationDetail';
import CreateReservation from './pages/Reservations/CreateReservation';
import Profile from './pages/Profile/Profile';
import './App.css';

const App: React.FC = () => {
  const { isAuthenticated } = useSelector((state: RootState) => state.auth);

  // Protected Route Component
  const ProtectedRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    return isAuthenticated ? <>{children}</> : <Navigate to="/login" replace />;
  };

  // Public Route Component (redirect if authenticated)
  const PublicRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    return !isAuthenticated ? <>{children}</> : <Navigate to="/dashboard" replace />;
  };

  return (
    <div className="App">
      <Routes>
        {/* Public Routes */}
        <Route path="/login" element={
          <PublicRoute>
            <Login />
          </PublicRoute>
        } />
        <Route path="/register" element={
          <PublicRoute>
            <Register />
          </PublicRoute>
        } />

        {/* Protected Routes */}
        <Route path="/" element={
          <ProtectedRoute>
            <Layout />
          </ProtectedRoute>
        }>
          <Route index element={<Navigate to="/dashboard" replace />} />
          <Route path="dashboard" element={<Dashboard />} />
          <Route path="hotels" element={<HotelList />} />
          <Route path="hotels/create" element={<CreateHotel />} />
          <Route path="hotels/:id" element={<HotelDetail />} />
          <Route path="hotels/:id/edit" element={<EditHotel />} />
          <Route path="reservations" element={<ReservationList />} />
          <Route path="reservations/create" element={<CreateReservation />} />
          <Route path="reservations/:id" element={<ReservationDetail />} />
          <Route path="profile" element={<Profile />} />
        </Route>

        {/* Catch all route */}
        <Route path="*" element={<Navigate to="/dashboard" replace />} />
      </Routes>
    </div>
  );
};

export default App; 