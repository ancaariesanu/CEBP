import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import supabase from './SupabaseClient';
import './SignUp.css';

const SignUpPage = () => {
  const [formData, setFormData] = useState({
    name: '',
    password: '',
    home_address: '',
    CNP: ''
  });

  const [errors, setErrors] = useState({
    name: '',
    password: '',
    home_address: '',
    CNP: ''
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const validateForm = () => {
    let formErrors = { name: '', password: '', home_address: '', CNP: '' };
    let valid = true;

    if (!formData.name) {
      formErrors.name = 'Name is required';
      valid = false;
    }
    if (!formData.password) {
      formErrors.password = 'Password is required';
      valid = false;
    } else if (formData.password.length < 6) {
      formErrors.password = 'Password must be at least 6 characters long';
      valid = false;
    } else if (
      !/(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*(),.?":{}|<>])/.test(formData.password)
    ) {
      formErrors.password =
        'Password must include at least 1 uppercase letter, 1 lowercase letter, 1 digit, and 1 special character';
      valid = false;
    }

    if (!formData.home_address) {
      formErrors.home_address = 'Home address is required';
      valid = false;
    }

    if (!formData.CNP) {
      formErrors.CNP = 'CNP is required';
      valid = false;
    } else if (!/^\d{13}$/.test(formData.CNP)) {
      formErrors.CNP = 'CNP must be 13 digits';
      valid = false;
    }

    setErrors(formErrors);
    return valid;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (validateForm()) {
      try {
        // Generate user_name by transforming the name into lowercase and replacing spaces with underscores
        const userName = formData.name
          .toLowerCase()
          .split(' ')
          .join('_');

        const { data, error } = await supabase
          .from('customer')
          .insert([
            {
              name: formData.name,
              user_name: userName, // Use the transformed user_name
              pass: formData.password,
              home_address: formData.home_address,
              CNP: formData.CNP,
              joinDate: new Date().toISOString() // Automatically set join date
            }
          ]);

        if (error) {
          console.error('Error creating user:', error);
          alert('Error creating user: ' + error.message);
        } else {
          console.log('User created successfully:', data);
          setFormData({ name: '', password: '', home_address: '', CNP: '' });
          navigate('/login-page');
        }
      } catch (err) {
        console.error('Unexpected error:', err);
        alert('Unexpected error: ' + err.message);
      }
    }
  };

  return (
    <div className="sign-up-container">
      <h2>Sign Up</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="name">Name</label>
          <input
            id="name"
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
            placeholder="Enter your name"
          />
          {errors.name && <span className="error">{errors.name}</span>}
        </div>
        <div className="form-group">
          <label htmlFor="password">Password</label>
          <input
            id="password"
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            placeholder="Enter your password"
          />
          {errors.password && <span className="error">{errors.password}</span>}
        </div>
        <div className="form-group">
          <label htmlFor="home_address">Home Address</label>
          <input
            id="home_address"
            type="text"
            name="home_address"
            value={formData.home_address}
            onChange={handleChange}
            placeholder="Enter your home address"
          />
          {errors.home_address && <span className="error">{errors.home_address}</span>}
        </div>
        <div className="form-group">
          <label htmlFor="CNP">CNP</label>
          <input
            id="CNP"
            type="text"
            name="CNP"
            value={formData.CNP}
            onChange={handleChange}
            placeholder="Enter your CNP (13 digits)"
          />
          {errors.CNP && <span className="error">{errors.CNP}</span>}
        </div>
        <button type="submit">Sign Up</button>
      </form>
    </div>
  );
};

export default SignUpPage;
