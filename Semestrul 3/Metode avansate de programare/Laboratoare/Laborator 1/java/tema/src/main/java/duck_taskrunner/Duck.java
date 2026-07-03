package duck_taskrunner;

public class Duck {
    private int id;
    private double speed;
    private double resistance;

    public Duck(int id,double speed, double resistance) {
        this.id=id;
        this.speed = speed;
        this.resistance = resistance;
    }

    public int getId() {return id;}

    public double getSpeed() {
        return speed;
    }

    public double getResistance() {
        return resistance;
    }

    public void setResistance(double resistance) {
        this.resistance = resistance;
    }
}
