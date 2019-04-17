package org.softwire.training.gasmon;

import com.google.common.base.MoreObjects;

public class LocationData {

    private String id;
    private Double x;
    private Double y;

    public LocationData(String id, Double x, Double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this.getClass())
                .add("ID", id)
                .add("x", x)
                .add("y", y)
                .toString();
    }
}
