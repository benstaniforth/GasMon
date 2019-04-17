package org.softwire.training.gasmon;

import com.google.common.base.MoreObjects;

public class LocationData {

    private String id;
    private double x;
    private double y;

    public LocationData(String id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
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
