package global;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Runner {
    private static Calendar previous;
    private static Calendar current;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    private static Runner INSTANCE = null;

    public static String currentTime() {
        return SDF.format(current.getTime());
    }

    public static Runner getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Runner();
        }
        return INSTANCE;
    }
    
    private Runner() {
        //init calendar
        try {
            Date currentDay = SDF.parse("2023-01-01");
            current = Calendar.getInstance();
            current.setTime(currentDay);
            Date previousDay = SDF.parse("2023-01-01");
            previous = Calendar.getInstance();
            previous.setTime(previousDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void simulate() {
        Scanner scanner = new Scanner(System.in);
        initSchool(scanner);
        dealOperations(scanner);
    }

    private void initSchool(Scanner scanner) {
        int n = Integer.parseInt(scanner.nextLine());
        for (int i = 0;i < n;++i) {
            new Library(scanner);
        }
    }

    private void dealOperations(Scanner scanner) {
        arrange();
        int n = Integer.parseInt(scanner.nextLine());
        for (int i = 0;i < n;++i) {
            Operation operation = new Operation(scanner.nextLine());
            dealOperation(operation);
        }
        Library.dealOrder();
        Library.dealTransport();
    }

    //update global date
    private void dealOperation(Operation operation) {
        update(operation.getTime());
        operation.execute();
    }

    private void update(String time) {
        try {
            Date opDate = SDF.parse(time);
            Calendar opCal = Calendar.getInstance();
            opCal.setTime(opDate);
            if (current.compareTo(opCal) < 0) {
                callItADay();
                while (current.compareTo(opCal) < 0) {
                    callItADay();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //maintain previous
    private void callItADay() {
        //night
        Library.dealOrder();
        Library.dealTransport();

        current.add(Calendar.DAY_OF_YEAR,1);

        //morning

        Library.dealReceive();
        Library.dealInterBorrow();

        if (current.get(Calendar.DAY_OF_YEAR) - previous.get(Calendar.DAY_OF_YEAR) >= 3) {
            Library.dealPurchase();
            arrange();
            previous.add(Calendar.DAY_OF_YEAR,3);
            Library.dealAllocate();
        }
    }

    private void arrange() {
        System.out.println("[" + SDF.format(current.getTime()) + "] arranging librarian arranged " +
                "all the books");
    }

}
