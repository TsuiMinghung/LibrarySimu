package entity;

import global.Library;

import java.util.Collection;
import java.util.Stack;

public class BookTemplate {

    private final String sequence;
    private final Category category;
    private final Stack<Book> copys;
    private final Library library;
    private final boolean rentable;

    public BookTemplate(String s, Library library) {
        String[] divisions = s.split(" ");
        this.category = Category.parse(divisions[0].split("-")[0]);
        this.sequence = divisions[0].split("-")[1];
        this.copys = new Stack<>();
        for (int i = 0;i < Integer.parseInt(divisions[1]);++i) {
            copys.push(new Book(this,i));
        }
        this.rentable = divisions[2].equals("Y");
        this.library = library;
    }

    public BookTemplate(String bookId,Library library,int num) {
        this.category = Category.parse(bookId.substring(0,1));
        this.sequence = bookId.substring(2);
        this.copys = new Stack<>();
        for (int i = 0;i < num;++i) {
            copys.push(new Book(this,i));
        }
        this.library = library;
        this.rentable = true;
    }

    public BookTemplate(Book book) {
        this.category = Category.parse(book.getBookId().substring(0,1));
        this.sequence = book.getBookId().substring(2);
        this.copys = new Stack<>();
        this.library = book.getLibrary();
        this.rentable = true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof BookTemplate) {
            return sequence.equals(((BookTemplate) o).sequence)
                    && category.equals(((BookTemplate) o).category)
                    && rentable == ((BookTemplate) o).rentable;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return category.hashCode() * sequence.hashCode();
    }

    public boolean isAvailable() {
        return !copys.isEmpty();
    }

    public Book fetch() {
        return copys.pop();
    }

    public String getBookId() {
        return category.toString() + "-" + sequence;
    }

    public void addAll(Collection<Book> books) {
        copys.addAll(books);
    }

    public String schoolName() {
        return library.schoolName();
    }

    public boolean isRentable() {
        return rentable;
    }

    public Library getLibrary() {
        return library;
    }
}
