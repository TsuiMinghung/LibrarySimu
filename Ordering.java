
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class Ordering {

    private static Ordering INSTANCE = null;

    public static Ordering getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Ordering();
        }
        return INSTANCE;
    }

    private final String name;
    private final LinkedList<Record> records;

    private Ordering() {
        this.name = "ordering librarian";
        records = new LinkedList<>();
    }

    public void order(Operation operation) {
        switch (operation.getCategory()) {
            case B:
                orderB(operation);
                break;
            case C:
                orderC(operation);
                break;
            default:
                Error.occur("unrecognized book category");
        }
    }

    private void orderB(Operation operation) {
        Student student = operation.getStudent();
        if (!student.hasB() && !student.hasReserved(operation.getBookId())
                && student.obeyRegisterRule(operation.getTime())) {
            student.reserveBook(operation.getBookId());
            Record record = new Record(operation);
            student.addRegister(record);
            records.offer(record);
            executeOrder(operation);
        }
    }

    private void orderC(Operation operation) {
        Student student = operation.getStudent();
        String bookId = operation.getBookId();
        if (!student.hasC(bookId) && !student.hasReserved(bookId) &&
            student.obeyRegisterRule(operation.getTime())) {
            student.reserveBook(bookId);
            Record  record = new Record(operation);
            student.addRegister(record);
            records.offer(record);
            executeOrder(operation);
        }
    }

    private void executeOrder(Operation operation) {
        String[] output = new String[]{operation.squaredTime(),operation.
                getStudentId(),"ordered", operation.getBookId(),"from",name};
        System.out.println(String.join(" ",output));
    }

    public void satisfy(HashMap<String, LinkedList<Book>> books) {
        Iterator<Record> iterator = records.iterator();
        while (iterator.hasNext()) {
            Record r = iterator.next();
            if (books.containsKey(r.getBookId())) {
                Book book = books.get(r.getBookId()).poll();
                switch (r.getBookId().charAt(0)) {
                    case 'B':
                        r.getStudent().ownB(book);
                        fetchBook(r.getStudent(),book);
                        break;
                    case 'C':
                        r.getStudent().ownC(book);
                        fetchBook(r.getStudent(),book);
                        break;
                    default:
                }
                if (books.get(r.getBookId()).isEmpty()) {
                    books.remove(r.getBookId());
                }
                iterator.remove();
            }
        }
    }

    private void fetchBook(Student student,Book book) {
        String[] output = new String[]{"[" + Runner.updateTime() + "]",student.
                getId(),"borrowed", book.getId(),"from",name};
        System.out.println(String.join(" ",output));
    }
}
