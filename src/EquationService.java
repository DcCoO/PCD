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
		
		int lastIndex = 0;
		for(char ch = '0'; ch <= '9'; ch++) {
			lastIndex = Math.max(lastIndex, s.lastIndexOf(ch) + 1);
		}
		s = s.substring(0, lastIndex);
		Scanner in = new Scanner(s);
		ArrayList<Integer> coef = new ArrayList<Integer>();
		while(in.hasNextInt()) {
			coef.add(in.nextInt());
		}
		
		
		int resp = 0;
		int var = coef.get(coef.size() - 1);
		for(int i = coef.size() - 2; i >= 0; i--) {
			//System.out.print(coef.get(i) + "*" + var + "^" + i + " + ");
			resp += coef.get(i) * Math.pow(var, i);
		}
		in.close();
		return resp;
	}
}
