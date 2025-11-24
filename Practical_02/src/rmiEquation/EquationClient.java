package rmiEquation;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class EquationClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1098);
            EquationSolver solver = (EquationSolver) registry.lookup("EquationService");

            Scanner sc = new Scanner(System.in);
            System.out.print("Enter value of a: ");
            double a = sc.nextDouble();
            System.out.print("Enter value of b: ");
            double b = sc.nextDouble();

            double result = solver.solveEquation(a, b);
            System.out.println("Result of (a-b)^2 = " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
