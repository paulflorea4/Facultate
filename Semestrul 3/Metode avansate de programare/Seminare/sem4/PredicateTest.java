import domain.Circle;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class PredicateTest {

    private static <E> void printList(List<E> l, Predicate<E> p) {
        l.forEach(x -> {
            if (p.test(x)) {
                System.out.println(x);
            }
        });
    }

    public static void main(String[] args) {
        Circle c1 = new Circle(8);
        Circle c2 = new Circle(9);
        Circle c3 = new Circle(20);
        List<Circle> circles = Arrays.asList(c1, c2, c3);

        Predicate<Circle> smallCirclePredicate = c -> c.getRadius() < 10;
        printList(circles, smallCirclePredicate);

        System.out.println("---");
        Predicate<Circle> largeCirclePredicate = smallCirclePredicate.negate();
        printList(circles, largeCirclePredicate);

        System.out.println("---");
        Predicate<Circle> smallCircleWithEvenRadiusPredicate =
                smallCirclePredicate.and(c -> c.getRadius() % 2 == 0);
        printList(circles, smallCircleWithEvenRadiusPredicate);
    }
}
