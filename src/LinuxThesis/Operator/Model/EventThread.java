package Model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

import OperatorSetup.OperatorStartup;
import util.Printer;

public class EventThread extends Thread {
	protected Socket socket;

	public EventThread(Socket clientSocket) {
		this.socket = clientSocket;
		Printer.CustomPrint("Event Channel connected");
	}

	public void run() {
		int counter = 0;
		InputStream inp = null;
		BufferedReader brinp = null;
		try {
			inp = socket.getInputStream();
			brinp = new BufferedReader(new InputStreamReader(inp));
		} catch (IOException e) {
			return;
		}
		String line;
		while (true) {
			try {
				synchronized (this) {
					line = brinp.readLine();
					if ((line == null) || line.equalsIgnoreCase("QUIT")) {
						socket.close();
						return;
					} else {
						Printer.CustomPrint("Event Channel says: " + line);
						List<String> commands;
						commands = OperatorStartup.openConnections.stream().filter(x -> x.eventThread == this)
								.findFirst().get().DecideOnLupsRules(line);
						for (String command : commands) {
							OperatorStartup.BroadCastToUpdateMangers(command);
						}
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}
}
