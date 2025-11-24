package BillDB;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface BillService extends Remote {
    List<String> getAllBills() throws RemoteException;
    String getBillByConsumer(String consumerName) throws RemoteException;
}
