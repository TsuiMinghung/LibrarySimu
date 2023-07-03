package department;

import entity.Book;
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
        if (book.schoolName().equals(library.schoolName())) {
            intraList.add(book);
        } else {
            library.getPurchasing().returnToTransport(
                    new Transport(library,book.getLibrary(),book)
            );
        }
        String[] output = new String[]{"[" + time + "]",book.toString()
                , "got repaired by",name,"in", library.schoolName()};
        System.out.println(String.join(" ",output));
    }

    public Collection<Book> collect() {
        Collection<Book> result = new ArrayList<>(intraList);
        intraList.clear();
        return result;
    }
}
