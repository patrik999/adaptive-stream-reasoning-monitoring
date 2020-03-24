package util;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;
import java.util.stream.Collectors;

import model.EventDetection;
import model.PredicateCommunication;
import receivers.EventReceiver;

public class SendMessagesTask extends TimerTask {
	public int PredicateId;
	public int ExceptionId;
	
	public SendMessagesTask(int predicateId, int exceptionId) {
		PredicateId=predicateId;
		ExceptionId=exceptionId;
	}
	public void run() {
		PredicateCommunication p =EventReceiver.activePredicates.stream().filter(x->x.PredicateId==PredicateId).findFirst().get();
		List<EventDetection> exceptionSubset = p.BuiltUpMessages.stream().filter(x->x.ExceptionId==ExceptionId).collect(Collectors.toList());
		if (exceptionSubset.stream().count() != 0) {
			for (EventDetection e : exceptionSubset){
				try {
					EventChannel.sendEvent(String.format("event(%s,%s,%s,%s,%s,%s)",p.PredicateId,"CM","Operator",e.Location,e.TimeOccurred.toString(),e.Arguments.toString()));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Printer.CustomPrint("Sending to Operator: Predicate id: " + e.PredicateId + " with exception: " + e.ExceptionId);
				// Console.WriteLine(DateTime.Now.Second-SecondOffset+": Predicate id: " +
				// e.PredicateId + " with exception: "+e.ExceptionId+" in location: " +
				// e.Location + " at time point: " + e.TimeOccurred);
			}
			p.BuiltUpMessages.removeAll(exceptionSubset);
		} else {
			// Once the SendMessageTimerVersion method runs once with an empty
			// BuiltUpMessages list, stop the timer. Restart it, once a Detected Event is
			// added to the Built Up MEssages again.
			// This is to avoid having a Background task run all the time.
			if (p.ExceptionRegister.containsKey(ExceptionId)) {
				Printer.CustomPrint("Nothing to send for Predicate: " + PredicateId + " with exception: " + ExceptionId+". Resetting timer.");
				p.ExceptionRegister.remove(ExceptionId);
				this.cancel();
			}
		}
	}
}