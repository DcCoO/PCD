
public class Config {
	static Protocol protocol = Protocol.MRMI;
	static boolean debug = true;
	static void log(String s) {
		if(debug) System.out.println(s);
	}
}

enum Protocol{
	UDP, TCP, MW, MRMI
}
