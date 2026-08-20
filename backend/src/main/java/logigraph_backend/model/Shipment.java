package logigraph_backend.model;

public class Shipment {

    private String id;
    private String status;
    private double weight;

    public Shipment() {
    }

    public Shipment(String id, String status, double weight) {
        this.id = id;
        this.status = status;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}