package DateTime;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DateTimeService extends Remote {
	String getDate() throws RemoteException;  
    String getTime() throws RemoteException;
    

}