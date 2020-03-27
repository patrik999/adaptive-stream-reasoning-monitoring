package receivers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import model.EventDetection;
import model.Predicate;
import model.PredicateCommunication;
import util.Printer;
import editors.InterfaceEditor;

public class EventReceiver extends Thread{
	public static List<PredicateCommunication> activePredicates;
	static final int SR_PORT = 1976;

	public void run()  {
		Printer.CustomPrint("Started Event Receiver on Communication Manager");
		activePredicates= Collections.synchronizedList(new ArrayList<PredicateCommunication>());
		
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
		
		try {
			inp = socket.getInputStream();
			brinp = new BufferedReader(new InputStreamReader(inp));
		} catch (IOException e) {
			return;
		}
		String line;


		while (!socket.isClosed()) {
			try {
				line = brinp.readLine();
					decideOnEvent(line);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	public void decideOnEvent(String eventMessage) {
		Thread t= new Thread(()->disectEvent(eventMessage));
		t.start();
	}
	
	public void disectEvent(String eventMessage) {
		String predicateName = eventMessage.substring(0, eventMessage.indexOf("("));
        String[] arguments = eventMessage.substring(eventMessage.indexOf("(") + 1, eventMessage.indexOf(")")).split(",");
        Predicate p=InterfaceEditor.Predicates.stream().
        		filter(x->x.Name.equalsIgnoreCase(predicateName) && x.NumberOfArguments==arguments.length).findFirst().get();
        
        int exceptionId = p.GetPredicateExceptionGivenArguments(Arrays.asList(arguments));
        
        EventDetection newEvent= new EventDetection(
        		p.Id,
        		Calendar.getInstance().getTime(),
        		"location",
        		Arrays.asList(arguments),
        		exceptionId);

        Printer.CustomPrint("Stream Reasoner sends: " + eventMessage);
        
		//has to run synchronously because otherwise if 2 events come in at the same exact time, both times the predicate
		//will not result in the list, thus adding a new instance of "activePredicate". when testing, multiple entries for the same
		//predicate were added due to this
		synchronized(this) {
	        if(activePredicates.stream().noneMatch(x->x.PredicateId==p.Id)) {
	        	
	        	PredicateCommunication pc = new PredicateCommunication(newEvent);
	        	activePredicates.add(pc);        	
	        	pc.AddEvent(newEvent);
	        }
	        else {
	        	activePredicates.stream().filter(x->x.PredicateId==p.Id).findFirst().get().AddEvent(newEvent);
	        }
		}

	}
}
