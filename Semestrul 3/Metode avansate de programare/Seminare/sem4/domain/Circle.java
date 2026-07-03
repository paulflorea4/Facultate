package domain;

public class Circle {

    private double radius;

    public Circle(float radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "domain.Circle{" +
                "radius=" + radius +
                '}';
    }
}
