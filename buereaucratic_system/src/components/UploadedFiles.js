import React from "react";
import "./UploadedFiles.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowLeft } from "@fortawesome/free-solid-svg-icons";
import { useNavigate } from "react-router-dom"; 

function UploadedFiles() {
    const navigate = useNavigate();

    return (
    <div className="uploaded-files-page">
      <header className="header">
        <div className="back-button">
          <button className="back-button" onClick={() => navigate("/office-dashboard")}>
            <FontAwesomeIcon icon={faArrowLeft} /> Back to Dashboard
          </button>
        </div>
      </header>
      <h1>Uploaded Files</h1>
      <section className="uploaded-files-list">
        {[...Array(5)].map((_, index) => (
          <div className="file-card" key={index}>
            <div className="file-icon">📄</div>
            <h2 className="file-title">File {index + 1}</h2>
            <p className="file-status">Uploaded on: 2024-11-28</p>
          </div>
        ))}
      </section>
    </div>
  );
}

export default UploadedFiles;
