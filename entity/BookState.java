package entity;

public enum BookState {
    newBook,
    onStudent,
    borrowAndReturn,
    logistics,
    machine,
    transport,
    receive;

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
            case transport:
                return "transport";
            case receive:
                return "receive";
            default:
                return "unreachable state";
        }
    }
}
