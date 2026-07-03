import domain.Area;
import domain.Circle;
import domain.Square;

import java.util.Arrays;
import java.util.List;

public class FunctionalInterfaceTest {

    private static <E> void printArea(List<E> l, Area<E> a) {
        l.forEach(x -> System.out.println(a.compute(x)));
    }

    public static void main(String[] args) {
        // anonymous class
        /* Area<Circle> circleArea = new domain.Area<domain.Circle>() {
            @Override
            public double compute(domain.Circle circle) {
                return Math.PI * circle.getRadius() * circle.getRadius();
            }
        }; */
        // lambda
        Area<Circle> circleArea = circle -> Math.PI * Math.pow(circle.getRadius(), 2);

        Circle c1 = new Circle(1);
        Circle c2 = new Circle(2);
        // System.out.println(circleArea.compute(c1));
        List<Circle> circles = Arrays.asList(c1, c2);
        printArea(circles, circleArea);


        Area<Square> squareArea = square -> Math.pow(square.getSide(), 2);

        Square s1 = new Square(3);
        Square s2 = new Square(4);
        // System.out.println(squareArea.compute(s1));
        List<Square> squares = Arrays.asList(s1, s2);
        printArea(squares, squareArea);
    }
}
