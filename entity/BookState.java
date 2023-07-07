package entity;

public enum BookState {
    newBook,
    onStudent,
    borrowAndReturn,
    logistics,
    machine,
    shelf,
    ordering,
    arranging,
    road,
    purchasing;

    @Override
    public String toString() {
        switch (this) {
            case onStudent:
                return "onStudent";
            case borrowAndReturn:
                return "borrowAndReturn";
            case logistics:
                return "logistics";
            case machine:
                return "machine";
            case newBook:
                return "newBook";
            case purchasing:
                return "purchasing";
            case arranging:
                return "arranging";
            case shelf:
                return "shelf";
            case ordering:
                return "ordering";
            case road:
                return "road";
            default:
                return "unreachable state";
        }
    }
}
