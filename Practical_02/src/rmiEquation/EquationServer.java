package rmiEquation;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class EquationServer {
    public static void main(String[] args) {
        try {
            EquationSolverImpl solver = new EquationSolverImpl();
            Registry registry = LocateRegistry.createRegistry(1098); // default RMI port
            registry.rebind("EquationService", solver);
            System.out.println("Equation Solver RMI Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
