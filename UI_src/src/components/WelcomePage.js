import React from 'react';
import { useNavigate } from 'react-router-dom';
import './WelcomePage.css';

const WelcomePage = () => {
  const navigate = useNavigate();

  return (
    <div className="welcome-container">
      <img src="Icon.png" alt="Company Logo" className="company-logo" />
      <h1>Welcome to BureuNet!</h1>
      <div className="button-group">
        <button className="button" onClick={() => navigate('/login-page')}>Login</button>
        <button className="button" onClick={() => navigate('/signup-page')}>Sign Up</button>
      </div>
    </div>
  );
};

export default WelcomePage;
