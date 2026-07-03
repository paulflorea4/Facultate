import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SupplierTest {

    public static void main(String[] args) {
        // lambda
        // Supplier<List> listSupplier = () -> new ArrayList<String>();
        // method reference
        Supplier<List> listSupplier = ArrayList<String>::new;
        List<String> list = listSupplier.get();
        System.out.println(list);

        // Supplier<LocalDate> dateSupplier = () -> LocalDate.now();
        Supplier<LocalDate> dateSupplier = LocalDate::now;
        LocalDate date = dateSupplier.get();
        System.out.println(date);
    }
}
