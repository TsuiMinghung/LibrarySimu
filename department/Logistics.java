package department;

import entity.Book;
import entity.BookState;
import entity.Transport;
import global.Library;

import java.util.ArrayList;
import java.util.Collection;

public class Logistics {

    private final ArrayList<Book> intraList;
    private final String name;
    private final Library library;

    public Logistics(Library library) {
        intraList = new ArrayList<>();
        name = "logistics division";
        this.library = library;
    }

    public void dealRepair(Book book, String time) {
        printRepair(book,time);

        book.setState(BookState.logistics);

        if (book.belongTo(library.schoolName())) {
            intraList.add(book);
        } else {
            book.setState(BookState.purchasing);
            library.getPurchasing().returnToTransport(
                    new Transport(library,book.getLibrary(),book)
            );
        }
    }

    private void printRepair(Book book,String time) {
        String[] output = new String[]{"[" + time + "]",book.toString()
                , "got repaired by",name,"in", library.schoolName()};
        System.out.println(String.join(" ",output));
        System.out.println("(State) [" + time + "] " +
                book.getBookId() + " transfers from " + book.stateString() + " to logistics");
    }

    public Collection<Book> collect() {
        Collection<Book> result = new ArrayList<>(intraList);
        intraList.clear();
        result.forEach(book -> book.setState(BookState.arranging));
        return result;
    }
}
