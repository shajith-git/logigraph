package logigraph_backend.model;

public class Vehicle {

    private String id;
    private String registrationNumber;
    private String type;

    public Vehicle() {
    }

    public Vehicle(String id, String registrationNumber, String type) {
        this.id = id;
        this.registrationNumber = registrationNumber;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}