import React, { useState, useEffect } from "react";
import "./DocumentView.css"; // Import a CSS file for styling
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowLeft, faCheckSquare, faSquare } from "@fortawesome/free-solid-svg-icons";
import { useNavigate, useParams } from "react-router-dom";

function DocumentView() {
  const navigate = useNavigate();
  const { id } = useParams(); // Retrieve the document ID from route parameters

  // State to manage the checkbox status
  const [checkedState, setCheckedState] = useState([]); // Dynamic state for checkboxes
  const [document, setDocument] = useState(null); // State to store the document data
  const [error, setError] = useState(null); // State to handle API errors

  // Fetch document data from the API
  useEffect(() => {
    const fetchDocument = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/document/${id}`);
        alert(response)
        if (!response.ok) {
          throw new Error(`Failed to fetch document: ${response.statusText}`);
        }
        const data = await response.json();
        setDocument(data);

        // Initialize checkbox state based on dependencies
        setCheckedState(data.dependencies.map(() => false));
      } catch (error) {
        console.error("Error fetching document:", error);
        setError(error.message);
      }
    };

    fetchDocument();
  }, [id]);

  // Toggle function for checkboxes
  const toggleCheckbox = (index) => {
    setCheckedState((prev) =>
      prev.map((state, i) => (i === index ? !state : state))
    );
  };

  // If there's an error, display it
  if (error) {
    return <div className="error">Error: {error}</div>;
  }

  // If the document data is not loaded, show a loading message
  if (!document) {
    return <div>Loading...</div>;
  }

  return (
    <div className="document-view">
      <aside className="sidebar">
        <h2 className="sidebar-title">BureuNet</h2>
        <ul className="document-list">
          <li className="document-item selected">{document.name}</li>
        </ul>
      </aside>

      <main className="main-content">
        <header className="header">
          <div className="back-button">
            <button onClick={() => navigate("/office-dashboard")}>
              <FontAwesomeIcon icon={faArrowLeft} /> Back to Dashboard
            </button>
          </div>
        </header>

        <section className="document-details">
          <div className="document-header">
            <div className="document-icon">▲ ● ■</div>
            <h2 className="document-title">{document.name}</h2>
          </div>

          <p className="document-description">
            <strong>Description:</strong>{" "}
            {document.description || "No description available."}
          </p>

          <div className="mandatory-documents">
            <h3>Dependencies:</h3>
            {document.dependencies && document.dependencies.length > 0 ? (
              document.dependencies.map((dependency, index) => (
                <label key={index}>
                  <FontAwesomeIcon
                    icon={checkedState[index] ? faCheckSquare : faSquare}
                  />{" "}
                  {dependency}
                  <input
                    type="checkbox"
                    checked={checkedState[index]}
                    onChange={() => toggleCheckbox(index)}
                    style={{ display: "none" }}
                  />
                </label>
              ))
            ) : (
              <p>No dependencies available.</p>
            )}
          </div>

          <div className="status">
            <strong>Document ID:</strong> {document.documentId}
          </div>

          <button className="upload-button">Upload Documents</button>
        </section>
      </main>
    </div>
  );
}

export default DocumentView;
