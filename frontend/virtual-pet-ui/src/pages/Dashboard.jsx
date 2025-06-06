import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Plus } from 'lucide-react';

const Dashboard = ({ userId }) => {
  const [pets] = useState([]);
  const [user] = useState({ 
    username: 'Demo User', 
    playerLevel: 1, 
    totalExperience: 0, 
    totalCoins: 500 
  });

  return (
    <div className="dashboard">
      <div className="dashboard-header">
        <h2>Your Virtual Pets</h2>
        <Link to="/create-pet" className="create-pet-btn">
          <Plus size={20} />
          Add New Pet
        </Link>
      </div>

      {/* User Stats */}
      <div className="user-stats">
        <div className="stat-card">
          <h3>Level {user.playerLevel}</h3>
          <p>{user.totalExperience} XP</p>
        </div>
        <div className="stat-card">
          <h3>{user.totalCoins} 🪙</h3>
          <p>Coins</p>
        </div>
        <div className="stat-card">
          <h3>{pets.length}</h3>
          <p>Total Pets</p>
        </div>
        <div className="stat-card">
          <h3>100%</h3>
          <p>Avg Wellbeing</p>
        </div>
      </div>

      {/* All Pets */}
      <div className="pets-section">
        <h3>All Your Pets ({pets.length})</h3>
        {pets.length === 0 ? (
          <div className="no-pets">
            <p>You don't have any pets yet!</p>
            <Link to="/create-pet" className="create-first-pet-btn">
              Create Your First Pet
            </Link>
          </div>
        ) : (
          <div className="pets-grid">
            {/* Pets will be displayed here */}
          </div>
        )}
      </div>
    </div>
  );
};

export default Dashboard;
