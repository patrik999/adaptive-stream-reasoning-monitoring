package receivers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import util.Printer;


public class RequestReceiver extends Thread{
	static final int OM_PORT = 1977;

	public void run(){
		Printer.CustomPrint("Started Request Receiver on Communication Manager");

		Socket socket = null;
		try {
			socket = new Socket("localhost", OM_PORT);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		InputStreamReader inp = null;
		BufferedReader bReader = null;

		try {
			inp = new InputStreamReader(socket.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		bReader = new BufferedReader(inp);

		String line;

		while (true) {
			try {
				line = bReader.readLine();
				Printer.CustomPrint("Operator sends: " + line);
				decideOnRequest(line);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}

	public void decideOnRequest(String requestMessage) {
		String command = requestMessage.substring(0, requestMessage.indexOf("("));
		if (command == "request") {
			Thread t = new Thread(() -> {
				try {
					requestAnswer(requestMessage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			});
			t.start();
		}
	}

	public void requestAnswer(String requestMessage) {
		int predicateId = Integer
				.parseInt(requestMessage.substring(requestMessage.indexOf("("), requestMessage.indexOf(",")));
		int exceptionId = Integer
				.parseInt(requestMessage.substring(requestMessage.indexOf(","), requestMessage.indexOf("(")));
		EventReceiver.activePredicates.stream().filter(x -> x.PredicateId == predicateId).findFirst().get()
				.RequestSpecificException(exceptionId);

	}
}
