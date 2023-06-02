import java.util.Collection;
import java.util.Stack;

public class BookTemplate {

    private final String sequence;
    private final Category category;
    private final Stack<Book> copys;

    public BookTemplate(String s, int count) {
        this.category = Category.parse(s.split("-")[0]);
        this.sequence = s.split("-")[1];
        this.copys = new Stack<>();
        for (int i = 0;i < count;++i) {
            copys.push(new Book(this,i));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof BookTemplate) {
            return sequence.equals(((BookTemplate) o).sequence)
                    && category.equals(((BookTemplate) o).category);
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
}
