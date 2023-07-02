package department;

import entity.Book;
import entity.BookTemplate;
import entity.Student;
import entity.Transport;
import global.Library;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Shelf {

    private final HashMap<String, BookTemplate> books;
    private final Library library;

    public Shelf(Library library) {
        books = new HashMap<>();
        this.library = library;
    }

    public boolean isAvailable(String bookId) {
        if (books.containsKey(bookId)) {
            return books.get(bookId).isAvailable();
        } else {
            return false;
        }
    }

    public boolean interAvailable(Student student, String bookId, Library call) {
        if (books.containsKey(bookId) && books.get(bookId).isRentable()) {
            if (books.get(bookId).isAvailable()) {
                library.getPurchasing().lendToTransport(
                        new Transport(library,call,fetchBook(bookId),student));
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void addBookTemplate(String s) {
        BookTemplate template = new BookTemplate(s,library);
        books.put(template.getBookId(),template);
    }

    public Book fetchBook(String bookId) {
        return books.get(bookId).fetch();
    }

    public void stack(HashMap<String, LinkedList<Book>> stackedBooks) {
        for (Map.Entry<String,LinkedList<Book>> id2books : stackedBooks.entrySet()) {
            books.get(id2books.getKey()).addAll(id2books.getValue());
        }
    }

    public boolean hasBookTemplate(String bookId) {
        return books.containsKey(bookId);
    }
}
