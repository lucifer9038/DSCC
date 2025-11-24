package BillDB;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BillServer {
    public static void main(String[] args) {
        try {
            BillServiceImpl obj = new BillServiceImpl();
            Registry reg = LocateRegistry.createRegistry(1099);
            reg.rebind("BillService", obj);
            System.out.println("[SERVER] Bill RMI Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
