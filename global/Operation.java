package global;

import entity.Category;
import entity.Student;

public class Operation {
    private final String time;
    private final Library library;
    private final Student student;
    private final OpType type;
    private final String bookId;

    public Operation(String s) {
        String[] part = s.split(" ");
        assert (part.length == 4);
        this.time = part[0].substring(1,part[0].length() - 1);
        this.library = Library.getSchool(part[1].split("-")[0]);
        String studentId = part[1].split("-")[1];
        this.library.tryAdd(studentId);
        this.student = this.library.getStudent(studentId);
        this.type = OpType.parse(part[2]);
        this.bookId = part[3];
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Operation) {
            return time.equals(((Operation) o).time) &&
                    library.equals(((Operation) o).library) &&
                    student.equals(((Operation) o).student) &&
                    type.equals(((Operation) o).type) &&
                    bookId.equals(((Operation) o).bookId);
        } else {
            return false;
        }
    }

    public OpType getOpType() {
        return type;
    }

    public String getBookId() {
        return bookId;
    }

    public String squaredTime() {
        return "[" + time + "]";
    }

    public String getTime() {
        return time;
    }

    public Category getCategory() {
        return Category.parse(getBookId().substring(0,1));
    }

    public Student getStudent() {
        return student;
    }

    public Library getSchool() {
        return library;
    }

    public void execute() {
        library.execute(this);
    }

    public enum OpType {
        borrow,
        smear,
        lost,
        returnBook;

        public static OpType parse(String s) {
            switch (s) {
                case "borrowed":
                    return borrow;
                case "smeared":
                    return smear;
                case "lost":
                    return lost;
                case "returned":
                    return returnBook;
                default:
                    return null;
            }
        }
    }
}
