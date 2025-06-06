import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { Home, User, Plus, LogOut } from 'lucide-react';

const Header = ({ user, onLogout }) => {
  const location = useLocation();

  return (
    <header className="header">
      <div className="header-content">
        <div className="header-left">
          <h1 className="app-title">🐾 Virtual Pet</h1>
        </div>
        
        <nav className="nav">
          <Link 
            to="/" 
            className={`nav-link ${location.pathname === '/' ? 'active' : ''}`}
          >
            <Home size={20} />
            Dashboard
          </Link>
          <Link 
            to="/create-pet" 
            className={`nav-link ${location.pathname === '/create-pet' ? 'active' : ''}`}
          >
            <Plus size={20} />
            New Pet
          </Link>
          <Link 
            to="/profile" 
            className={`nav-link ${location.pathname === '/profile' ? 'active' : ''}`}
          >
            <User size={20} />
            Profile
          </Link>
        </nav>
        
        <div className="header-right">
          <span className="username">Welcome, {user.username}!</span>
          <button onClick={onLogout} className="logout-btn">
            <LogOut size={18} />
            Logout
          </button>
        </div>
      </div>
    </header>
  );
};

export default Header;
