import java.util.ArrayList;
import java.util.Collection;

public class Machine {

    private static Machine INSTANCE = null;

    public static Machine getInstance() {
        if (INSTANCE == null) {
            INSTANCE  = new Machine();
        }
        return INSTANCE;
    }

    private final ArrayList<Book> list;
    private final String name;

    private Machine() {
        list = new ArrayList<>();
        this.name = "self-service machine";
    }

    public boolean queryAvailable(Operation op) {
        String[] output = new String[]{op.squaredTime(),
                op.getStudentId(),"queried",op.getBookId(),"from",name};
        System.out.println(String.join(" ",output));
        return Library.getInstance().isAvailable(op.getBookId());
    }

    public void borrow(Operation operation) {
        Book book = Library.getInstance().fetchBook(operation.getBookId());
        if (meetLimit(operation)) {
            operation.getStudent().ownC(book);
            finishBorrow(operation);
        } else {
            list.add(book);
        }
    }

    public boolean meetLimit(Operation operation) {
        return !operation.getStudent().hasC(operation.getBookId());
    }

    public void dealReturn(Operation operation) {
        Student  student = operation.getStudent();
        Book book = student.returnC(operation.getBookId());
        if (book.isSmeared()) {
            BorrowAndReturn.getInstance().dealFine(operation);
            finishReturn(operation);
            Logistics.getInstance().dealRepair(book,operation);
        } else {
            list.add(book);
            finishReturn(operation);
        }
    }

    private void finishReturn(Operation operation) {
        String[] output = new String[]{operation.squaredTime(),
                operation.getStudentId(),"returned",operation.getBookId(),"to",name};
        System.out.println(String.join(" ",output));
    }

    private void finishBorrow(Operation operation) {
        String[] output = new String[]{operation.squaredTime(),operation.
                getStudentId(),"borrowed",operation.getBookId(),"from",name};
        System.out.println(String.join(" ",output));
    }

    public Collection<Book> collect() {
        Collection<Book> result = new ArrayList<>(list);
        list.clear();
        return result;
    }
}
