package entity;

import global.Library;

public class Transport {
    private final Library from;
    private final Library to;
    private final Book book;
    private final Student student;

    public Transport(Library from,Library to,Book book) {
        this.from = from;
        this.to = to;
        this.book = book;
        this.student = null;
    }

    public Transport(Library from,Library to,Book book,Student student) {
        this.from = from;
        this.to = to;
        this.book = book;
        this.student = student;
    }

    @Override
    public int hashCode() {
        return from.hashCode() * to.hashCode() * book.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Transport) {
            return book.equals(((Transport) o).book) && from.equals(((Transport) o).from) &&
                    to.equals(((Transport) o).to);
        } else {
            return false;
        }
    }

    public Library getTo() {
        return to;
    }

    public Book getBook() {
        return book;
    }

    public boolean isInterBorrow() {
        return student != null;
    }

    public Student getStudent() {
        return student;
    }
}
