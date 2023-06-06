package department;

import entity.Book;
import entity.BookTemplate;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Library {

    private static Library INSTANCE = null;

    public static Library getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Library();
        }
        return INSTANCE;
    }

    private final HashMap<String, BookTemplate> books;

    private Library() {
        books = new HashMap<>();
    }

    public boolean isAvailable(String bookId) {
        if (books.containsKey(bookId)) {
            return books.get(bookId).isAvailable();
        } else {
            return false;
        }
    }

    public void addBookTemplate(String s,int num) {
        books.put(s,new BookTemplate(s,num));
    }

    public Book fetchBook(String bookId) {
        return books.get(bookId).fetch();
    }

    public void stack(HashMap<String, LinkedList<Book>> stackedBooks) {
        for (Map.Entry<String,LinkedList<Book>> id2books : stackedBooks.entrySet()) {
            books.get(id2books.getKey()).addAll(id2books.getValue());
        }
    }
}
