import java.io.IOException;
import java.rmi.RemoteException;

public class ClientProxy implements Solver {

	Client myClient;
	ClientProxy cp;
	Requestor r;
	
	int ServeClient(Client c) {
		cp = new ClientProxy(c);
		r = new Requestor(cp);
		try {
			return r.MakeRequest();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	public ClientProxy(Client c) {
		myClient = c;
	}
	
	@Override
	public int Solve(String s) throws RemoteException {
		// TODO Auto-generated method stub
		try {
			return r.MakeRequest();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

}
