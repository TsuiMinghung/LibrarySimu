package department;

import entity.Book;
import entity.BookTemplate;
import entity.Transport;
import global.Library;
import global.Runner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class Purchasing {

    private final String name;
    private final ArrayList<Transport> lendTo;
    private final ArrayList<Transport> returnTo;
    private final ArrayList<Transport> receive;
    private final ArrayList<Book> books;
    private final Library library;

    public Purchasing(Library library) {
        this.name = "purchasing department";
        this.library = library;
        this.lendTo = new ArrayList<>();
        this.returnTo = new ArrayList<>();
        this.receive = new ArrayList<>();
        this.books = new ArrayList<>();
    }

    public Collection<Book> collect() {
        Collection<Book> result = new ArrayList<>(books);
        books.clear();
        return result;
    }

    public void lendToTransport(Transport transport) {
        lendTo.add(transport);
    }

    public void returnToTransport(Transport transport) {
        returnTo.add(transport);
    }

    public void executeTransport() {
        String time = "[" + Runner.currentTime() + "]";
        for (Transport transport : lendTo) {
            String[] output = new String[]{time,transport.getBook().toString()
                    ,"got transported by",name,"in",library.schoolName()};
            System.out.println(String.join(" ",output));
            transport.getTo().getPurchasing().dealReceive(transport);
        }
        for (Transport transport : returnTo) {
            String[] output = new String[]{time,transport.getBook().toString()
                    ,"got transported by",name,"in",library.schoolName()};
            System.out.println(String.join(" ",output));
            transport.getTo().getPurchasing().dealReceive(transport);
        }
        lendTo.clear();
        returnTo.clear();
    }

    public void dealReceive(Transport transport) {
        receive.add(transport);
    }

    public void printReceive() {
        String time = "[" + Runner.currentTime() + "]";
        for (Transport transport : receive) {
            String[] output = new String[]{time,transport.getBook().toString(),"got received by"
                    ,name,"in",library.schoolName()};
            System.out.println(String.join(" ",output));
        }
    }

    public void dealInterBorrow() {
        String time = "[" + Runner.currentTime() + "]";
        for (Transport transport : receive) {
            if (transport.isInterBorrow()) {
                String[] output = new String[]{time, name, "lent", transport.getBook().toString()
                        , "to", transport.getStudent().toString()};
                System.out.println(String.join(" ", output));
                transport.getStudent().own(transport.getBook());
                output = new String[]{time, transport.getStudent().toString(), "borrowed"
                        , transport.getBook().toString(), "from", name};
                System.out.println(String.join(" ", output));
            } else {
                books.add(transport.getBook());
            }
        }
        receive.clear();
    }

    public void dealPurchase() {
        String time = "[" + Runner.currentTime() + "]";
        for (Map.Entry<String,Integer> kv : library.getOrdering().getPurchaseList().entrySet()) {
            BookTemplate template = new BookTemplate(kv.getKey(),library,Math.max(kv.getValue(),3));
            while (template.isAvailable()) {
                books.add(template.fetch());
            }

            String[] output = new String[]{time,library.schoolName() + "-" + kv.getKey()
                    ,"got purchased by",name,"in",library.schoolName()};
            System.out.println(String.join(" ",output));
        }
    }

}
