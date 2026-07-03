import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ComparatorTest {

    public static void main(String[] args) {
        // lambda
        Comparator<Integer> descComparator = (a, b) -> Integer.compare(b, a);
        // method reference
        Comparator<Integer> ascComparator = Integer::compare;

        List<Integer> list = Arrays.asList(1, 4, 2, 5, 3);
        list.sort(ascComparator);
        System.out.println(list);
        list.sort(descComparator);
        System.out.println(list);
        list.sort(ascComparator.reversed());
        System.out.println(list);
    }
}
