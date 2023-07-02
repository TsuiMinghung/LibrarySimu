package department;

import entity.Book;
import global.Library;
import global.Operation;
import entity.Record;
import entity.Student;
import global.Error;
import global.Runner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class Ordering {
    private final String name;
    private final LinkedList<Operation> dailyOperations;
    private final ArrayList<Record> intraRecords;
    private final HashMap<Student,InterList> interRecords;
    private final HashMap<String,Integer> purchaseList;
    private final Library library;

    public Ordering(Library library) {
        this.name = "ordering librarian";
        this.library = library;
        this.interRecords = new HashMap<>();
        this.intraRecords = new ArrayList<>();
        dailyOperations = new LinkedList<>();
        purchaseList = new HashMap<>();
    }

    public void order(Operation operation) {
        dailyOperations.offer(operation);
    }

    public void dealOrder() {
        for (Operation operation : dailyOperations) {
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
        dailyOperations.clear();
    }

    private void orderB(Operation operation) {
        Student student = operation.getStudent();
        String bookId = operation.getBookId();
        if (!interRecords.containsKey(student)) {
            interRecords.put(student,new InterList());
        }
        if (!student.hasB() && !interRecords.get(student).hasB()
                && Library.interBorrow(operation.getStudent(),bookId,library)) {
            interRecords.get(student).setB();
        }
        if (!student.hasB() && !student.hasReserved(bookId)
                && student.obeyRegisterRule(operation.getTime())) {
            student.reserveBook(bookId);
            Record record = new Record(operation);

            if (!library.getShelf().hasBookTemplate(bookId)) {
                purchaseList.put(bookId,purchaseList.getOrDefault(bookId,0) + 1);
            }
            intraRecords.add(record);
            printOrder(operation);
            student.addRegister(record);
        }
    }

    private void orderC(Operation operation) {
        Student student = operation.getStudent();
        String bookId = operation.getBookId();
        if (!interRecords.containsKey(student)) {
            interRecords.put(student,new InterList());
        }

        if (!student.hasC(bookId) && !interRecords.get(student).hasC(bookId)
                && Library.interBorrow(operation.getStudent(),bookId,library)) {
            interRecords.get(student).borrowC(bookId);
        }

        if (!student.hasC(bookId) && !student.hasReserved(bookId) &&
            student.obeyRegisterRule(operation.getTime())) {

            student.reserveBook(bookId);
            Record record = new Record(operation);
            if (!library.getShelf().hasBookTemplate(bookId)) {
                purchaseList.put(bookId,purchaseList.getOrDefault(bookId,0) + 1);
            }
            intraRecords.add(record);
            printOrder(operation);
            student.addRegister(record);
        }
    }

    private void printOrder(Operation operation) {
        String[] output = new String[]{operation.squaredTime(),operation.
                getStudent().toString(),"ordered", library.getSchoolName() +
                "-" + operation.getBookId(),"from",name};
        System.out.println(String.join(" ",output));
        output = new String[]{operation.squaredTime(),name,"recorded",operation.
                getStudent().toString() + "'s order of"
                , library.getSchoolName() + "-" + operation.getBookId()};
        System.out.println(String.join(" ",output));
    }

    public HashMap<String, Integer> getPurchaseList() {
        HashMap<String,Integer> result = new HashMap<>(purchaseList);
        purchaseList.clear();
        return result;
    }

    public void satisfy(HashMap<String, LinkedList<Book>> books) {
        Iterator<Record> iterator = intraRecords.iterator();
        while (iterator.hasNext()) {
            Record r = iterator.next();
            if (r.getStudent().canHold(r.getBookId()) && books.containsKey(r.getBookId())) {
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

    private void fetchBook(Student student, Book book) {
        String time = "[" + Runner.currentTime() + "]";
        String[] output = new String[]{time,name,"lent",book.toString(),"to",student.toString()};
        System.out.println(String.join(" ",output));
        output = new String[]{time,student.toString(),"borrowed",book.toString(),"from",name};
        System.out.println(String.join(" ",output));
    }

    private static class InterList {
        private boolean bookB;
        private final HashSet<String> bookCs;

        public InterList() {
            bookB = false;
            bookCs = new HashSet<>();
        }

        public void setB() {
            bookB = true;
        }

        public void borrowC(String bookId) {
            bookCs.add(bookId);
        }

        public boolean hasB() {
            return bookB;
        }

        public boolean hasC(String bookId) {
            return bookCs.contains(bookId);
        }
    }
}
