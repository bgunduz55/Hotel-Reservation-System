import React from 'react';
import { useSelector } from 'react-redux';
import { RootState } from '../../store';
import './Profile.css';

const Profile: React.FC = () => {
  const { user } = useSelector((state: RootState) => state.auth);

  if (!user) {
    return (
      <div className="profile">
        <div className="loading">
          <div className="spinner"></div>
        </div>
      </div>
    );
  }

  return (
    <div className="profile">
      <div className="profile-header">
        <h1 className="page-title">Profile</h1>
      </div>

      <div className="profile-content">
        <div className="profile-card">
          <div className="profile-avatar">
            <div className="avatar-placeholder">
              {user.username.charAt(0).toUpperCase()}
            </div>
          </div>

          <div className="profile-info">
            <div className="info-section">
              <h3>Personal Information</h3>
              <div className="info-grid">
                <div className="info-item">
                  <label>Username:</label>
                  <span>{user.username}</span>
                </div>
                <div className="info-item">
                  <label>Roles:</label>
                  <span className="role-badge">{user.roles.join(', ')}</span>
                </div>
              </div>
            </div>

            <div className="info-section">
              <h3>Account Information</h3>
              <div className="info-grid">
                <div className="info-item">
                  <label>Account Status:</label>
                  <span className="status-active">Active</span>
                </div>
                <div className="info-item">
                  <label>Member Since:</label>
                  <span>Recently</span>
                </div>
              </div>
            </div>

            <div className="info-section">
              <h3>Preferences</h3>
              <div className="preferences">
                <div className="preference-item">
                  <label>
                    <input type="checkbox" defaultChecked />
                    Email Notifications
                  </label>
                </div>
                <div className="preference-item">
                  <label>
                    <input type="checkbox" defaultChecked />
                    SMS Notifications
                  </label>
                </div>
                <div className="preference-item">
                  <label>
                    <input type="checkbox" />
                    Marketing Emails
                  </label>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div className="profile-actions">
          <button className="btn btn-primary">
            Edit Profile
          </button>
          <button className="btn btn-secondary">
            Change Password
          </button>
          <button className="btn btn-danger">
            Delete Account
          </button>
        </div>
      </div>
    </div>
  );
};

export default Profile; 