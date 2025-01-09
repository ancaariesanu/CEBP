import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import supabase from './SupabaseClient';
import './LoginPage.css';
import userClass from './UserClass'; // Import the singleton instance

const LoginPage = () => {
  const [formData, setFormData] = useState({
    username: '',
    password: ''
  });

  const [errors, setErrors] = useState({
    username: '',
    password: ''
  });

  const [loginError, setLoginError] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const validateForm = () => {
    let formErrors = { username: '', password: '' };
    let valid = true;

    if (!formData.username) {
      formErrors.username = 'Username is required';
      valid = false;
    }

    if (!formData.password) {
      formErrors.password = 'Password is required';
      valid = false;
    }

    setErrors(formErrors);
    return valid;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (validateForm()) {
      // Query Supabase to verify credentials
      const { data, error } = await supabase
        .from('customer')
        .select('*')
        .eq('user_name', formData.username)
        .eq('pass', formData.password)
        .single();

      if (error || !data) {
        setLoginError('Invalid username or password');
        console.error('Login error:', error);
      } else {
        console.log('Login successful:', data);
        setFormData({ username: '', password: '' });
        setLoginError('');
        
        // Set the user in the UserClass instance
        userClass.setUser(data);  // Store the logged-in user

        navigate('/office-dashboard');
      }
    }
  };

  return (
    <div className="login-container">
      <h2>Login</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Username</label>
          <input
            type="text"
            name="username"
            value={formData.username}
            onChange={handleChange}
            placeholder="Enter your username"
          />
          {errors.username && <span className="error">{errors.username}</span>}
        </div>
        <div className="form-group">
          <label>Password</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            placeholder="Enter your password"
          />
          {errors.password && <span className="error">{errors.password}</span>}
        </div>
        {loginError && <span className="error">{loginError}</span>}
        <button type="submit">Login</button>
      </form>
    </div>
  );
};

export default LoginPage;
