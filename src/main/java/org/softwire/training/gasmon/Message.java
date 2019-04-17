package org.softwire.training.gasmon;

import com.google.common.base.MoreObjects;

public class Message {

    private String locationId;
    private String eventId;
    private Double value;
    private Long timestamp;

    public Message(String locationId, String eventId, Double value, Long timestamp) {
        this.locationId = locationId;
        this.eventId = eventId;
        this.value = value;
        this.timestamp = timestamp;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getEventId() {
        return eventId;
    }

    public Double getValue() {
        return value;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String toString () {
        return MoreObjects.toStringHelper(this.getClass())
                .add("Location ID", locationId)
                .add("Event ID", eventId)
                .add("Value", value)
                .add("Timestamp", timestamp)
                .toString();
    }
}
