import React from "react";
import { Link } from "react-router-dom";
import "./OfficeDashboard.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faFileAlt, faUser } from "@fortawesome/free-solid-svg-icons";

function OfficeDashboard() {
  return (
    <div className="dashboard">
      <aside className="sidebar">
        <h2 className="sidebar-title">Bureaucratic System</h2>
        <ul className="office-list">
          <li className="office-item selected">Office 1</li>
          <li className="office-item">Office 2</li>
          <li className="office-item">Office 3</li>
          <li className="office-item">Office 4</li>
        </ul>
      </aside>

      <main className="main-content">
        <header className="header">
          <h1>Office 1</h1>
          <div className="header-buttons">
            <Link to="/uploaded-files" className="button">
              <FontAwesomeIcon icon={faFileAlt} />
            </Link>
            <Link to="/profile" className="button">
              <FontAwesomeIcon icon={faUser} />
            </Link>
          </div>
        </header>

        <div className="tab-buttons">
          <Link to="/sort-by-counters" className="button">
            Counters
          </Link>
          <Link to="/sort-by-documents" className="button">
            Documents
          </Link>
        </div>

        <div className="documents-section">
          {[...Array(7)].map((_, index) => (
            <Link
              to={index === 0 ? "/document-1" : "#"}
              className="document-card"
              key={index}
            >
              <div className="document-icon">▲ ● ■</div>
              <h2 className="document-title">Document {index + 1}</h2>
              <p className="document-status">
                {index === 0
                  ? "1 document missing"
                  : index < 3
                  ? "Pending"
                  : "No request registered"}
              </p>
            </Link>
          ))}
        </div>
      </main>
    </div>
  );
}

export default OfficeDashboard;
