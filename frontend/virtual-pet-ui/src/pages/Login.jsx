import React, { useState } from 'react';

const Login = ({ onLogin }) => {
  const [username, setUsername] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    if (username.trim()) {
      onLogin({ 
        id: Date.now().toString(), 
        username: username.trim() 
      });
    }
  };

  return (
    <div className="login-page">
      <div className="login-container">
        <div className="login-header">
          <h1>🐾 Virtual Pet</h1>
          <p>Welcome to your virtual pet world!</p>
        </div>

        <form onSubmit={handleSubmit} className="login-form">
          <h2>Enter Your Name</h2>
          
          <div className="form-group">
            <label htmlFor="username">Username</label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
              placeholder="Enter your username"
            />
          </div>

          <button type="submit" className="submit-btn">
            Start Playing
          </button>
        </form>

        <div className="demo-info">
          <p><strong>Demo Mode:</strong> Just enter any username to start!</p>
        </div>
      </div>
    </div>
  );
};

export default Login;
