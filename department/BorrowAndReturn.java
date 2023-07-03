package department;

import entity.Book;
import entity.Transport;
import global.Library;
import global.Operation;
import entity.Student;

import java.util.ArrayList;
import java.util.Collection;

public class BorrowAndReturn {

    private final ArrayList<Book> intraList;
    private final String name;
    private final Library library;

    public BorrowAndReturn(Library library) {
        this.intraList = new ArrayList<>();
        this.name = "borrowing and returning librarian";
        this.library = library;
    }

    //need to check restrict
    public void borrow(Operation operation) {
        Book book = library.getShelf().fetchBook(operation.getBookId());
        if (meetLimit(operation)) {
            operation.getStudent().ownB(book);
            finishBorrow(operation,book);
        } else {
            String[] output = new String[]{operation.squaredTime(),name
                    ,"refused lending",book.toString(),"to",operation.getStudent().toString()};
            System.out.println(String.join(" ",output));
            intraList.add(book);
        }
    }

    public boolean meetLimit(Operation operation) {
        return !operation.getStudent().hasB();
    }

    private void finishBorrow(Operation operation,Book book) {
        String[] output = new String[]{operation.squaredTime(),name,"lent"
                ,book.toString(),"to",operation.getStudent().toString()};
        System.out.println(String.join(" ",output));
        output = new String[]{operation.squaredTime(),operation.
                getStudent().toString(),"borrowed",book.toString(),"from",name};
        System.out.println(String.join(" ",output));
    }

    public void dealLost(Operation operation) {
        dealFine(operation);
    }

    public void dealFine(Operation operation) {
        String[] output = new String[]{operation.squaredTime(),
                operation.getStudent().toString(),"got punished by",name};
        System.out.println(String.join(" ",output));
        output = new String[]{operation.squaredTime(),name,"received"
                ,operation.getStudent().toString() + "'s fine"};
        System.out.println(String.join(" ",output));
    }

    public void dealReturn(Operation operation) {
        Student student = operation.getStudent();
        Book book = student.returnB();
        if (book.isSmeared()) {
            dealFine(operation);
            finishReturn(operation,book);
            library.getLogistics().dealRepair(book,operation.getTime());
        } else {
            if (book.schoolName().equals(student.schoolName())) {
                intraList.add(book);
            } else {
                library.getPurchasing().returnToTransport(
                        new Transport(library,book.getLibrary(),book));
            }
            finishReturn(operation,book);
        }
    }

    private void finishReturn(Operation operation,Book book) {
        String[] output = new String[]{operation.squaredTime(),
                operation.getStudent().toString(),"returned",book.toString(),"to",name};
        System.out.println(String.join(" ",output));
        output = new String[]{operation.squaredTime(),name,"collected",book.toString(),"from"
                ,operation.getStudent().toString()};
        System.out.println(String.join(" ",output));
    }

    public Collection<Book> collect() {
        Collection<Book> result = new ArrayList<>(intraList);
        intraList.clear();
        return result;
    }
}
