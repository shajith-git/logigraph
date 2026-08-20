package logigraph_backend.model;

public class ShipmentGraphResponse {

    private String shipmentId;
    private String shipmentStatus;
    private double weight;

    private String customerId;
    private String customerName;

    private String vehicleId;
    private String registrationNumber;
    private String vehicleType;

    private String driverId;
    private String driverName;
    private String driverPhone;

    public ShipmentGraphResponse(
            String shipmentId,
            String shipmentStatus,
            double weight,
            String customerId,
            String customerName,
            String vehicleId,
            String registrationNumber,
            String vehicleType,
            String driverId,
            String driverName,
            String driverPhone) {

        this.shipmentId = shipmentId;
        this.shipmentStatus = shipmentStatus;
        this.weight = weight;
        this.customerId = customerId;
        this.customerName = customerName;
        this.vehicleId = vehicleId;
        this.registrationNumber = registrationNumber;
        this.vehicleType = vehicleType;
        this.driverId = driverId;
        this.driverName = driverName;
        this.driverPhone = driverPhone;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public String getShipmentStatus() {
        return shipmentStatus;
    }

    public double getWeight() {
        return weight;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }
}