import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Welcome from './components/WelcomePage';
import SignUp from './components/SignUp';
import LoginPage from './components/LoginPage';
import OfficeDashboard from './components/OfficeDashboard';
import DocumentView from './components/DocumentView';
import UploadedFiles from './components/UploadedFiles';
import Profile from './components/Profile';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Welcome />} />
        <Route path="/signup-page" element={<SignUp />} />
        <Route path="/office-dashboard" element={<OfficeDashboard />} />
        <Route path="/document-1" element={<DocumentView />} />
        <Route path="/uploaded-files" element={<UploadedFiles />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/login-page" element={<LoginPage />} />
      </Routes>
    </Router>
  );
};

export default App;
