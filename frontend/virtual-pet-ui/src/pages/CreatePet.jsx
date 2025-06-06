import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const CreatePet = ({ userId }) => {
  const [petName, setPetName] = useState('');
  const [petType, setPetType] = useState('DOG');
  const navigate = useNavigate();

  const petTypes = [
    { type: 'DOG', emoji: '🐕', name: 'Dog' },
    { type: 'CAT', emoji: '🐱', name: 'Cat' },
    { type: 'DRAGON', emoji: '🐉', name: 'Dragon' },
    { type: 'ROBOT', emoji: '🤖', name: 'Robot' },
    { type: 'FAIRY', emoji: '🧚‍♀️', name: 'Fairy' }
  ];

  const handleSubmit = (e) => {
    e.preventDefault();
    if (petName.trim()) {
      // For demo, just go back to dashboard
      alert(`Created pet: ${petName} (${petType})`);
      navigate('/');
    }
  };

  return (
    <div className="create-pet">
      <h2>Create Your New Pet</h2>
      
      <form onSubmit={handleSubmit} className="create-pet-form">
        <div className="form-group">
          <label htmlFor="petName">Pet Name</label>
          <input
            type="text"
            id="petName"
            value={petName}
            onChange={(e) => setPetName(e.target.value)}
            required
            placeholder="Enter your pet's name"
          />
        </div>

        <div className="form-group">
          <label>Pet Type</label>
          <div className="pet-type-grid">
            {petTypes.map((type) => (
              <div
                key={type.type}
                className={`pet-type-option ${petType === type.type ? 'selected' : ''}`}
                onClick={() => setPetType(type.type)}
              >
                <span className="pet-type-emoji">{type.emoji}</span>
                <span className="pet-type-name">{type.name}</span>
              </div>
            ))}
          </div>
        </div>

        <button type="submit" className="submit-btn">
          Create Pet
        </button>
      </form>
    </div>
  );
};

export default CreatePet;
