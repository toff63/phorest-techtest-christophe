package com.marchal.christophe.phoresttechtest.migration;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

// CSV module defaults to alphabetic ordering so this is optional:
@JsonPropertyOrder({"x", "y", "visible"})
public class Point {
    private int x, y;
    private boolean visible;

    public Point() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point point)) return false;
        return x == point.x && y == point.y && visible == point.visible;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, visible);
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", visible=" + visible +
                '}';
    }
}