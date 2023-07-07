package department;

import entity.Book;
import entity.BookState;
import global.Library;

import java.util.HashMap;
import java.util.LinkedList;

public class Arranging {

    private final String name;
    private final Library library;

    public Arranging(Library library) {
        this.name = "arranging librarian";
        this.library = library;
    }

    public void dealAllocate() {
        HashMap<String, LinkedList<Book>> books = new HashMap<>();
        for (Book book : library.getBorrowAndReturn().collect()) {
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
        for (Book book : library.getMachine().collect()) {
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
        for (Book book : library.getLogistics().collect()) {
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
        for (Book book : library.getPurchasing().collect()) {
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
        library.getOrdering().getOrderedBook(books);
        books.forEach((k,v) -> v.forEach(book -> book.setState(BookState.shelf)));
        library.getShelf().stack(books);
    }
}
