import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './SignUp.css';

const SignUpPage = () => {
  const [formData, setFormData] = useState({
    firstname: '',
    lastname: '',
    email: '',
    password: ''
  });

  const [errors, setErrors] = useState({
    firstname: '',
    lastname: '',
    email: '',
    password: ''
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
    let formErrors = { firstname: '', lastname: '', email: '', password: '' };
    let valid = true;
  
    if (!formData.firstname) {
      formErrors.firstname = 'First Name is required';
      valid = false;
    }
    if (!formData.lastname) {
      formErrors.lastname = 'Last Name is required';
      valid = false;
    }
    if (!formData.email) {
      formErrors.email = 'Email is required';
      valid = false;
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      formErrors.email = 'Email is not valid';
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
  
    setErrors(formErrors);
    return valid;
  };  

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validateForm()) {
      console.log('Form submitted successfully:', formData);
      setFormData({ firstname: '', lastname: '', email: '', password: '' });
      navigate('/login-page');
    }
  };

  return (
    <div className="sign-up-container">
      <h2>Sign Up</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="firstname">First Name</label>
          <input
            id="firstname"
            type="text"
            name="firstname"
            value={formData.firstname}
            onChange={handleChange}
            placeholder="Enter your first name"
          />
          {errors.firstname && <span className="error">{errors.firstname}</span>}
        </div>
        <div className="form-group">
          <label htmlFor="lastname">Last Name</label>
          <input
            id="lastname"
            type="text"
            name="lastname"
            value={formData.lastname}
            onChange={handleChange}
            placeholder="Enter your last name"
          />
          {errors.lastname && <span className="error">{errors.lastname}</span>}
        </div>
        <div className="form-group">
          <label htmlFor="email">Email</label>
          <input
            id="email"
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            placeholder="Enter your email"
          />
          {errors.email && <span className="error">{errors.email}</span>}
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
        <button type="submit">Sign Up</button>
      </form>
    </div>
  );
};

export default SignUpPage;
