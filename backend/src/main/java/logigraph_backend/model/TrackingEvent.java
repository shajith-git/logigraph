package logigraph_backend.model;

public class TrackingEvent {

    private String id;
    private String status;
    private String timestamp;

    public TrackingEvent() {
    }

    public TrackingEvent(String id, String status, String timestamp) {
        this.id = id;
        this.status = status;
        this.timestamp = timestamp;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}