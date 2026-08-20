import { useEffect, useState } from "react";
import "./index.css";

const API = "http://localhost:8080";

function App() {
  const [shipments, setShipments] = useState([]);
  const [selectedShipment, setSelectedShipment] = useState(null);
  const [locations, setLocations] = useState(null);
  const [tracking, setTracking] = useState([]);
  const [affected, setAffected] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadShipments = async () => {
    try {
      setLoading(true);
      setError("");

      const response = await fetch(`${API}/api/shipments`);

      if (!response.ok) {
        throw new Error("Unable to load shipments");
      }

      const data = await response.json();
      setShipments(data);

      if (data.length > 0) {
        selectShipment(data[0]);
      }
    } catch (err) {
      setError("Backend is unavailable. Make sure Spring Boot is running.");
    } finally {
      setLoading(false);
    }
  };

  const selectShipment = async (shipment) => {
    setSelectedShipment(shipment);

    try {
      const [locationResponse, trackingResponse] = await Promise.all([
        fetch(`${API}/api/shipments/${shipment.id}/locations`),
        fetch(`${API}/api/shipments/${shipment.id}/tracking`)
      ]);

      if (locationResponse.ok) {
        setLocations(await locationResponse.json());
      }

      if (trackingResponse.ok) {
        setTracking(await trackingResponse.json());
      }
    } catch (err) {
      console.error(err);
    }
  };

  const loadAffectedShipments = async () => {
    try {
      const response = await fetch(
          `${API}/api/disruptions/DIS-001/shipments`
      );

      if (response.ok) {
        setAffected(await response.json());
      }
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    loadShipments();
    loadAffectedShipments();
  }, []);

  return (
      <div className="app">

        <header className="topbar">
          <div>
            <div className="brand">LOGIGRAPH</div>
            <div className="subtitle">Shipment Intelligence Platform</div>
          </div>

          <div className="status">
            <span className="status-dot"></span>
            CognoDB Connected
          </div>
        </header>

        <main className="container">

          <section className="hero">
            <div>
              <p className="eyebrow">GRAPH-POWERED LOGISTICS</p>
              <h1>Shipment Intelligence</h1>
              <p className="hero-text">
                Explore shipments, routes, tracking events and disruption
                impact through a connected logistics graph.
              </p>
            </div>
          </section>

          {error && (
              <div className="error">
                ⚠ {error}
              </div>
          )}

          <section className="stats">

            <div className="stat-card">
              <span>Total Shipments</span>
              <strong>{shipments.length}</strong>
            </div>

            <div className="stat-card">
              <span>In Transit</span>
              <strong>
                {shipments.filter(
                    (s) => s.status === "IN_TRANSIT"
                ).length}
              </strong>
            </div>

            <div className="stat-card">
              <span>Active Disruptions</span>
              <strong>1</strong>
            </div>

            <div className="stat-card">
              <span>Affected Shipments</span>
              <strong>{affected.length}</strong>
            </div>

          </section>

          <section className="content-grid">

            <div className="panel">

              <div className="panel-header">
                <div>
                  <h2>Shipments</h2>
                  <p>Select a shipment to inspect its graph relationships.</p>
                </div>
              </div>

              {loading ? (
                  <div className="empty">Loading shipments...</div>
              ) : shipments.length === 0 ? (
                  <div className="empty">No shipments found.</div>
              ) : (
                  <div className="shipment-list">
                    {shipments.map((shipment) => (
                        <button
                            className={`shipment-row ${
                                selectedShipment?.id === shipment.id
                                    ? "selected"
                                    : ""
                            }`}
                            key={shipment.id}
                            onClick={() => selectShipment(shipment)}
                        >
                          <div>
                            <strong>{shipment.id}</strong>
                            <span>{shipment.weight} kg</span>
                          </div>

                          <span className="badge">
                      {shipment.status}
                    </span>
                        </button>
                    ))}
                  </div>
              )}

            </div>

            <div className="panel">

              <div className="panel-header">
                <div>
                  <h2>Shipment Details</h2>
                  <p>Connected logistics information.</p>
                </div>
              </div>

              {selectedShipment ? (
                  <div className="details">

                    <div className="shipment-title">
                      <div>
                        <span className="label">SHIPMENT</span>
                        <h3>{selectedShipment.id}</h3>
                      </div>

                      <span className="badge large">
                    {selectedShipment.status}
                  </span>
                    </div>

                    <div className="route">

                      <div className="location">
                        <span className="route-label">ORIGIN</span>
                        <strong>
                          {locations?.originCity || "—"}
                        </strong>
                        <small>
                          {locations?.originState || ""}
                        </small>
                      </div>

                      <div className="route-line">
                        <span>→</span>
                      </div>

                      <div className="location">
                        <span className="route-label">DESTINATION</span>
                        <strong>
                          {locations?.destinationCity || "—"}
                        </strong>
                        <small>
                          {locations?.destinationState || ""}
                        </small>
                      </div>

                    </div>

                    <div className="info-row">
                      <div>
                        <span>Weight</span>
                        <strong>{selectedShipment.weight} kg</strong>
                      </div>

                      <div>
                        <span>Tracking Events</span>
                        <strong>{tracking.length}</strong>
                      </div>
                    </div>

                    <div className="tracking">
                      <h3>Tracking History</h3>

                      {tracking.length === 0 ? (
                          <p className="muted">
                            No tracking events found.
                          </p>
                      ) : (
                          tracking.map((event) => (
                              <div className="event" key={event.id}>
                                <div className="event-dot"></div>

                                <div>
                                  <strong>{event.status}</strong>
                                  <p>{event.timestamp}</p>
                                  <small>
                                    Event ID: {event.id}
                                  </small>
                                </div>
                              </div>
                          ))
                      )}
                    </div>

                  </div>
              ) : (
                  <div className="empty">
                    Select a shipment.
                  </div>
              )}

            </div>

          </section>

          <section className="disruption">

            <div className="disruption-header">
              <div>
              <span className="eyebrow warning">
                ACTIVE DISRUPTION
              </span>

                <h2>Heavy traffic at Bangalore distribution hub</h2>

                <p>
                  DIS-001 · Bangalore · Active
                </p>
              </div>

              <div className="warning-icon">!</div>
            </div>

            <div className="impact">

              <div>
                <span>Affected shipments</span>
                <strong>{affected.length}</strong>
              </div>

              <div className="affected-list">
                {affected.map((shipment) => (
                    <button
                        key={shipment.id}
                        onClick={() => selectShipment(shipment)}
                    >
                      {shipment.id}
                      <span>{shipment.status}</span>
                    </button>
                ))}
              </div>

            </div>

          </section>

          <section className="graph-explanation">

            <div>
              <span className="eyebrow">WHY GRAPH?</span>
              <h2>Relationships are the intelligence.</h2>
            </div>

            <p>
              LogiGraph connects shipments with customers, vehicles,
              drivers, locations, tracking events and disruptions.
              A disruption can therefore be traversed through the
              graph to identify affected shipments without manually
              joining multiple independent data sources.
            </p>

          </section>

        </main>

        <footer>
          LogiGraph · Built with Java, Spring Boot, React and CognoDB
        </footer>

      </div>
  );
}

export default App;