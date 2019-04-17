package org.softwire.training.gasmon.model;

import com.google.common.base.MoreObjects;

public class Event {

    private String locationId;
    private String eventId;
    private double value;
    private long timestamp;

    public Event(String locationId, String eventId, double value, long timestamp) {
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

    public double getValue() {
        return value;
    }

    public long getTimestamp() {
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
