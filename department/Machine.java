package department;

import entity.Book;
import entity.BookState;
import entity.Operation;
import entity.Student;
import entity.Transport;
import global.Library;

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
        output = new String[] {"(Sequence)",operation.squaredTime()
                ,"Library sends a message to Machine"};
        System.out.println(String.join(" ",output));
        output = new String[] {operation.squaredTime(),name
                , "provided information of",operation.getBookId()};
        System.out.println(String.join(" ",output));
        output = new String[] {"(Sequence)",operation.squaredTime()
                ,"Machine sends a message to Library"};
        System.out.println(String.join(" ",output));
        return library.getShelf().isAvailable(operation.getBookId());
    }

    //need to check restrict
    public void borrow(Operation operation) {
        Book book = library.getShelf().fetchBook(operation.getBookId());
        book.setState(BookState.machine);

        if (meetLimit(operation)) {

            String[] output = new String[]{operation.squaredTime(),name,"lent"
                    ,book.toString(),"to",operation.getStudent().toString()};
            System.out.println(String.join(" ",output));
            System.out.println("(State) " + operation.squaredTime() + " " +
                    book.getBookId() + " transfers from machine to onStudent");

            book.setState(BookState.onStudent);
            operation.getStudent().ownC(book);
            output = new String[]{"(Sequence)",operation.squaredTime()
                    , "Machine sends a message to Library"};
            System.out.println(String.join(" ",output));

            output = new String[]{operation.squaredTime(),operation.
                    getStudent().toString(),"borrowed",book.toString(),"from",name};
            System.out.println(String.join(" ",output));
        } else {
            String[] output = new String[]{operation.squaredTime(),name
                    ,"refused lending",book.toString(),"to",operation.getStudent().toString()};
            System.out.println(String.join(" ",output));
            System.out.println("(State) " + operation.squaredTime() + " " +
                    book.getBookId() + " transfers from shelf to borrowAndReturn");

            output = new String[]{"(Sequence)",operation.squaredTime()
                    , "Machine sends a message to Library"};
            System.out.println(String.join(" ",output));
            intraList.add(book);
        }
    }

    public boolean meetLimit(Operation operation) {
        return !operation.getStudent().hasC(operation.getBookId());
    }

    public void dealReturn(Operation operation) {
        Student student = operation.getStudent();
        Book book = student.returnC(operation.getBookId());

        if (book.overDue()) {
            library.getBorrowAndReturn().dealFine(operation);
        }

        if (book.isSmeared()) {
            library.getBorrowAndReturn().dealFine(operation);
            finishReturn(operation,book);
            library.getLogistics().dealRepair(book,operation.getTime());
        } else {
            finishReturn(operation,book);

            if (book.belongTo(student.schoolName())) {
                intraList.add(book);
            } else {
                book.setState(BookState.purchasing);
                library.getPurchasing().returnToTransport(
                        new Transport(library,book.getLibrary(),book)
                );
            }
        }

        book.resetOwned();
    }

    private void finishReturn(Operation operation,Book book) {
        String[] output = new String[]{operation.squaredTime(),
                operation.getStudent().toString(),"returned",book.toString(),"to",name};
        System.out.println(String.join(" ",output));
        output = new String[]{operation.squaredTime(),name,"collected",book.toString(),"from"
                ,operation.getStudent().toString()};
        System.out.println(String.join(" ",output));
        System.out.println("(State) " + operation.squaredTime() + " " +
                book.getBookId() + " transfers from onStudent to machine");
    }

    public Collection<Book> collect() {
        Collection<Book> result = new ArrayList<>(intraList);
        intraList.clear();
        result.forEach(book -> book.setState(BookState.arranging));
        return result;
    }
}
