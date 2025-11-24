package DateTime;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class DateTimeClient {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 2000);
            DateTimeService dateTimeService = (DateTimeService) registry.lookup("DateTimeService");

            String date = dateTimeService.getDate();
            String time = dateTimeService.getTime();

            System.out.println("Server Date: " + date);
            System.out.println("Server Time: " + time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}