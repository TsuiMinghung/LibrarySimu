package department;

import entity.Book;

import java.util.HashMap;
import java.util.LinkedList;

public class Arranging {

    private static Arranging INSTANCE = null;

    public static Arranging getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Arranging();
        }
        return INSTANCE;
    }

    private final String name;

    private Arranging() {
        this.name = "arranging librarian";
    }

    public void arrange() {
        HashMap<String, LinkedList<Book>> books = new HashMap<>();
        for (Book book : BorrowAndReturn.getInstance().collect()) {
            if (books.containsKey(book.getBookId())) {
                books.get(book.getBookId()).add(book);
            } else {
                books.put(book.getBookId(),new LinkedList<Book>() {
                    {
                        offer(book);
                    }
                });
            }
        }
        for (Book book : Machine.getInstance().collect()) {
            if (books.containsKey(book.getBookId())) {
                books.get(book.getBookId()).add(book);
            } else {
                books.put(book.getBookId(),new LinkedList<Book>() {
                    {
                        offer(book);
                    }
                });
            }
        }
        for (Book book : Logistics.getInstance().collect()) {
            if (books.containsKey(book.getBookId())) {
                books.get(book.getBookId()).add(book);
            } else {
                books.put(book.getBookId(),new LinkedList<Book>() {
                    {
                        offer(book);
                    }
                });
            }
        }
        Ordering.getInstance().satisfy(books);
        Library.getInstance().stack(books);
    }
}
