
public class Config {
	static Protocol protocol = Protocol.UDP;
	static boolean debug = false;
	static void log(String s) {
		if(debug) System.out.println(s);
	}
}

enum Protocol{
	UDP, TCP, MW
}
