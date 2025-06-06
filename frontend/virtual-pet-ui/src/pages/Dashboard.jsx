import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Plus } from 'lucide-react';
import { gameApi, petApi, userApi } from '../services/api';

const Dashboard = ({ userId }) => {
  const [pets, setPets] = useState([]);
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const loadDashboardData = async () => {
      try {
        setLoading(true);

        // Load user data
        const userResponse = await userApi.getUser(userId);
        setUser(userResponse.data.data);

        // Load pets
        const petsResponse = await petApi.getAllPets(userId);
        setPets(petsResponse.data.data || []);

      } catch (err) {
        console.error('Failed to load dashboard:', err);
        setError('Failed to load dashboard data');
      } finally {
        setLoading(false);
      }
    };

    if (userId) {
      loadDashboardData();
    }
  }, [userId]);

  if (loading) return <div className="loading">Loading dashboard...</div>;
  if (error) return <div className="error">{error}</div>;
  if (!user) return <div className="error">User not found</div>;

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
            <h3>{pets.length > 0 ? Math.round(pets.reduce((sum, pet) => sum + pet.overallWellbeing, 0) / pets.length) : 0}%</h3>
            <p>Avg Wellbeing</p>
          </div>
        </div>

        {/* Pets Section */}
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
                {pets.map(pet => (
                    <div key={pet.id} className="pet-card">
                      <h4>{pet.name}</h4>
                      <p>Type: {pet.type}</p>
                      <p>Level: {pet.level}</p>
                      <p>Health: {pet.health}%</p>
                      <p>Happiness: {pet.happiness}%</p>
                      <Link to={`/pet/${pet.id}`} className="view-pet-btn">
                        View Details
                      </Link>
                    </div>
                ))}
              </div>
          )}
        </div>
      </div>
  );
};

export default Dashboard;