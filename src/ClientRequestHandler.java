import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ClientRequestHandler {
	
	static long start, end;
	
	Protocol protocol = Config.protocol;
	
	int SendRequest(Client c, String args) throws IOException {
		
		switch(protocol) {
		case UDP:
			return UDP_Request(c, args);
		case TCP:
			return TCP_Request(c, args);
		case MW:
			return MW_Request(c, args);
		}
		return -1;
	}
	
	
	int UDP_Request(Client c, String args) throws IOException {

		DatagramSocket clientSocket = new DatagramSocket();

		String servidor = "localhost";
		int porta = 5001;

		InetAddress IPAddress = InetAddress.getByName(servidor);

		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];

		sendData = args.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, porta);

		clientSocket.send(sendPacket);
		
		Config.log("CLIENT REQUEST HANDLER: sending client " + c + " request.");

		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

		clientSocket.receive(receivePacket);

		String equationAnswer = new String(receivePacket.getData());

		int lastIndex = 0;
		for(char ch = '0'; ch <= '9'; ch++) {
			lastIndex = Math.max(lastIndex, equationAnswer.lastIndexOf(ch) + 1);
		}

		equationAnswer = equationAnswer.substring(0, lastIndex);
		clientSocket.close();

		Config.log("CLIENT REQUEST HANDLER: client " + c + " answer = " + equationAnswer + ".");
		
		return Integer.parseInt(equationAnswer);
	}
	
	int TCP_Request(Client c, String args) throws UnknownHostException, IOException {
		String equationAnswer;

		int porta = 5000;
		String servidor = "localhost";

		Socket clientSocket = new Socket(servidor, porta);
		
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
		outToServer.writeBytes(args + '\n');
		
		Config.log("CLIENT REQUEST HANDLER: sending client " + c + " request.");
		
		equationAnswer = inFromServer.readLine();
		Config.log("CLIENT REQUEST HANDLER: client " + c + " answer = " + equationAnswer + ".");
		
		clientSocket.close();

		return Integer.parseInt(equationAnswer);
	}
	
	int MW_Request(Client c, String args) {
		try {
			Solver solver = (Solver) Naming.lookup("rmi://localhost:1099/EquationService");
			int equationAnswer = solver.Solve(args);
			Config.log("CLIENT REQUEST HANDLER: client " + c + " answer = " + equationAnswer + ".");
			return equationAnswer;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	
	
	public static void main(String[] args) throws InterruptedException, IOException {
		
		long total = 0;
		int it = 50000;
		ClientRequestHandler crh = new ClientRequestHandler();
		for(int i = 0; i < it; i++) {
			Client client = new Client(crh);
			start = System.nanoTime();
			client.Solve();
			end = System.nanoTime();
			total += (end - start);
			client.Thank();
			TimeUnit.MILLISECONDS.sleep(100);
			Config.log("");
			//System.out.println(i);
		}
		
		System.out.println("TOTAL = " + (total / 1000000));
		System.out.println("Media = " + ((double) total / 1000000) / it);
	}
	
	
}
