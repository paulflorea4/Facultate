import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StreamTest {

    private static <E> List<E> filterGeneric(List<E> list, Predicate<E> predicate) {
        return list.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    private static <E> List<E> filterAndSortGeneric(List<E> list, Predicate<E> predicate, Comparator<E> comparator) {
        return list.stream()
                .filter(predicate)
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 4, 2, 5, 3);
        List<Integer> evenNumbers = filterGeneric(list, i -> i % 2 == 0);
        System.out.println(evenNumbers);
        List<Integer> evenSortedNumbers = filterAndSortGeneric(list, i -> i % 2 == 0, Integer::compare);
        System.out.println(evenSortedNumbers);
    }
}
