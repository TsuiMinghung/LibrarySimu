package entity;

import global.Error;
import global.Library;
import global.Runner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class Student {

    private final String id;
    private final Library library;
    private final HashMap<String,Book> bookCs;
    private Book bookB;
    private final HashSet<String> reservedBooks;
    private final HashMap<String, LinkedList<Record>> historyRecords;

    public Student(String id, Library library) {
        this.id = id;
        this.bookCs = new HashMap<>();
        this.bookB = null;
        this.reservedBooks = new HashSet<>();
        this.historyRecords = new HashMap<>();
        this.library = library;
    }

    public void smearBook(Operation operation) {
        switch (operation.getCategory()) {
            case B:
                bookB.setSmeared();
                break;
            case C:
                bookCs.get(operation.getBookId()).setSmeared();
                break;
            default:
                Error.occur("smear unsupported category");
        }
    }

    public void lostBook(Operation operation) {
        switch (operation.getCategory()) {
            case B:
                bookB = null;
                break;
            case C:
                bookCs.remove(operation.getBookId());
                break;
            default:
                Error.occur("lost unsupported category");
        }
    }

    public boolean hasB() {
        return bookB != null;
    }

    public boolean hasC(String bookId) {
        return bookCs.containsKey(bookId);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Student) {
            return id.equals(((Student) o).id);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public boolean canHold(String bookId) {
        switch (bookId.charAt(0)) {
            case 'B':
                return !hasB();
            case 'C':
                return !hasC(bookId);
            default:
                Error.occur("unsupported category");
                return false;
        }
    }

    public void own(Book book) {
        switch (book.getBookId().charAt(0)) {
            case 'B':
                ownB(book);
                break;
            case 'C':
                ownC(book);
                break;
            default:
                Error.occur("unsupported category");
        }
    }

    public void ownB(Book book) {
        bookB = book;
        book.setState(BookState.onStudent);
        book.setOwnedTime(Runner.currentTime());
    }

    public void ownC(Book book) {
        book.setState(BookState.onStudent);
        bookCs.put(book.getBookId(),book);
        book.setOwnedTime(Runner.currentTime());
    }

    public boolean hasReserved(String bookId) {
        return reservedBooks.contains(bookId);
    }

    public boolean obeyRegisterRule(String date) {
        return !historyRecords.containsKey(date) || historyRecords.get(date).size() < 3;
    }

    public void reserveBook(String bookId) {
        reservedBooks.add(bookId);
    }

    public void addRegister(Record record) {
        if (!historyRecords.containsKey(record.getDate())) {
            historyRecords.put(record.getDate(), new LinkedList<>());
        }
        historyRecords.get(record.getDate()).offer(record);
    }

    public Book returnB() {
        Book result = bookB;
        bookB = null;
        return result;
    }

    public Book returnC(String bookId) {
        return bookCs.remove(bookId);
    }

    @Override
    public String toString() {
        return library.schoolName() + "-" + id;
    }

    public String schoolName() {
        return library.schoolName();
    }
}
