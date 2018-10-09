import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.*;

public class MyRMI {
	
	public static MyRMI instance = null; 
	
	private MyRMI() {}
	
	public static MyRMI GetInstance() {
		if(instance == null) instance = new MyRMI();
		return instance;
	}
	
	void register(Solver s, String name) throws IOException {
		FileOutputStream fileOut = new FileOutputStream(name + ".txt");
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(s);
		out.close();
		fileOut.close();
	}
	
	Solver retrieve(String name) throws IOException, ClassNotFoundException {
		Solver s = null;
		FileInputStream fileIn = new FileInputStream(name + ".txt");
		ObjectInputStream in = new ObjectInputStream(fileIn);
		s = (Solver) in.readObject();
		in.close();
		fileIn.close();
		return s;
	}

}
