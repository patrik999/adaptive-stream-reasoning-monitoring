package receivers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import communicationManagerSetup.CmStartup;
import util.InterfaceReader;
import util.Printer;

public class UpdateReceiver extends Thread {
	static final int UR_PORT = 1979;

	public void run() {
		Printer.CustomPrint("Started Update Receiver on Communication Manager");

		ServerSocket sr_ServerSocket = null;

		Socket socket = null;

		try {
			sr_ServerSocket = new ServerSocket(UR_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (true) {
			try {
				socket = sr_ServerSocket.accept();
			} catch (IOException e) {
				System.out.println("I/O error: " + e);
			}

			InputStreamReader inp = null;
			BufferedReader bReader = null;

			try {
				inp = new InputStreamReader(socket.getInputStream());
				bReader = new BufferedReader(inp);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			String line;

			try {
				line = bReader.readLine();
				Printer.CustomPrint("Update Manager sends: " + line);
				decideOnMessage(line);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}

	public void decideOnMessage(String updateMessage) {
		String command = updateMessage.substring(0, updateMessage.indexOf("("));
		if (command.equalsIgnoreCase("alert")) {
			Thread t = new Thread(() -> {
				try {
					String argument = updateMessage.substring(updateMessage.indexOf("(")+1, updateMessage.indexOf(")"));
					if (argument.equals("interface_updated")) {
						updateInterface();
					}
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			t.start();
		}

	}

	public void updateInterface() throws ParserConfigurationException, SAXException, IOException {
		InterfaceReader.LoadInterface(CmStartup.interfacePath);
		Printer.CustomPrint("Interface reloaded");
	}
}
