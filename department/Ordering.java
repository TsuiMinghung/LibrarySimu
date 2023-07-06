package department;

import entity.Book;
import global.Library;
import entity.Operation;
import entity.Record;
import entity.Student;
import global.Error;
import global.Runner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class Ordering { //interBorrow and order
    private final String name;
    private final LinkedList<Operation> dailyOperations;
    private final ArrayList<Record> intraRecords;//intra order or buy new books
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
        //prerequisite: shelf doesn't have the book
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
        interRecords.clear();
    }

    private void orderB(Operation operation) {
        Student student = operation.getStudent();
        String bookId = operation.getBookId();
        if (!interRecords.containsKey(student)) {
            interRecords.put(student,new InterList());
        }

        if (student.hasB()) {
            return;
        } else {
            //try inter
            if (interRecords.get(student).hasB()) {
                //to guarantee the book from inter borrow must be accepted
                return;
            } else {
                //consider inter borrow first
                if (Library.canInterBorrow(operation.getStudent(),bookId,library)) {
                    interRecords.get(student).setB();
                } else {
                    //intra
                    if (!student.hasReserved(bookId)
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
            }
        }
    }

    private void orderC(Operation operation) {
        Student student = operation.getStudent();
        String bookId = operation.getBookId();
        if (!interRecords.containsKey(student)) {
            interRecords.put(student,new InterList());
        }

        if (student.hasC(bookId)) {
            return;
        } else {
            if (interRecords.get(student).hasC(bookId)) {
                return;
            } else {
                if (Library.canInterBorrow(operation.getStudent(),bookId,library)) {
                    interRecords.get(student).borrowC(bookId);
                } else {
                    if (!student.hasReserved(bookId) &&
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
            }
        }
    }

    private void printOrder(Operation operation) {
        String[] output = new String[]{operation.squaredTime(),operation.
                getStudent().toString(),"ordered", library.schoolName() +
                "-" + operation.getBookId(),"from",name};
        System.out.println(String.join(" ",output));
        output = new String[]{operation.squaredTime(),name,"recorded",operation.
                getStudent().toString() + "'s order of"
                , library.schoolName() + "-" + operation.getBookId()};
        System.out.println(String.join(" ",output));
        output = new String[]{"(Sequence)",operation.squaredTime()
                , "Ordering sends a message to Library"};
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
                        fetchBook(r.getStudent(),book);
                        r.getStudent().ownB(book);
                        break;
                    case 'C':
                        fetchBook(r.getStudent(),book);
                        r.getStudent().ownC(book);
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
