import java.util.ArrayList;
import java.util.Collection;

public class Logistics {
    private static Logistics INSTANCE = null;

    public static Logistics getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Logistics();
        }
        return INSTANCE;
    }

    private final ArrayList<Book> list;
    private final String name;

    private Logistics() {
        list = new ArrayList<>();
        name = "logistics division";
    }

    public void dealRepair(Book book,Operation operation) {
        list.add(book);
        String[] output = new String[]{operation.squaredTime(),operation.
                getBookId(), "got repaired by",name};
        System.out.println(String.join(" ",output));
    }

    public Collection<Book> collect() {
        Collection<Book> result = new ArrayList<>(list);
        list.clear();
        return result;
    }
}
