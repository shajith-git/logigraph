package logigraph_backend.controller;

import logigraph_backend.model.Shipment;
import logigraph_backend.service.CognoDbService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import logigraph_backend.model.Customer;
import org.springframework.web.bind.annotation.RequestParam;
import logigraph_backend.model.Location;
import logigraph_backend.model.LocationRequest;
import logigraph_backend.model.Driver;
import logigraph_backend.model.Vehicle;
import logigraph_backend.model.VehicleDriverRequest;
import logigraph_backend.model.TrackingEvent;
import logigraph_backend.model.TrackingEventRequest;
import logigraph_backend.model.Disruption;
import logigraph_backend.model.DisruptionRequest;

@RestController
public class CognoDbController {

    private final CognoDbService cognoDbService;

    public CognoDbController(CognoDbService cognoDbService) {
        this.cognoDbService = cognoDbService;
    }
    @PostMapping("/api/shipments/{shipmentId}/vehicle")
    public String assignVehicleAndDriver(
            @PathVariable String shipmentId,
            @RequestBody VehicleDriverRequest request) {

        Vehicle vehicle = new Vehicle(
                request.getVehicleId(),
                request.getRegistrationNumber(),
                request.getVehicleType()
        );

        Driver driver = new Driver(
                request.getDriverId(),
                request.getDriverName(),
                request.getDriverPhone()
        );

        return cognoDbService.assignVehicleAndDriver(
                shipmentId,
                vehicle,
                driver
        );
    }
    @GetMapping("/api/shipments")
    public java.util.List<Shipment> getAllShipments() {
        return cognoDbService.getAllShipments();
    }
    @GetMapping("/api/shipments/{id}")
    public Shipment getShipmentById(@PathVariable String id) {
        return cognoDbService.getShipmentById(id);
    }

    @GetMapping("/api/test-db")
    public String testDatabase() {
        return cognoDbService.testConnection();
    }

    @PostMapping("/api/shipments")
    public String createShipment(@RequestBody Shipment shipment) {
        return cognoDbService.createShipment(shipment);
    }

    @PutMapping("/api/shipments/{id}")
    public String updateShipment(
            @PathVariable String id,
            @RequestBody Shipment shipment) {

        return cognoDbService.updateShipment(id, shipment);
    }
    @DeleteMapping("/api/shipments/{id}")
    public boolean deleteShipment(@PathVariable String id) {
        return cognoDbService.deleteShipment(id);
    }
    @PostMapping("/api/customers/{customerId}/shipments/{shipmentId}")
    public String assignShipment(
            @PathVariable String customerId,
            @PathVariable String shipmentId,
            @RequestParam String name) {

        Customer customer = new Customer(customerId, name);

        return cognoDbService.createCustomerAndAssignShipment(
                customer,
                shipmentId
        );
    }
    @PostMapping("/api/shipments/{shipmentId}/locations")
    public String assignLocations(
            @PathVariable String shipmentId,
            @RequestBody LocationRequest request) {

        Location origin = new Location(
                request.getOriginId(),
                request.getOriginCity(),
                request.getOriginState()
        );

        Location destination = new Location(
                request.getDestinationId(),
                request.getDestinationCity(),
                request.getDestinationState()
        );

        return cognoDbService.assignLocations(
                shipmentId,
                origin,
                destination
        );
    }
    @GetMapping("/api/shipments/{shipmentId}/locations")
    public LocationRequest getShipmentLocations(
            @PathVariable String shipmentId) {

        return cognoDbService.getShipmentLocations(shipmentId);
    }
    @PostMapping("/api/shipments/{shipmentId}/tracking")
    public String addTrackingEvent(
            @PathVariable String shipmentId,
            @RequestBody TrackingEventRequest request) {

        TrackingEvent event = new TrackingEvent(
                request.getEventId(),
                request.getStatus(),
                request.getTimestamp()
        );

        return cognoDbService.addTrackingEvent(
                shipmentId,
                event,
                request.getLocationId()
        );
    }

    @PostMapping("/api/disruptions")
    public String createDisruption(
            @RequestBody DisruptionRequest request) {

        Disruption disruption = new Disruption(
                request.getDisruptionId(),
                request.getReason(),
                request.getStatus()
        );

        return cognoDbService.createDisruption(
                disruption,
                request.getLocationId()
        );
    }



    @GetMapping("/api/disruptions/{disruptionId}/shipments")
    public java.util.List<Shipment> getAffectedShipments(
            @PathVariable String disruptionId) {

        return cognoDbService.getShipmentsAffectedByDisruption(
                disruptionId
        );
    }
    @GetMapping("/api/shipments/{shipmentId}/tracking")
    public java.util.List<TrackingEvent> getTrackingEvents(
            @PathVariable String shipmentId) {

        return cognoDbService.getTrackingEvents(shipmentId);
    }
}