import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import "./OfficeDashboard.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faFileAlt, faUser } from "@fortawesome/free-solid-svg-icons";

function OfficeDashboard() {
  const [offices, setOffices] = useState([]); // State to hold office data
  const [selectedOffice, setSelectedOffice] = useState(null); // Null by default (no office selected)
  const [document, setDocument] = useState(null); // State to hold document data

  useEffect(() => {
    const fetchOffices = async () => {
      try {
        console.log("Fetching office data...");

        // Fetch office data from the API endpoint
        const response = await fetch("http://localhost:8080/api/offices/list");

        // Check for HTTP errors
        if (!response.ok) {
          throw new Error(`Failed to fetch offices: ${response.statusText}`);
        }

        const data = await response.json(); // Parse JSON response
        console.log("Fetched office data:", data);

        // Set the office data in the state
        setOffices(data);
      } catch (error) {
        console.error("Error fetching offices:", error);
      }
    };

    const fetchDocument = async () => {
      try {
        console.log("Fetching document data...");

        // Fetch document data from the API endpoint
        const response = await fetch("http://localhost:8080/api/document/103");

        // Check for HTTP errors
        if (!response.ok) {
          throw new Error(`Failed to fetch document: ${response.statusText}`);
        }

        const data = await response.json(); // Parse JSON response
        console.log("Fetched document data:", data);

        // Set the document data in the state
        setDocument(data);
      } catch (error) {
        console.error("Error fetching document:", error);
      }
    };

    fetchOffices(); // Fetch offices when the component mounts
    fetchDocument(); // Fetch document when the component mounts
  }, []);

  return (
    <div className="dashboard">
      {/* Sidebar for office list */}
      <aside className="sidebar">
        <h2 className="sidebar-title">BureuNet</h2>
        <ul className="office-list">
          {/* Render the list of offices */}
          {offices.length > 0 ? (
            offices.map((office) => (
              <li
                key={office.id}
                className={`office-item ${
                  office.name === selectedOffice ? "selected" : ""
                }`}
                onClick={() => setSelectedOffice(office.name)} // Set the selected office
              >
                {office.name}
              </li>
            ))
          ) : (
            <li className="office-item">No offices available</li>
          )}
        </ul>
      </aside>

      {/* Main content section */}
      <main className="main-content">
        <header className="header">
          {selectedOffice ? (
            <>
              <h1>{selectedOffice}</h1> {/* Display the selected office */}
              <div className="header-buttons">
                <Link to="/uploaded-files" className="button">
                  <FontAwesomeIcon icon={faFileAlt} />
                </Link>
                <Link to="/profile" className="button">
                  <FontAwesomeIcon icon={faUser} />
                </Link>
              </div>
            </>
          ) : (
            <>
              <h1>Welcome to the BureuNet</h1> {/* Welcome message */}
              <div className="header-buttons">
                <Link to="/uploaded-files" className="button">
                  <FontAwesomeIcon icon={faFileAlt} />
                </Link>
                <Link to="/profile" className="button">
                  <FontAwesomeIcon icon={faUser} />
                </Link>
              </div>
            </>
          )}
        </header>

        {/* If an office is selected, show additional details */}
        {selectedOffice && document && (
          <>
            <div className="tab-buttons">
              <Link to="/sort-by-counters" className="button">
                Counters
              </Link>
              <Link to="/sort-by-documents" className="button">
                Documents
              </Link>
            </div>

            {/* Display the fetched document */}
            <div className="documents-section">
              <Link to={`/document/${document.documentId}`} className="document-card">
                <div className="document-icon">▲ ● ■</div>
                <h2 className="document-title">{document.name}</h2>
                <p className="document-status">
                  {document.description || "No description available"}
                </p>
              </Link>
            </div>
          </>
        )}
      </main>
    </div>
  );
}

export default OfficeDashboard;
