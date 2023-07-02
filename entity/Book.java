package entity;

import global.Library;

public class Book {
    private final BookTemplate bookTemplate;
    private final int id;
    private BookState state;

    public Book(BookTemplate template,int id) {
        this.bookTemplate = template;
        this.id = id;
        this.state = BookState.returned;
    }

    @Override
    public int hashCode() {
        return bookTemplate.hashCode() * id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Book) {
            return bookTemplate.equals(((Book) o).bookTemplate) &&
                    id == ((Book) o).id;
        } else {
            return false;
        }
    }

    public void setState(BookState newState) {
        this.state = newState;
    }

    public String getBookId() {
        return bookTemplate.getBookId();
    }

    public boolean isSmeared() {
        return state.equals(BookState.smeared);
    }

    @Override
    public String toString() {
        return bookTemplate.schoolName() + "-" + getBookId();
    }

    public String schoolName() {
        return bookTemplate.schoolName();
    }

    public Library getLibrary() {
        return bookTemplate.getLibrary();
    }
}
