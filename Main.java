import department.Library;
import entity.Operation;
import simulate.Runner;

import java.util.Scanner;

public class Main {
    public static void main(String[] argv) {
        Scanner scanner = new Scanner(System.in);
        inputBook(scanner);
        simulate(scanner);
    }

    private static void inputBook(Scanner scanner) {
        int n = Integer.parseInt(scanner.nextLine());
        for (int i = 0;i < n;++i) {
            String[] stringDivision = scanner.nextLine().split(" ");
            assert stringDivision.length == 2;
            Library.getInstance().addBookTemplate(
                    stringDivision[0],Integer.parseInt(stringDivision[1]));
        }
    }

    private static void simulate(Scanner scanner) {
        int m = Integer.parseInt(scanner.nextLine());
        for (int i = 0;i < m;++i) {
            Operation currentOp = new Operation(scanner.nextLine());
            Runner.feed(currentOp);
        }
    }
}
