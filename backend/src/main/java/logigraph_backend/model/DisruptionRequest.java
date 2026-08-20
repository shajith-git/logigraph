package logigraph_backend.model;

public class DisruptionRequest {

    private String disruptionId;
    private String locationId;
    private String reason;
    private String status;

    public DisruptionRequest() {
    }

    public String getDisruptionId() {
        return disruptionId;
    }

    public void setDisruptionId(String disruptionId) {
        this.disruptionId = disruptionId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}