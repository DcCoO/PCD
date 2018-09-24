

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.UUID;

public class Client{
	String ID;
	int [] coef;
	int var;
	private int answer;
	ClientRequestHandler CRH;
	
	public Client(ClientRequestHandler crh) {
		ID = UUID.randomUUID().toString().substring(0, 4);
		this.CRH = crh;
		
		java.util.Random r = new java.util.Random();
		var = 1 + r.nextInt(5);
		coef = new int[1 + r.nextInt(4)];
		for(int i = 0; i < coef.length; i++) coef[i] = r.nextInt(6);
	}
	
	String equation() {
		String s = "";
		for(int i = 0; i < coef.length; i++) {
			s += coef[coef.length - i - 1] + "*" + var + "^" + (coef.length - i - 1); 
			s += (i != coef.length - 1? " + ": "");
		}
		return s;
	}
	
	String GetArgs() {
		String s = "";
		for(int i = 0; i < coef.length; i++) {
			s += coef[i] + " ";
		}
		return s + var;
	}

	public int Solve() throws IOException {
		System.out.println(ID + ": can you solve " + equation() + " for me?");
		answer = CRH.SendRequest(this, GetArgs());
		return answer;
	}
	
	public void Thank() {
		System.out.println(ID + ": thank you for answering " + answer + " for me!");
	}
	
	public String toString() {
		return ID;
	}
	
	
}
