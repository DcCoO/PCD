import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class ServerRequestHandler {
	static ServerRequestHandler instance;
	private ServerRequestHandler(){}
	
	static ServerRequestHandler GetInstance() {
		if(instance == null) {
			instance = new ServerRequestHandler();
			try {
				MyRMI.GetInstance().register(new EquationService(), "EquationService");
				
				System.setProperty("java.rmi.server.hostname", "localhost");
				LocateRegistry.createRegistry(1099);
				Solver s = new EquationService();
				Naming.bind("EquationService", (Remote) s);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return instance;
	}
	
	void StartServer() throws AlreadyBoundException, IOException{
		Config.log("SERVER REQUEST HANDLER: opening server.");

		switch(Config.protocol) {
			case UDP: {
				int porta = 5001;
	
				DatagramSocket serverSocket = new DatagramSocket(porta);
				
				Config.log("SERVER REQUEST HANDLER: server opened.");
				
				byte[] receiveData = new byte[1024];
				byte[] sendData = new byte[1024];
	
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				
				serverSocket.receive(receivePacket);
				
				String sentence = new String(receivePacket.getData());
				
				int equationAnswer = (new EquationService()).Solve(sentence);
				
				InetAddress IPAddress = receivePacket.getAddress();
	
				int port = receivePacket.getPort();
	
				sendData = (Integer.toString(equationAnswer)).getBytes();
				
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
				
				serverSocket.send(sendPacket);
				
				serverSocket.close();
				
				StartServer();
				break;
			}
			case TCP: {
				String clientSentence;
				int answer;
				ServerSocket welcomeSocket = new ServerSocket(5000);
	
				Config.log("SERVER REQUEST HANDLER: server opened.");
				Socket connectionSocket = welcomeSocket.accept();
	
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				clientSentence = inFromClient.readLine();
				
				answer = new EquationService().Solve(clientSentence);
				outToClient.writeBytes(Integer.toString(answer) + '\n');
				welcomeSocket.close();
				StartServer();
	
				break;
			}
			case MW: {
				
				String clientSentence;
				int answer = -1;
				ServerSocket welcomeSocket = new ServerSocket(5000);
	
				Config.log("SERVER REQUEST HANDLER: server opened.");
				Socket connectionSocket = welcomeSocket.accept();
	
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				clientSentence = inFromClient.readLine();
				
				try {
					Solver solver = (Solver) Naming.lookup("rmi://localhost:1099/EquationService");
					answer = solver.Solve(clientSentence);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
						
				outToClient.writeBytes(Integer.toString(answer) + '\n');
				welcomeSocket.close();
				StartServer();
				break;
			}
				
			case MRMI: {
				
				String clientSentence;
				int answer = -1;
				ServerSocket welcomeSocket = new ServerSocket(5000);
	
				Config.log("SERVER REQUEST HANDLER: server opened.");
				Socket connectionSocket = welcomeSocket.accept();
	
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				clientSentence = inFromClient.readLine();
				
				try {
					answer = MyRMI.GetInstance().retrieve("EquationService").Solve(clientSentence);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
						
				outToClient.writeBytes(Integer.toString(answer) + '\n');
				welcomeSocket.close();
				StartServer();
				
				
				break;
			}
		}
		System.out.println();
		
	}
	
	
	
	public static void main(String[] args) throws AlreadyBoundException, IOException {
		ServerRequestHandler SRH = new ServerRequestHandler();
		SRH.StartServer();
	}
	
}
