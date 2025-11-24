package tcpcalc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class CalculatorServer {

	public static void main(String[] args) 
	{
		try (ServerSocket serverSocket = new ServerSocket(1235)) {
		    System.out.println("Calculator Server is running on port 1235...");
		    
		    while (true) {
		        Socket clientSocket = serverSocket.accept();
		        System.out.println("Client Connected!");
		        
		        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); 

		        String request = in.readLine(); 
		        System.out.println("Request: " + request);
		        
		        String[] parts = request.split(":");
		        String operation = parts[0].toLowerCase();
		        double num1 = Double.parseDouble(parts[1]);
		        double num2 = Double.parseDouble(parts[2]);
		        double result;

		        switch (operation) {
		            case "add": result = num1 + num2; break;
		            case "sub": result = num1 - num2; break;
		            case "mul": result = num1 * num2; break;
		            case "div": result = (num2 != 0) ? num1 / num2 : Double.NaN; break;
		            default: result = Double.NaN;
		        }

		        out.println("Result:" + result);
		        clientSocket.close();
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}

	}
}