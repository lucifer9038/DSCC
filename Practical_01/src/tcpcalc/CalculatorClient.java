package tcpcalc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class CalculatorClient {

	public static void main(String[] args) {
		
		try (Socket s = new Socket("localhost", 1235)) {
		    BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		    PrintWriter out = new PrintWriter(s.getOutputStream(), true); 

		    Scanner sc = new Scanner(System.in);
		    System.out.println("Enter an operation (add/sub/mul/div):");
		    String operation = sc.next();
		    System.out.println("Enter number 1:");
		    double num1 = sc.nextDouble();
		    System.out.println("Enter number 2:");
		    double num2 = sc.nextDouble();

		    String request = operation + ":" + num1 + ":" + num2;
		    out.println(request);

		    String response = in.readLine();
		    System.out.println("Response from server = " + response);
		} catch (Exception e) {
		    e.printStackTrace();
		}

	}

}