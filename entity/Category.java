package entity;

import global.Error;

public enum Category {
    A,
    B,
    C;

    public static Category parse(String s) {
        switch (s) {
            case "A":
                return A;
            case "B":
                return B;
            case "C":
                return C;
            default:
                Error.occur("unrecognized entity.Book entity.Category");
                return null;
        }
    }

    public String toString() {
        switch (this) {
            case A:
                return "A";
            case B:
                return "B";
            case C:
                return "C";
            default:
                return null;
        }
    }
}
