// ============================================
// LogiGraph - CognoDB Seed Data
// ============================================

// ---------- Locations ----------

MERGE (blr:Location {id: 'LOC-BLR'})
SET blr.city = 'Bangalore',
    blr.state = 'Karnataka'

MERGE (chn:Location {id: 'LOC-CHN'})
SET chn.city = 'Chennai',
    chn.state = 'Tamil Nadu'


// ---------- Customer ----------

MERGE (customer:Customer {id: 'CUS-001'})
SET customer.name = 'Acme Logistics'


// ---------- Shipment ----------

MERGE (shipment:Shipment {id: 'SHP-TEST-001'})
SET shipment.status = 'IN_TRANSIT',
    shipment.weight = 25.5

MERGE (customer)-[:OWNS]->(shipment)

MERGE (shipment)-[:ORIGINATES_FROM]->(blr)

MERGE (shipment)-[:DESTINED_FOR]->(chn)


// ---------- Tracking Event ----------

MERGE (event:TrackingEvent {id: 'EVT-001'})
SET event.status = 'IN_TRANSIT',
    event.timestamp = '2026-08-20T12:30:00'

MERGE (shipment)-[:HAS_EVENT]->(event)

MERGE (event)-[:OCCURRED_AT]->(blr)


// ---------- Vehicle ----------

MERGE (vehicle:Vehicle {id: 'VEH-001'})
SET vehicle.registrationNumber = 'KA-01-AB-1234',
    vehicle.type = 'Truck'

MERGE (shipment)-[:USES]->(vehicle)


// ---------- Driver ----------

MERGE (driver:Driver {id: 'DRV-001'})
SET driver.name = 'Arun Kumar',
    driver.phone = '9876543210'

MERGE (vehicle)-[:DRIVEN_BY]->(driver)


// ---------- Disruption ----------

MERGE (disruption:Disruption {id: 'DIS-001'})
SET disruption.reason = 'Heavy traffic at Bangalore distribution hub',
    disruption.status = 'ACTIVE'

MERGE (disruption)-[:AFFECTS]->(blr)


// ---------- Verification ----------

MATCH (s:Shipment {id: 'SHP-TEST-001'})
RETURN s