package global;

import department.Arranging;
import department.BorrowAndReturn;
import department.Shelf;
import department.Logistics;
import department.Machine;
import department.Ordering;
import department.Purchasing;
import entity.Student;

import java.util.HashMap;
import java.util.Scanner;

public class Library {

    private static final HashMap<String, Library> LIBRARIES = new HashMap<>();

    public static Library getSchool(String name) {
        return LIBRARIES.get(name);
    }

    public static void dealOrder() {
        for (Library library : LIBRARIES.values()) {
            library.ordering.dealOrder();
        }
    }

    public static void dealTransport() {
        for (Library library : LIBRARIES.values()) {
            library.purchasing.executeTransport();
        }
    }

    public static boolean interBorrow(Student student, String bookId, Library call) {
        for (Library library : LIBRARIES.values()) {
            if (!library.schoolName.equals(call.schoolName)) {
                if (library.shelf.interAvailable(student,bookId,call)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void dealReceive() {
        for (Library library : LIBRARIES.values()) {
            library.purchasing.printReceive();
        }
    }

    public static void dealInterBorrow() {
        for (Library library : LIBRARIES.values()) {
            library.purchasing.dealInterBorrow();
        }
    }

    public static void dealPurchase() {
        for (Library library : LIBRARIES.values()) {
            library.purchasing.dealPurchase();
        }
    }

    public static void dealAllocate() {
        for (Library library : LIBRARIES.values()) {
            library.arranging.dealAllocate();
        }
    }

    private final String schoolName;
    private final Arranging arranging;
    private final BorrowAndReturn borrowAndReturn;
    private final Shelf shelf;
    private final Logistics logistics;
    private final Machine machine;
    private final Ordering ordering;
    private final Purchasing purchasing;

    private final HashMap<String,Student> students;

    public Library(Scanner scanner) {
        this.students = new HashMap<>();
        shelf = new Shelf(this);
        logistics = new Logistics(this);
        borrowAndReturn = new BorrowAndReturn(this);
        machine = new Machine(this);
        ordering = new Ordering(this);
        purchasing = new Purchasing(this);
        arranging = new Arranging(this);
        String currLine = scanner.nextLine();
        String[] division = currLine.split(" ");
        assert division.length == 2;
        this.schoolName = division[0];
        int n = Integer.parseInt(division[1]);
        for (int i = 0;i < n;++i) {
            shelf.addBookTemplate(scanner.nextLine());
        }
        LIBRARIES.put(schoolName,this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Library) {
            return schoolName.equals(((Library) o).schoolName);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return schoolName.hashCode();
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void execute(Operation operation) {
        switch (operation.getOpType()) {
            case borrow:
                dealBorrow(operation);
                break;
            case smear:
                dealSmear(operation);
                break;
            case lost:
                dealLost(operation);
                break;
            case returnBook:
                dealReturn(operation);
                break;
            default:
                Error.occur("unrecognized operation type");
        }
    }

    private void dealBorrow(Operation operation) {
        if (machine.queryAvailable(operation)) {
            switch (operation.getCategory()) {
                case A:
                    //read and return
                    break;
                case B:
                    borrowAndReturn.borrow(operation);
                    break;
                case C:
                    machine.borrow(operation);
                    break;
                default:
                    Error.occur("Unrecognized book type");
            }
        } else {
            ordering.order(operation);
        }
    }

    private void dealSmear(Operation operation) {
        operation.getStudent().smearBook(operation);
    }

    private void dealLost(Operation operation) {
        operation.getStudent().lostBook(operation);
        borrowAndReturn.dealLost(operation);
    }

    private void dealReturn(Operation operation) {
        switch (operation.getCategory()) {
            case B:
                borrowAndReturn.dealReturn(operation);
                break;
            case C:
                machine.dealReturn(operation);
                break;
            default:
                Error.occur("Unrecognized book type");
        }
    }

    public BorrowAndReturn getBorrowAndReturn() {
        return borrowAndReturn;
    }

    public Shelf getShelf() {
        return shelf;
    }

    public Logistics getLogistics() {
        return logistics;
    }

    public Machine getMachine() {
        return machine;
    }

    public Ordering getOrdering() {
        return ordering;
    }

    public Purchasing getPurchasing() {
        return purchasing;
    }

    public Student getStudent(String id) {
        return students.get(id);
    }

    public void tryAdd(String id) {
        if (!students.containsKey(id)) {
            students.put(id,new Student(id, this));
        }
    }
}
