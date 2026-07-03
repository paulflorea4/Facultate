package domain;

public class Square {

    private double side;

    public Square(float side) {
        this.side = side;
    }

    public double getSide() {
        return side;
    }

    public void setSide(double side) {
        this.side = side;
    }

    @Override
    public String toString() {
        return "domain.Square{" +
                "side=" + side +
                '}';
    }
}
