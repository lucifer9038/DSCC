package libDB;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class LibraryClient {

	public static void main(String[] args) {
		try {
			Registry reg = LocateRegistry.getRegistry("localhost",1090);
			LibraryInterface lib= (LibraryInterface) reg.lookup("LibraryService");
			
			Scanner sc= new Scanner(System.in);
			System.out.println("1. get All books ");
			System.out.println("2. get Books by ID ");
			System.out.print("Enter choice: ");
			int choice = sc.nextInt();
			
			if (choice == 1) {
				List<String> books = lib.getAllBooks();
				for(String b : books)
				{
					System.out.println(b);
				}
				
			}
			else if (choice ==2) {
				System.out.println("Enter Book ID : ");
				int id = sc.nextInt();
				System.out.println(lib.getBookById(id));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
