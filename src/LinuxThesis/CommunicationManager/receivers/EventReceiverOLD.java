package receivers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.EventDetection;
import model.Predicate;
import model.PredicateCommunication;
import util.Printer;
import editors.InterfaceEditor;

public class EventReceiverOLD extends Thread {
	public static List<PredicateCommunication> activePredicates;
	static final int SR_PORT = 1976;
	int counter=0;

	public void run() {
		
		Printer.CustomPrint("Started Event Receiver on Communication Manager");
		activePredicates = Collections.synchronizedList(new ArrayList<PredicateCommunication>());

		ServerSocket sr_ServerSocket = null;

		Socket socket = null;

		try {
			sr_ServerSocket = new ServerSocket(SR_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			socket = sr_ServerSocket.accept();
		} catch (IOException e) {
			System.out.println("I/O error: " + e);
		}

		InputStream inp = null;
		BufferedReader brinp = null;
		DataOutputStream out = null;

		try {
			inp = socket.getInputStream();
			// Scanner s= new Scanner(inp,"UTF-8");
			brinp = new BufferedReader(new InputStreamReader(inp));
			// out = socket.getOutputStream();
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			return;
		}
		String line;

		while (!socket.isClosed()) {
			try {
				line = brinp.readLine();
				if (line != null) {
					//Printer.CustomPrint("Stream Reasoner sends: " + line);

					Matcher get = Pattern.compile("^GET").matcher(line);
					if (get.find()) {
						String request = line;
						while (brinp.ready()) {
							String temp = brinp.readLine() + "\n";
							request += temp;

						}
						Matcher match = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(request);
						match.find();
						String responseS = "HTTP/1.1 101 Switching Protocols\r\n" + "Connection: Upgrade\r\n"
								+ "Upgrade: websocket\r\n" + "Sec-WebSocket-Accept: "
								+ Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1").digest(
										(match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes("UTF-8")))
								+ "\r\n\r\n";
						out.writeBytes(responseS);
						byte[] response = (responseS).getBytes("UTF-8");
						// out.write(response, 0, response.length);
					} else {
						//DECODE MESSAGE FROM WEBSOCKET SENDER
						//decodeMessage(inp);
						
						//OLD plain socket implemenation
						// decideOnEvent(line);
					}
					
				}
			} catch (IOException e) {
				e.printStackTrace();
				return;
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void decodeMessage(InputStream rawIn) throws IOException {
		int len = 0;
		byte[] b = new byte[8000];
		// rawIn is a Socket.getInputStream();
		while (true) {
			len = rawIn.read(b);
			if (len != -1) {
				byte rLength = 0;
				int rMaskIndex = 2;
				int rDataStart = 0;
				// b[0] is always text in my case so no need to check;
				byte data = b[1];
				byte op = (byte) 127;
				rLength = (byte) (data & op);

				if (rLength == (byte) 126)
					rMaskIndex = 4;
				if (rLength == (byte) 127)
					rMaskIndex = 10;

				byte[] masks = new byte[4];

				int j = 0;
				int i = 0;
				for (i = rMaskIndex; i < (rMaskIndex + 4); i++) {
					masks[j] = b[i];
					j++;
				}

				rDataStart = rMaskIndex + 4;

				int messLen = len - rDataStart;

				byte[] message = new byte[messLen];

				for (i = rDataStart, j = 0; i < len; i++, j++) {
					message[j] = (byte) (b[i] ^ masks[j % 4]);
				}
				String sMessage=new String(message);
				if(sMessage.startsWith("i")||sMessage.startsWith("e")||sMessage.startsWith(" ")) {
					Printer.CustomPrint(sMessage.substring(0,sMessage.indexOf(")")+1));
					counter++;
					Printer.CustomPrint(""+counter);
				}
				
				//Printer.CustomPrint(new String(b));
				// parseMessage(new String(b));

				b = new byte[8000];

			}
		}
	}

	public void decideOnEvent(String eventMessage) {
		Thread t = new Thread(() -> disectEvent(eventMessage));
		t.start();
	}

	public void disectEvent(String eventMessage) {
		String predicateName = eventMessage.substring(0, eventMessage.indexOf("("));
		String[] arguments = eventMessage.substring(eventMessage.indexOf("(") + 1, eventMessage.indexOf(")"))
				.split(",");
		Predicate p = InterfaceEditor.Predicates.stream()
				.filter(x -> x.Name.equalsIgnoreCase(predicateName) && x.NumberOfArguments == arguments.length)
				.findFirst().get();

		int exceptionId = p.GetPredicateExceptionGivenArguments(Arrays.asList(arguments));

		EventDetection newEvent = new EventDetection(p.Id, Calendar.getInstance().getTime(), "location",
				Arrays.asList(arguments), exceptionId);

		Printer.CustomPrint("Stream Reasoner sends: " + eventMessage);

		// has to run synchronously because otherwise if 2 events come in at the same
		// exact time, both times the predicate
		// will not result in the list, thus adding a new instance of "activePredicate".
		// when testing, multiple entries for the same
		// predicate were added due to this
		synchronized (this) {
			if (activePredicates.stream().noneMatch(x -> x.PredicateId == p.Id)) {

				PredicateCommunication pc = new PredicateCommunication(newEvent);
				activePredicates.add(pc);
				pc.AddEvent(newEvent);
			} else {
				activePredicates.stream().filter(x -> x.PredicateId == p.Id).findFirst().get().AddEvent(newEvent);
			}
		}

	}
}
