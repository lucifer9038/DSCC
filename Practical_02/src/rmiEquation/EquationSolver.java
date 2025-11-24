package rmiEquation;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface EquationSolver extends Remote {
	double solveEquation(double a, double b) throws RemoteException;

}
