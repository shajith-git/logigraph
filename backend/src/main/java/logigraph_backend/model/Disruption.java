package logigraph_backend.model;

public class Disruption {

    private String id;
    private String reason;
    private String status;

    public Disruption() {
    }

    public Disruption(String id, String reason, String status) {
        this.id = id;
        this.reason = reason;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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