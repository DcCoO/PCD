import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

public class EquationService extends UnicastRemoteObject implements Solver{
	
	private static final long serialVersionUID = 1L;

	protected EquationService() throws RemoteException {
		super();
	}

	@Override
	public int Solve(String s) throws RemoteException {
		Scanner in = new Scanner(s);
		ArrayList<Integer> coef = new ArrayList<Integer>();
		while(in.hasNextInt()) {
			coef.add(in.nextInt());
		}
		int resp = 0;
		int var = coef.get(coef.size() - 1);
		for(int i = 0; i < coef.size() - 1; i++) {
			resp += Math.pow(var, i) * coef.get(i);
		}
		in.close();
		return resp;
	}
}
