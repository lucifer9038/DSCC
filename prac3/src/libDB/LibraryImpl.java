package libDB;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;


public class LibraryImpl extends UnicastRemoteObject implements LibraryInterface {
	
	Connection conn;
	
	protected LibraryImpl() throws RemoteException
	{
		super();
		try {
			//connect to mysql
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/prac3","root","");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public List<String> getAllBooks() throws RemoteException{
		List<String> books = new ArrayList<String>();
		try {
			Statement stmt= conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Book");
			while(rs.next())
			{
				books.add(rs.getInt(1)+" - "+rs.getString(2)+" by "+rs.getString(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return books;
		
	}
	@Override
	public String getBookById(int id) throws RemoteException{
		try {
			PreparedStatement ps= conn.prepareStatement("SELECT * FROM Book WHERE Book_id = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) + " - "+ rs.getString(2)+" by" + rs.getString(3);
				
			}
			
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "Book not found";
	}

	

}