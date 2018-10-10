import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Requestor {
	ClientRequestHandler CRH = ClientRequestHandler.GetInstance();
	ClientProxy cp;
	
	Requestor(ClientProxy cp){
		this.cp = cp;
	}
	
	int MakeRequest() throws IOException {
		switch(Config.protocol) {
		case UDP:
			return CRH.UDP_Request(cp.myClient, cp.myClient.GetArgs());
		case TCP:
			return CRH.TCP_Request(cp.myClient, cp.myClient.GetArgs());
		case MW:
			return CRH.MW_Request(cp.myClient, cp.myClient.GetArgs());
		case MRMI:
			return CRH.MRMI_Request(cp.myClient, cp.myClient.GetArgs());
		}
		return -1;
	}
}
