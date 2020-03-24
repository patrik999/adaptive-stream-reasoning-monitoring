package util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class EventChannel {
	static final int OP_PORT = 1980;
	public static DataOutputStream out;
	public static void init() throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", OP_PORT);
		Printer.CustomPrint("Started Event Channel on Communication Manager");
		
		out = new DataOutputStream(socket.getOutputStream());
	}
	public static void sendEvent(String message) throws IOException {
    	out.writeBytes(message + "\n");
        out.flush();
	}
}
