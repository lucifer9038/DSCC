package rmiEquation;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class EquationSolverImpl extends UnicastRemoteObject implements EquationSolver {
	protected EquationSolverImpl() throws RemoteException {
        super();
    }

	@Override
	public double solveEquation(double a, double b) throws RemoteException {
		// TODO Auto-generated method stub
		return (a*a) - (2*a*b) + (b*b);
	}


}
