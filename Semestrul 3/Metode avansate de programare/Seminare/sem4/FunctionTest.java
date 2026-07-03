import java.util.function.Function;

public class FunctionTest {

    public static void main(String[] args) {
        // lambda
        // Function<String, Integer> stringToInteger = s -> Integer.valueOf(s);
        // method reference
        Function<String, Integer> stringToInteger = Integer::valueOf;
        System.out.println(stringToInteger.apply("10"));

        Function<Integer, Integer> pow = i -> i * i;
        System.out.println(stringToInteger.andThen(pow).apply("10"));
    }
}
