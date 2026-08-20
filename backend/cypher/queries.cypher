// ============================================================
// LogiGraph - Representative Cypher Queries
// ============================================================

// 1. Find all shipments
MATCH (s:Shipment)
RETURN s
ORDER BY s.id;


// 2. Find the origin and destination of a shipment
MATCH (s:Shipment {id: $shipmentId})
OPTIONAL MATCH (s)-[:ORIGINATES_FROM]->(origin:Location)
OPTIONAL MATCH (s)-[:DESTINED_FOR]->(destination:Location)
RETURN s, origin, destination;


// 3. Multi-hop traversal:
// Shipment -> TrackingEvent -> Location
MATCH (s:Shipment {id: $shipmentId})
      -[:HAS_EVENT]->(event:TrackingEvent)
      -[:OCCURRED_AT]->(location:Location)
RETURN s.id AS shipmentId,
       event.id AS eventId,
       event.status AS status,
       event.timestamp AS timestamp,
       location.city AS city;


// 4. Graph-specific disruption analysis:
// Shipment -> Event -> Location -> Disruption
MATCH (s:Shipment {id: $shipmentId})
      -[:HAS_EVENT]->(:TrackingEvent)
      -[:OCCURRED_AT]->(location:Location)
      -[:AFFECTS]->(disruption:Disruption)
RETURN s.id AS shipmentId,
       location.city AS affectedLocation,
       disruption.id AS disruptionId,
       disruption.reason AS reason,
       disruption.status AS disruptionStatus;


// 5. Find every shipment affected by a disruption
MATCH (disruption:Disruption {id: $disruptionId})
      <-[:AFFECTS]-(location:Location)
      <-[:OCCURRED_AT]-(event:TrackingEvent)
      <-[:HAS_EVENT]-(shipment:Shipment)
RETURN DISTINCT shipment;


// 6. Find the vehicle and driver assigned to a shipment
MATCH (s:Shipment {id: $shipmentId})
      -[:ASSIGNED_TO]->(vehicle:Vehicle)
      -[:DRIVEN_BY]->(driver:Driver)
RETURN s.id AS shipmentId,
       vehicle.id AS vehicleId,
       driver.id AS driverId,
       driver.name AS driverName;