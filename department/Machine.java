package department;

import entity.Book;
import entity.Transport;
import global.Library;
import entity.Operation;
import entity.Student;

import java.util.ArrayList;
import java.util.Collection;

public class Machine {

    private final ArrayList<Book> intraList;
    private final String name;
    private final Library library;

    public Machine(Library library) {
        this.intraList = new ArrayList<>();
        this.name = "self-service machine";
        this.library = library;
    }

    public boolean queryAvailable(Operation operation) {
        String[] output = new String[]{operation.squaredTime(),operation.getStudent().toString()
                ,"queried",operation.getBookId(),"from",name};
        System.out.println(String.join(" ",output));
        output = new String[] {operation.squaredTime(),name
                , "provided information of",operation.getBookId()};
        System.out.println(String.join(" ",output));
        return library.getShelf().isAvailable(operation.getBookId());
    }

    //need to check restrict
    public void borrow(Operation operation) {
        Book book = library.getShelf().fetchBook(operation.getBookId());
        if (meetLimit(operation)) {
            operation.getStudent().ownC(book);
            finishBorrow(operation,book);
        } else {
            String[] output = new String[]{operation.squaredTime(),name
                    ,"refused lending",book.toString(),"to",operation.getStudent().toString()};
            System.out.println(String.join(" ",output));
            intraList.add(book);
        }
    }

    public boolean meetLimit(Operation operation) {
        return !operation.getStudent().hasC(operation.getBookId());
    }

    private void finishBorrow(Operation operation,Book book) {
        String[] output = new String[]{operation.squaredTime(),name,"lent"
                ,book.toString(),"to",operation.getStudent().toString()};
        System.out.println(String.join(" ",output));
        output = new String[]{operation.squaredTime(),operation.
                getStudent().toString(),"borrowed",book.toString(),"from",name};
        System.out.println(String.join(" ",output));
    }

    public void dealReturn(Operation operation) {
        Student student = operation.getStudent();
        Book book = student.returnC(operation.getBookId());
        if (book.isSmeared()) {
            library.getBorrowAndReturn().dealFine(operation);
            finishReturn(operation,book);
            library.getLogistics().dealRepair(book,operation.getTime());
        } else {
            if (book.schoolName().equals(student.schoolName())) {
                intraList.add(book);
            } else {
                library.getPurchasing().returnToTransport(
                        new Transport(library,book.getLibrary(),book)
                );
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
