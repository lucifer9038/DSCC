package BillDB;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillServiceImpl extends UnicastRemoteObject implements BillService {

    private Connection conn;

    protected BillServiceImpl() throws RemoteException {
        super();
        try {
            // ✅ Connect to MySQL
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/Electric_Bill?useSSL=false&serverTimezone=UTC", 
                "root", "");  // password if any
            System.out.println("[SERVER] Database connected successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getAllBills() throws RemoteException {
        List<String> bills = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Bill");
            while (rs.next()) {
                bills.add(rs.getString("consumer_name") + 
                          " | Due: " + rs.getDate("bill_due_date") + 
                          " | Amount: " + rs.getBigDecimal("bill_amount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    @Override
    public String getBillByConsumer(String consumerName) throws RemoteException {
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM Bill WHERE consumer_name = ?");
            ps.setString(1, consumerName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("consumer_name") +
                       " | Due: " + rs.getDate("bill_due_date") +
                       " | Amount: " + rs.getBigDecimal("bill_amount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "No bill found for consumer: " + consumerName;
    }
}
