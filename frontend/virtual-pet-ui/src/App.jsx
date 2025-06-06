import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import './App.css';

// Components
import Header from './components/Header';
import Dashboard from './pages/Dashboard';
import PetDetail from './pages/PetDetail';
import CreatePet from './pages/CreatePet';
import UserProfile from './pages/UserProfile';
import Login from './pages/Login';
import TestConnection from './pages/TestConnection'; // Add test component

function App() {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // Check if user is logged in
        const userId = localStorage.getItem('userId');
        const username = localStorage.getItem('username');

        if (userId && username) {
            setUser({ id: userId, username });
        }
        setLoading(false);
    }, []);

    const handleLogin = (userData) => {
        setUser(userData);
        localStorage.setItem('userId', userData.id);
        localStorage.setItem('username', userData.username);
    };

    const handleLogout = () => {
        setUser(null);
        localStorage.removeItem('userId');
        localStorage.removeItem('username');
    };

    if (loading) {
        return (
            <div className="app-loading">
                <div className="loading-spinner">Loading Virtual Pet...</div>
            </div>
        );
    }

    return (
        <Router>
            <div className="App">
                {user && <Header user={user} onLogout={handleLogout} />}

                <main className="main-content">
                    <Routes>
                        {!user ? (
                            <>
                                <Route path="/test" element={<TestConnection />} />
                                <Route path="*" element={<Login onLogin={handleLogin} />} />
                            </>
                        ) : (
                            <>
                                <Route path="/" element={<Dashboard userId={user.id} />} />
                                <Route path="/pet/:petId" element={<PetDetail userId={user.id} />} />
                                <Route path="/create-pet" element={<CreatePet userId={user.id} />} />
                                <Route path="/profile" element={<UserProfile userId={user.id} />} />
                                <Route path="/test" element={<TestConnection />} />
                                <Route path="*" element={<Navigate to="/" />} />
                            </>
                        )}
                    </Routes>
                </main>
            </div>
        </Router>
    );
}

export default App;