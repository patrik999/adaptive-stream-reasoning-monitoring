package receivers;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import editors.InterfaceEditor;
import model.EventDetection;
import model.Predicate;
import model.PredicateCommunication;
import util.Printer;

@WebSocket
public class EventReceiver{
	public static List<PredicateCommunication> activePredicates = Collections.synchronizedList(new ArrayList<PredicateCommunication>());
	static final int SR_PORT = 1976;

	@OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.println("Close: statusCode=" + statusCode + ", reason=" + reason);
    }

    @OnWebSocketError
    public void onError(Throwable t) {
        System.out.println("Error: " + t.getMessage());
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("Connect: " + session.getRemoteAddress().getAddress());
        try {
            session.getRemote().sendString("Hello Webbrowser");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnWebSocketMessage
    public void onMessage(String message) {
    	decideOnEvent(message.replace(" ", ""));
    }		

	public void decideOnEvent(String eventMessage) {
		Thread t = new Thread(() -> disectEvent(eventMessage));
		t.start();
	}

	public void disectEvent(String eventMessage) {
		eventMessage=eventMessage.trim();
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
