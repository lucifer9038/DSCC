package calc;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class CalculatorClient {

	public static void main(String[] args) {
		try {
			Registry registry =LocateRegistry.getRegistry("localhost",1099);
			Calculator calculator =(Calculator) registry.lookup("CalculatorService");
			
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter a First number: ");
			double a = sc.nextDouble();
			System.out.println("Enter a  Second Number");
			double b = sc.nextDouble();
			
			System.out.println("Choose operation (+,-,*,/): ");
			char op =sc.next().charAt(0);
		
			double result =0;
	        switch (op) {
	                case '+':
	                    result = calculator.add(a, b);
	                    System.out.println("Result: " + result);
	                    break;
	                case '-':
	                    result = calculator.sub(a, b);
	                    System.out.println("Result: " + result);
	                    break;
	                case '*':
	                    result = calculator.mul(a, b);
	                    System.out.println("Result: " + result);
	                    break;
	                case '/':
	                    if (b == 0) {
	                        System.out.println("Cannot divide by zero!");
	                    } else {
	                        result = calculator.div(a, b);
	                        System.out.println("Result: " + result);
	                    }
	                    break;
	                default:
	                    System.out.println("Invalid operation.");
	            }
	        
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}