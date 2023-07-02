package global;

public class Error {
    public static void occur(String s) {
        System.err.println(s);
        System.exit(1);
    }
}
