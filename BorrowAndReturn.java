import java.util.ArrayList;
import java.util.Collection;

public class BorrowAndReturn {

    private static BorrowAndReturn INSTANCE = null;

    public static BorrowAndReturn getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BorrowAndReturn();
        }
        return INSTANCE;
    }

    private final ArrayList<Book> list;
    private final String name;

    private BorrowAndReturn() {
        this.list = new ArrayList<>();
        this.name = "borrowing and returning librarian";
    }

    public void borrow(Operation operation) {
        Book book = Library.getInstance().fetchBook(operation.getBookId());
        if (meetLimit(operation)) {
            operation.getStudent().ownB(book);
            finishBorrow(operation);
        } else {
            list.add(book);
        }
    }

    public boolean meetLimit(Operation operation) {
        return !operation.getStudent().hasB();
    }

    public void dealLost(Operation operation) {
        dealFine(operation);
    }

    public void dealReturn(Operation operation) {
        Student student = operation.getStudent();
        Book book = student.returnB();
        if (book.isSmeared()) {
            dealFine(operation);
            finishReturn(operation);
            Logistics.getInstance().dealRepair(book,operation);
        } else {
            list.add(book);
            finishReturn(operation);
        }
    }

    public void dealFine(Operation operation) {
        String[] output = new String[]{operation.squaredTime(),
                operation.getStudentId(),"got punished by",name};
        System.out.println(String.join(" ",output));
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
