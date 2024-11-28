import React from "react";
import "./Profile.css";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowLeft } from "@fortawesome/free-solid-svg-icons";
import { useNavigate } from "react-router-dom"; 

function Profile() {
    const navigate = useNavigate();
    
    return (
    <div className="profile-page">
      <header className="header">
        <div className="back-button">
          <button className="back-button" onClick={() => navigate("/office-dashboard")}>
            <FontAwesomeIcon icon={faArrowLeft} /> Back to Dashboard
          </button>
        </div>  
      </header>
      <h1>User Profile</h1>
      <section className="profile-details">
        <div className="profile-card">
          <h2>First Name: John</h2>
          <h2>Last Name: Doe</h2>
          <p>Email: john.doe@gmail.com</p>
          <p>Home Address: Independentei nr.14 ap.3</p>
          <p>CNP: 7023467928808</p>
          <p>Joined: 2023-01-15</p>
        </div>
      </section>
    </div>
  );
}

export default Profile;
