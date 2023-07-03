package entity;

public class Record {
    private final String date;
    private final Student student;
    private final String bookId;

    public Record(Operation operation) {
        this.date = operation.getTime();
        this.student = operation.getStudent();
        this.bookId = operation.getBookId();
    }

    @Override
    public int hashCode() {
        return date.hashCode() * student.hashCode() * bookId.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Record) {
            return date.equals(((Record) o).date) && student.equals(((Record) o).student) &&
                    bookId.equals(((Record) o).bookId);
        } else {
            return false;
        }
    }

    public String getDate() {
        return date;
    }

    public Student getStudent() {
        return student;
    }

    public String getBookId() {
        return bookId;
    }
}
