

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Solver extends Remote{
	public int Solve(String s) throws RemoteException;
}
