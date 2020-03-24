package receivers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import editors.*;

public class CommandReceiver {
	static final int OM_PORT = 1978;
	
	public void run() throws UnknownHostException, IOException {
		System.out.println(java.time.LocalTime.now()+": "+"Started Command Receiver on Update Manager");


		Socket socket = new Socket("localhost", OM_PORT);

		InputStreamReader inp = null;
		BufferedReader bReader = null;

		inp = new InputStreamReader(socket.getInputStream());
		bReader = new BufferedReader(inp);
		
		String line;

		while (true) {
			try {
				line = bReader.readLine();
				System.out.println("Operator sends: " + line);
				decideOnCommand(line);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}

	public void decideOnCommand(String commandMessage) {
		String command = commandMessage.substring(0, commandMessage.indexOf("("));
		if (command.equals("interfaceCommand")) {
			Thread t = new Thread(() -> {
				try {
					executeInterfaceCommand(commandMessage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			t.start();
		} else if (command.equals("srCommand")) {
			Thread t = new Thread(() -> {
				try {
					executeStreamReasonerCommand(commandMessage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			t.start();
		}
	}

	public void executeStreamReasonerCommand(String commandMessage) {
		List<String> arguments = new ArrayList<String>();
		Matcher m = Pattern.compile("([\\w+|\\s*]|\\[.+\\])+(,?)").matcher(commandMessage.substring(commandMessage.indexOf("(")+1, commandMessage.indexOf(")")));
		while (m.find()) {
			String raw = m.group();
			arguments.add((raw.endsWith(",") ? raw.substring(0, raw.length() - 1) : raw).trim());
		}
		String[] ar =  arguments.toArray(new String[0]);
		String commandName = commandMessage.substring(commandMessage.indexOf("(")+1, commandMessage.indexOf(","));

		switch (commandName) {
		case "addFact":
			StreamReasonerEditor.addFact(ar[1]);
			;
		case "removeFact":
			StreamReasonerEditor.deleteFact(ar[1]);
			;
		}
	}

	public void executeInterfaceCommand(String commandMessage) throws NumberFormatException, Exception {
		List<String> arguments = new ArrayList<String>();
		Matcher m = Pattern.compile("([\\w+|\\s*]|\\[.+\\])+(,?)").matcher(commandMessage.substring(commandMessage.indexOf("(")+1, commandMessage.lastIndexOf(")")));
		while (m.find()) {
			String raw = m.group();
			arguments.add((raw.endsWith(",") ? raw.substring(0, raw.length() - 1) : raw).trim());
		}
		String[] ar =  arguments.toArray(new String[0]);
		String commandName = commandMessage.substring(commandMessage.indexOf("(")+1, commandMessage.indexOf(","));

		switch (commandName) {
		case "addParameter":
			InterfaceEditor.AddParameter(Integer.parseInt(ar[1]), ar[2], ar[3], ar[4], ar[5]);
			break;	
		case "addPredicateBase":
			InterfaceEditor.AddPredicateBase(Integer.parseInt(ar[1]), ar[2], Integer.parseInt(ar[3]), ar[4], ar[5],
					ar[6], Boolean.parseBoolean(ar[7]), Integer.parseInt(ar[8]));
			break;
		case "addException":
			InterfaceEditor.AddException(Integer.parseInt(ar[1]), Integer.parseInt(ar[2]), ar[3], ar[4],
					Boolean.parseBoolean(ar[5]), Integer.parseInt(ar[6]));
			break;
		case "deleteParameter":
			InterfaceEditor.DeleteParameter(Integer.parseInt(ar[1]));
			break;
		case "deletePredicate":
			InterfaceEditor.DeletePredicate(Integer.parseInt(ar[1]));
			break;
		case "deleteException":
			InterfaceEditor.DeleteException(Integer.parseInt(ar[1]), Integer.parseInt(ar[2]));
			break;
		case "editParameterValue":
			InterfaceEditor.EditParameterValue(Integer.parseInt(ar[1]), ar[2]);
			break;
		case "editPredicateCommunication":
			InterfaceEditor.EditPredicateCommunication(Integer.parseInt(ar[1]), ar[2], Boolean.parseBoolean(ar[3]),
					Integer.parseInt(ar[4]));
			break;
		default:
			System.out.println("Command name: "+commandName+" has not been recognized");
		}
		InterfaceEditor.SaveChanges();
	}
}
