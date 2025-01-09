import React, { useEffect, useState } from "react";
import "./Profile.css";
import supabase from './SupabaseClient';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowLeft } from "@fortawesome/free-solid-svg-icons";
import { useNavigate } from "react-router-dom";
import UserClass from "./UserClass";

function Profile() {
  const [userData, setUserData] = useState(null); // State to store user data
  const [loading, setLoading] = useState(true); // Loading state for user data
  const navigate = useNavigate();

  // Fetch user data from Supabase
  const fetchUserData = async () => {
    const currentUser = UserClass.getUser(); // Get the user from UserClass

    if (!currentUser || !currentUser.user_name) {
      console.error("User is not logged in.");
      return;
    }

    try {
      console.log("Fetching user data for:", currentUser.user_name);
      
      // Query the Supabase customer table to retrieve the user information
      const { data, error } = await supabase
        .from('customer')
        .select('*')
        .eq('user_name', currentUser.user_name)
        .single(); // Fetch a single record based on the user_name

      if (error) {
        console.error("Error fetching user data:", error);
        setLoading(false); // Stop loading if there's an error
        return;
      }

      if (!data) {
        console.error("No data found for user:", currentUser.user_name);
        setLoading(false); // Stop loading if no user data found
        return;
      }

      console.log("User data fetched successfully:", data);

      // Set the fetched data into the state
      setUserData(data);
      setLoading(false); // Stop loading once the data is fetched
    } catch (err) {
      console.error("Error fetching data from Supabase:", err);
      setLoading(false); // Stop loading in case of an error
    }
  };

  // Fetch user data on component mount
  useEffect(() => {
    fetchUserData();
  }, []); // Only run once on mount

  // Handle logout
  const handleLogout = async () => {
    try {
      const { error } = await supabase.auth.signOut();
      if (error) {
        console.error("Error logging out:", error);
      } else {
        console.log("User logged out");
        UserClass.clearUser(); // Clear user data
        navigate("/"); // Redirect to home page
      }
    } catch (err) {
      console.error("Unexpected error during logout:", err);
    }
  };

  if (loading) {
    return <div>Loading user profile...</div>; // Show a loading message while data is being fetched
  }

  if (!userData) {
    return <div>User not found or there was an error fetching the data.</div>; // Show an error if no user data is fetched
  }

  return (
    <div className="profile-page">
      <header className="header">
        <div className="back-button">
          <button onClick={() => navigate("/office-dashboard")}>
            <FontAwesomeIcon icon={faArrowLeft} /> Back to Dashboard
          </button>
        </div>
      </header>
      <h1>User Profile</h1>
      <section className="profile-details">
        <div className="profile-card">
          <h2>Name: {userData.name}</h2>
          <p>User Name: {userData.user_name}</p>
          <p>Home Address: {userData.home_address}</p>
          <p>CNP: {userData.CNP}</p>
          <p>Joined: {new Date(userData.joinDate).toLocaleDateString()}</p>
          <div className="logout-container">
            <button className="logout-button" onClick={handleLogout}>
              Logout
            </button>
          </div>
        </div>
      </section>
    </div>
  );
}

export default Profile;
