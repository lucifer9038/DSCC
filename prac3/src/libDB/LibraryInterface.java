package libDB;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface LibraryInterface extends Remote {
	List<String> getAllBooks() throws RemoteException;
	String getBookById(int id) throws RemoteException;

}