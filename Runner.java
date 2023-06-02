import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Runner {
    private static String previousDate = "2023-01-01";
    private static String currentDate = "2023-01-01";

    public static String updateTime() {
        return previousDate;
    }

    public static void feed(Operation operation) {
        tryRearrange(operation);
        Student.tryAdd(operation.getStudentId());
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

    private static void tryRearrange(Operation operation) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date current = sdf.parse(currentDate);
            Date updated = sdf.parse(operation.getTime());
            Date previous = sdf.parse(previousDate);
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(current);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(updated);
            Calendar cal3 = Calendar.getInstance();
            cal3.setTime(previous);
            while (cal1.compareTo(cal2) < 0) {
                cal1.add(Calendar.DAY_OF_YEAR,1);
                currentDate = sdf.format(cal1.getTime());
                if (cal1.get(Calendar.DAY_OF_YEAR) - cal3.get(Calendar.DAY_OF_YEAR) >= 3) {
                    cal3.add(Calendar.DAY_OF_YEAR,3);
                    previousDate = sdf.format(cal3.getTime());
                    Arranging.getInstance().arrange();
                }
            }
            previousDate = sdf.format(cal3.getTime());
            currentDate = sdf.format(cal1.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void dealBorrow(Operation operation) {
        if (Machine.getInstance().queryAvailable(operation)) {
            switch (operation.getCategory()) {
                case A:
                    //read and return
                    break;
                case B:
                    BorrowAndReturn.getInstance().borrow(operation);
                    break;
                case C:
                    Machine.getInstance().borrow(operation);
                    break;
                default:
                    Error.occur("Unrecognized book type");
            }
        } else {
            Ordering.getInstance().order(operation);
        }
    }

    public static void dealSmear(Operation operation) {
        operation.getStudent().smearBook(operation);
    }

    public static void dealLost(Operation operation) {
        operation.getStudent().lostBook(operation);
        BorrowAndReturn.getInstance().dealLost(operation);
    }

    public static void dealReturn(Operation operation) {
        switch (operation.getCategory()) {
            case B:
                BorrowAndReturn.getInstance().dealReturn(operation);
                break;
            case C:
                Machine.getInstance().dealReturn(operation);
                break;
            default:
                Error.occur("Unrecognized book type");
        }
    }
}
