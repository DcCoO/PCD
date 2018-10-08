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
import java.util.concurrent.TimeUnit;

public class ClientRequestHandler {
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

		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

		clientSocket.receive(receivePacket);
		//System.out.println("Pacote UDP recebido!");

		String equationAnswer = new String(receivePacket.getData());

		clientSocket.close();

		System.out.println("CLIENT REQUEST HANDLER: client " + c + " answer = " + equationAnswer + ".");

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
		
		System.out.println("CLIENT REQUEST HANDLER: sending client " + c + " request.");
		
		equationAnswer = inFromServer.readLine();
		System.out.println("CLIENT REQUEST HANDLER: client " + c + " answer = " + equationAnswer + ".");
		
		clientSocket.close();

		return Integer.parseInt(equationAnswer);
	}
	
	int MW_Request(Client c, String args) {
		
		int equationAnswer = ServerRequestHandler.instance.CallEquationService(args);
		System.out.println("CLIENT REQUEST HANDLER: client " + c + " answer = " + equationAnswer + ".");
		return equationAnswer;
	}
	
	
	
	public static void main(String[] args) throws InterruptedException, IOException {
		
		ClientRequestHandler crh = new ClientRequestHandler();
		for(int i = 0; i < 3; i++) {
			Client client = new Client(crh);
			client.Solve();
			client.Thank();
			TimeUnit.SECONDS.sleep(1);
			System.out.println();
		}
	}
	
	
}
