package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;

import util.*;
import editors.InterfaceEditor;
import model.*;

public class PredicateCommunication {

	public int PredicateId;
	public List<EventDetection> BuiltUpMessages;
	public Hashtable<Integer, CustomTimer> ExceptionRegister = new Hashtable<Integer, CustomTimer>();

	public PredicateCommunication(EventDetection detectedEvent) {
		PredicateId = detectedEvent.PredicateId;
		BuiltUpMessages = Collections.synchronizedList(new ArrayList<EventDetection>());
	}

	public void AddEvent(EventDetection detectedEvent)
    {
        BuiltUpMessages.add(detectedEvent);

        Predicate p = InterfaceEditor.Predicates.parallelStream().filter(x->x.Id==detectedEvent.PredicateId).findFirst().get();
        CommunicationMode cm = p.GetCommunicationModeByPredicateException(detectedEvent.ExceptionId);

        if (cm.Mode.equals("push"))
        {
            //In this case communication is push based without a buffer. So we just send the messages immediatly
            if (!cm.Buffered)
            {
                new SendMessagesTask(PredicateId,detectedEvent.ExceptionId).run();
            }
            //In this case we see this Exception for the first time. So we send the messages and then start the burst timer.
            else if (cm.Buffered && !ExceptionRegister.containsKey(detectedEvent.ExceptionId))
            {
                int time = cm.SameMessageDelay*1000;
                CustomTimer timer = new CustomTimer();
                timer.SecondsDelay=time;
                timer.schedule(new SendMessagesTask(PredicateId, detectedEvent.ExceptionId),0, time);

                ExceptionRegister.put(detectedEvent.ExceptionId, timer);
            }
            //In this case the communication mode is buffered and we have added Events previously. So if the clock has been stopped, we can send message immediatly
            else if(cm.Buffered && ExceptionRegister.containsKey(detectedEvent.ExceptionId))
            {
            	if(ExceptionRegister.get(detectedEvent.ExceptionId).SecondsDelay!=cm.SameMessageDelay*1000) {
            		ExceptionRegister.get(detectedEvent.ExceptionId).cancel();
            		
            		int time = cm.SameMessageDelay*1000;
                    CustomTimer timer = new CustomTimer();
                    timer.SecondsDelay=time;
                    timer.schedule(new SendMessagesTask(PredicateId, detectedEvent.ExceptionId),0, time);
                    
                    ExceptionRegister.put(detectedEvent.ExceptionId, timer);
            	}
                //Do nothing since Thread will come and pick up the messages
            }
        }
        else
        {
            //Here it is pull, since we added the detected Event already we don't need to do anything else.
        }
    }
    public void RequestSpecificException(int exceptionId)
    {
        new SendMessagesTask(PredicateId,exceptionId).run();
    }
	
}
