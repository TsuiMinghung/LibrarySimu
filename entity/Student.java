package entity;

import simulate.Error;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class Student {

    private static final HashMap<String,Student> STUDENTS = new HashMap<>();

    public static Student getStudent(String id) {
        return STUDENTS.get(id);
    }

    public static void tryAdd(String id) {
        if (!STUDENTS.containsKey(id)) {
            STUDENTS.put(id,new Student(id));
        }
    }

    private final String id;
    private final HashMap<String,Book> bookCs;
    private Book bookB;
    private final HashSet<String> reservedBooks;
    private final HashMap<String, LinkedList<Record>> historyRecords;

    public Student(String id) {
        this.id = id;
        this.bookCs = new HashMap<>();
        this.bookB = null;
        this.reservedBooks = new HashSet<>();
        this.historyRecords = new HashMap<>();
    }

    public void smearBook(Operation operation) {
        switch (operation.getCategory()) {
            case B:
                bookB.setState(BookState.smeared);
                break;
            case C:
                bookCs.get(operation.getBookId()).setState(BookState.smeared);
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

    public void ownB(Book book) {
        bookB = book;
    }

    public void ownC(Book book) {
        bookCs.put(book.getBookId(),book);
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

    public String getId() {
        return id;
    }
}
