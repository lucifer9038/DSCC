package DateTime;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class DateTimeServer {

    public static void main(String[] args) {
        try {
            DateTimeService dateTimeService = new DateTimeServiceImpl();
            Registry registry = LocateRegistry.createRegistry(2000);
            registry.rebind("DateTimeService", dateTimeService);
            System.out.println("DateTime RMI Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}