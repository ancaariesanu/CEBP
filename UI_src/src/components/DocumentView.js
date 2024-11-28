import React, { useState } from "react";
import "./DocumentView.css"; // Import a CSS file for styling
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowLeft, faDownload } from "@fortawesome/free-solid-svg-icons";
import { useNavigate } from "react-router-dom";
import { faCheckSquare, faSquare } from "@fortawesome/free-solid-svg-icons";

function DocumentView() {
  const navigate = useNavigate(); // Hook for navigation

  // State to manage the checkbox status
  const [isChecked1, setIsChecked1] = useState(false); // ID Card checkbox
  const [isChecked2, setIsChecked2] = useState(false); // Typed Request checkbox

  // Toggle function for checkboxes
  const toggleCheckbox = (checkbox) => {
    if (checkbox === 1) {
      setIsChecked1(!isChecked1);
    } else if (checkbox === 2) {
      setIsChecked2(!isChecked2);
    }
  };

  return (
    <div className="document-view">
      <aside className="sidebar">
        <h2 className="sidebar-title">Bureaucratic System</h2>
        <ul className="document-list">
          <li className="document-item selected">Document 1</li>
          <li className="document-item">Document 2</li>
          <li className="document-item">Document 3</li>
          <li className="document-item">Document 4</li>
        </ul>
      </aside>

      <main className="main-content">
        <header className="header">
          {/* Change the navigation path to /office-dashboard */}
          <div className="back-button">
            <button className="back-button" onClick={() => navigate("/office-dashboard")}>
              <FontAwesomeIcon icon={faArrowLeft} /> Back to Dashboard
            </button>
          </div>
        </header>

        <section className="document-details">
          <div className="document-header">
            <div className="document-icon">▲ ● ■</div>
            <h2 className="document-title">Document 1</h2>
          </div>

          <p className="document-description">
            <strong>Description:</strong> Lorem Ipsum is simply dummy text of the printing
            and typesetting industry. Lorem Ipsum has been the industry’s standard dummy
            text ever since the 1500s.
          </p>

          <div className="mandatory-documents">
            <h3>Mandatory documents:</h3>
            <label onClick={() => toggleCheckbox(1)}>
              <FontAwesomeIcon icon={isChecked1 ? faCheckSquare : faSquare} /> ID Card
            </label>
            <label onClick={() => toggleCheckbox(2)}>
              <FontAwesomeIcon icon={isChecked2 ? faCheckSquare : faSquare} /> Typed request
              <button className="download-button">
                <FontAwesomeIcon icon={faDownload} />
              </button>
            </label>
          </div>

          <div className="status">
            <strong>Status:</strong> 1 document missing
          </div>

          <button className="upload-button">Upload documents</button>
        </section>
      </main>
    </div>
  );
}

export default DocumentView;
