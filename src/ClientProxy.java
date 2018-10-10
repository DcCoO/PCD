import java.io.IOException;
import java.rmi.RemoteException;

public class ClientProxy implements Solver {

	Client myClient;
	
	static int ServeClient(Client c) {
		ClientProxy cp = new ClientProxy(c);
		Requestor r = new Requestor(cp);
		try {
			return r.MakeRequest();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	private ClientProxy(Client c) {
		myClient = c;
	}
	
	@Override
	public int Solve(String s) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

}
