package libDB;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LibraryServer {

	public static void main(String[] args) {
		try {
			LibraryImpl obj = new LibraryImpl();
			Registry reg = LocateRegistry.createRegistry(1090);
			reg.rebind("LibraryService", obj);
			System.out.println("Library RMI Server is running....");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}