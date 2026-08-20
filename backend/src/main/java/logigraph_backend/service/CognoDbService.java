package logigraph_backend.service;

import logigraph_backend.model.Shipment;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Service;
import logigraph_backend.model.Customer;
import logigraph_backend.model.Location;
import logigraph_backend.model.Vehicle;
import logigraph_backend.model.TrackingEvent;
import logigraph_backend.model.LocationRequest;
import logigraph_backend.model.Disruption;

@Service
public class CognoDbService {

    private final Driver driver;

    public CognoDbService(Driver driver) {
        this.driver = driver;
    }

    public String testConnection() {

        try (Session session = driver.session()) {

            return session.executeRead(tx ->
                    tx.run("RETURN 'CognoDB connection successful!' AS message")
                            .single()
                            .get("message")
                            .asString()
            );
        }
    }

    public String createShipment(Shipment shipment) {

        String query = """
            MERGE (s:Shipment {id: $id})
            SET s.status = $status,
                s.weight = $weight
            RETURN s.id AS id
            """;

        try (Session session = driver.session()) {

            return session.executeWrite(tx ->
                    tx.run(
                                    query,
                                    org.neo4j.driver.Values.parameters(
                                            "id", shipment.getId(),
                                            "status", shipment.getStatus(),
                                            "weight", shipment.getWeight()
                                    )
                            )
                            .single()
                            .get("id")
                            .asString()
            );
        }
    }
    public java.util.List<Shipment> getAllShipments() {

        String query = """
            MATCH (s:Shipment)
            RETURN s.id AS id,
                   s.status AS status,
                   s.weight AS weight
            ORDER BY s.id
            """;

        try (Session session = driver.session()) {

            return session.executeRead(tx ->
                    tx.run(query)
                            .list(record -> new Shipment(
                                    record.get("id").asString(),
                                    record.get("status").asString(),
                                    record.get("weight").asDouble()
                            ))
            );
        }
    }
    public Shipment getShipmentById(String id) {

        String query = """
            MATCH (s:Shipment {id: $id})
            RETURN s.id AS id,
                   s.status AS status,
                   s.weight AS weight
            """;

        try (Session session = driver.session()) {

            return session.executeRead(tx ->
                    tx.run(query,
                                    org.neo4j.driver.Values.parameters("id", id))
                            .list(record -> new Shipment(
                                    record.get("id").asString(),
                                    record.get("status").asString(),
                                    record.get("weight").asDouble()
                            ))
                            .stream()
                            .findFirst()
                            .orElse(null)
            );
        }
    }
    public String updateShipment(String id, Shipment shipment) {

        String query = """
            MATCH (s:Shipment {id: $id})
            SET s.status = $status,
                s.weight = $weight
            RETURN s.id AS id
            """;

        try (Session session = driver.session()) {

            return session.executeWrite(tx ->
                    tx.run(
                                    query,
                                    org.neo4j.driver.Values.parameters(
                                            "id", id,
                                            "status", shipment.getStatus(),
                                            "weight", shipment.getWeight()
                                    )
                            )
                            .list(record -> record.get("id").asString())
                            .stream()
                            .findFirst()
                            .orElse(null)
            );
        }
    }
    public boolean deleteShipment(String id) {

        String query = """
            MATCH (s:Shipment {id: $id})
            DELETE s
            RETURN count(s) AS deleted
            """;

        try (Session session = driver.session()) {

            return session.executeWrite(tx ->
                    tx.run(
                                    query,
                                    org.neo4j.driver.Values.parameters("id", id)
                            )
                            .single()
                            .get("deleted")
                            .asLong() > 0
            );
        }
    }
    public String createCustomerAndAssignShipment(
            Customer customer,
            String shipmentId) {

        String query = """
            MERGE (c:Customer {id: $customerId})
            SET c.name = $customerName

            WITH c

            MATCH (s:Shipment {id: $shipmentId})

            MERGE (c)-[:OWNS]->(s)

            RETURN c.id AS customerId
            """;

        try (Session session = driver.session()) {

            return session.executeWrite(tx ->
                    tx.run(
                                    query,
                                    org.neo4j.driver.Values.parameters(
                                            "customerId", customer.getId(),
                                            "customerName", customer.getName(),
                                            "shipmentId", shipmentId
                                    )
                            )
                            .single()
                            .get("customerId")
                            .asString()
            );
        }
    }
    public String assignLocations(
            String shipmentId,
            Location origin,
            Location destination) {

        String query = """
            MATCH (s:Shipment {id: $shipmentId})

            MERGE (o:Location {id: $originId})
            SET o.city = $originCity,
                o.state = $originState

            MERGE (d:Location {id: $destinationId})
            SET d.city = $destinationCity,
                d.state = $destinationState

            MERGE (s)-[:ORIGINATES_FROM]->(o)
            MERGE (s)-[:DESTINED_FOR]->(d)

            RETURN s.id AS shipmentId
            """;

        try (Session session = driver.session()) {

            return session.executeWrite(tx ->
                    tx.run(
                                    query,
                                    org.neo4j.driver.Values.parameters(
                                            "shipmentId", shipmentId,

                                            "originId", origin.getId(),
                                            "originCity", origin.getCity(),
                                            "originState", origin.getState(),

                                            "destinationId", destination.getId(),
                                            "destinationCity", destination.getCity(),
                                            "destinationState", destination.getState()
                                    )
                            )
                            .single()
                            .get("shipmentId")
                            .asString()
            );
        }
    }
    public String assignVehicleAndDriver(
            String shipmentId,
            Vehicle vehicle,
            logigraph_backend.model.Driver logisticsDriver) {

        String query = """
            MATCH (s:Shipment {id: $shipmentId})

            MERGE (v:Vehicle {id: $vehicleId})
            SET v.registrationNumber = $registrationNumber,
                v.type = $vehicleType

            MERGE (d:Driver {id: $driverId})
            SET d.name = $driverName,
                d.phone = $driverPhone

            MERGE (s)-[:ASSIGNED_TO]->(v)
            MERGE (v)-[:DRIVEN_BY]->(d)

            RETURN s.id AS shipmentId
            """;

        try (Session session = driver.session()) {

            return session.executeWrite(tx ->
                    tx.run(
                                    query,
                                    org.neo4j.driver.Values.parameters(
                                            "shipmentId", shipmentId,

                                            "vehicleId", vehicle.getId(),
                                            "registrationNumber",
                                            vehicle.getRegistrationNumber(),
                                            "vehicleType",
                                            vehicle.getType(),

                                            "driverId", logisticsDriver.getId(),
                                            "driverName", logisticsDriver.getName(),
                                            "driverPhone", logisticsDriver.getPhone()
                                    )
                            )
                            .single()
                            .get("shipmentId")
                            .asString()
            );
        }
    }
    public String addTrackingEvent(
            String shipmentId,
            TrackingEvent event,
            String locationId) {

        String query = """
            MATCH (s:Shipment {id: $shipmentId})

            MERGE (e:TrackingEvent {id: $eventId})
            SET e.status = $status,
                e.timestamp = $timestamp

            MERGE (l:Location {id: $locationId})

            MERGE (s)-[:HAS_EVENT]->(e)
            MERGE (e)-[:OCCURRED_AT]->(l)

            SET s.status = $status

            RETURN e.id AS eventId
            """;

        try (Session session = driver.session()) {

            return session.executeWrite(tx ->
                    tx.run(
                                    query,
                                    org.neo4j.driver.Values.parameters(
                                            "shipmentId", shipmentId,
                                            "eventId", event.getId(),
                                            "status", event.getStatus(),
                                            "timestamp", event.getTimestamp(),
                                            "locationId", locationId
                                    )
                            )
                            .single()
                            .get("eventId")
                            .asString()
            );
        }
    }
    public java.util.List<TrackingEvent> getTrackingEvents(String shipmentId) {

        String query = """
        MATCH (s:Shipment {id: $shipmentId})
              -[:HAS_EVENT]->(e:TrackingEvent)
        RETURN e.id AS id,
               e.status AS status,
               e.timestamp AS timestamp
        ORDER BY e.timestamp
        """;

        try (Session session = driver.session()) {

            return session.executeRead(tx ->
                    tx.run(
                            query,
                            org.neo4j.driver.Values.parameters(
                                    "shipmentId", shipmentId
                            )
                    ).list(record -> new TrackingEvent(
                            record.get("id").asString(),
                            record.get("status").asString(),
                            record.get("timestamp").asString()
                    ))
            );
        }
    }
    public LocationRequest getShipmentLocations(String shipmentId) {

        String query = """
        MATCH (s:Shipment {id: $shipmentId})
        OPTIONAL MATCH (s)-[:ORIGINATES_FROM]->(origin:Location)
        OPTIONAL MATCH (s)-[:DESTINED_FOR]->(destination:Location)

        RETURN origin.id AS originId,
               origin.city AS originCity,
               origin.state AS originState,
               destination.id AS destinationId,
               destination.city AS destinationCity,
               destination.state AS destinationState
        """;

        try (Session session = driver.session()) {

            return session.executeRead(tx ->
                    tx.run(
                                    query,
                                    org.neo4j.driver.Values.parameters(
                                            "shipmentId", shipmentId
                                    )
                            )
                            .list(record -> {

                                LocationRequest result = new LocationRequest();

                                result.setOriginId(record.get("originId").asString());
                                result.setOriginCity(record.get("originCity").asString());
                                result.setOriginState(record.get("originState").asString());

                                result.setDestinationId(record.get("destinationId").asString());
                                result.setDestinationCity(record.get("destinationCity").asString());
                                result.setDestinationState(record.get("destinationState").asString());

                                return result;
                            })
                            .stream()
                            .findFirst()
                            .orElse(null)
            );
        }
    }
    public String createDisruption(
            Disruption disruption,
            String locationId) {

        String query = """
        MERGE (d:Disruption {id: $disruptionId})
        SET d.reason = $reason,
            d.status = $status

        MATCH (l:Location {id: $locationId})

        MERGE (d)-[:AFFECTS]->(l)

        RETURN d.id AS disruptionId
        """;

        try (Session session = driver.session()) {

            return session.executeWrite(tx ->
                    tx.run(
                                    query,
                                    org.neo4j.driver.Values.parameters(
                                            "disruptionId", disruption.getId(),
                                            "reason", disruption.getReason(),
                                            "status", disruption.getStatus(),
                                            "locationId", locationId
                                    )
                            )
                            .single()
                            .get("disruptionId")
                            .asString()
            );
        }
    }
    public java.util.List<Shipment> getAffectedShipments(
            String disruptionId) {

        String query = """
        MATCH (d:Disruption {id: $disruptionId})
              -[:AFFECTS]->(l:Location)

        MATCH (s:Shipment)
        WHERE (s)-[:ORIGINATES_FROM]->(l)
           OR (s)-[:DESTINED_FOR]->(l)
           OR (s)-[:HAS_EVENT]->(:TrackingEvent)-[:OCCURRED_AT]->(l)

        RETURN DISTINCT s.id AS id,
               s.status AS status,
               s.weight AS weight
        ORDER BY s.id
        """;

        try (Session session = driver.session()) {

            return session.executeRead(tx ->
                    tx.run(
                            query,
                            org.neo4j.driver.Values.parameters(
                                    "disruptionId", disruptionId
                            )
                    ).list(record -> new Shipment(
                            record.get("id").asString(),
                            record.get("status").asString(),
                            record.get("weight").asDouble()
                    ))
            );
        }
    }


    public java.util.List<Shipment> getShipmentsAffectedByDisruption(
            String disruptionId) {

        String query = """
        MATCH (d:Disruption {id: $disruptionId})
              -[:AFFECTS]->(l:Location)

        MATCH (s:Shipment)-[:ORIGINATES_FROM|DESTINED_FOR]->(l)

        RETURN DISTINCT
               s.id AS id,
               s.status AS status,
               s.weight AS weight
        ORDER BY s.id
        """;

        try (Session session = driver.session()) {

            return session.executeRead(tx ->
                    tx.run(
                            query,
                            org.neo4j.driver.Values.parameters(
                                    "disruptionId", disruptionId
                            )
                    ).list(record -> new Shipment(
                            record.get("id").asString(),
                            record.get("status").asString(),
                            record.get("weight").asDouble()
                    ))
            );
        }
    }
}