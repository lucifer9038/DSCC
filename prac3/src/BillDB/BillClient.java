package BillDB;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class BillClient {
    public static void main(String[] args) {
        try {
            // Connect to RMI registry
            Registry reg = LocateRegistry.getRegistry("localhost", 1099);
            BillService billService = (BillService) reg.lookup("BillService");

            Scanner sc = new Scanner(System.in);
            int choice;

            do {
                System.out.println("\n===== Electric Bill Menu =====");
                System.out.println("1. Get All Bills");
                System.out.println("2. Get Bill by Consumer Name");
                System.out.print("Enter your choice: ");
                choice = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        List<String> bills = billService.getAllBills();
                        if (bills.isEmpty()) {
                            System.out.println("No bills found!");
                        } else {
                            System.out.println("\n--- Bill Details ---");
                            for (String b : bills) {
                                System.out.println(b);
                            }
                        }
                        break;

                    case 2:
                        System.out.print("Enter Consumer Name: ");
                        String name = sc.nextLine();
                        String result = billService.getBillByConsumer(name);
                        System.out.println("\n" + result);
                        break;

                    default:
                        System.out.println("Invalid choice. Try again!");
                }
            } while (choice != 3);

            sc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
