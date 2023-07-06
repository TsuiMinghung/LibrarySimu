package entity;

import global.Error;
import global.Library;
import global.Runner;

public class Book {
    private final BookTemplate bookTemplate;
    private final int id;
    private BookState state;
    private String ownedTime;
    private boolean isSmeared;

    public Book(BookTemplate template,int id,BookState state) {
        this.bookTemplate = template;
        this.id = id;
        this.state = state;
        this.ownedTime = null;
        this.isSmeared = false;
    }

    @Override
    public int hashCode() {
        return bookTemplate.hashCode() * id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Book) {
            return bookTemplate.equals(((Book) o).bookTemplate) &&
                    id == ((Book) o).id;
        } else {
            return false;
        }
    }

    public void setState(BookState newState) {
        String[] output = new String[]{"(State)",Runner.currentTime(),getBookId(),"transfers from"
                ,this.state.toString(),newState.toString()};
        this.state = newState;
        System.out.println(String.join(" ",output));
    }

    public String getBookId() {
        return bookTemplate.getBookId();
    }

    public boolean isSmeared() {
        return isSmeared;
    }

    public void setSmeared() {
        this.isSmeared = true;
    }

    @Override
    public String toString() {
        return bookTemplate.schoolName() + "-" + getBookId();
    }

    public String schoolName() {
        return bookTemplate.schoolName();
    }

    public Library getLibrary() {
        return bookTemplate.getLibrary();
    }

    public void setOwnedTime(String time) {
        this.ownedTime = time;
    }

    public void resetOwned() {
        this.ownedTime = null;
    }

    public boolean overDue() {
        if (ownedTime == null) {
            return false;
        } else {
            switch (bookTemplate.getCategory()) {
                case A:
                    return false;
                case B:
                    return Runner.gapOfDay(ownedTime) > 30;
                case C:
                    return Runner.gapOfDay(ownedTime) > 60;
                default:
                    Error.occur("unreachable");
                    return false;
            }
        }
    }
}
