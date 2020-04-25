package Model;

import java.util.List;
import java.util.ArrayList;

public class ConnectedStreamReasoner {
	public int srID;
	public Thread cmThread;
	public Thread umThread;
	public Thread eventThread;
	public List<LupsRule> lupsRules = new ArrayList<LupsRule>();
	
	public ConnectedStreamReasoner() {
		
	}
	public ConnectedStreamReasoner(int srID, Thread cmThread, Thread umThread, Thread eventThread) {
		this.srID= srID;
		this.cmThread= cmThread;
		this.umThread = umThread;
		this.eventThread=eventThread;
	}
	
	public List<String> DecideOnLupsRules(String eventMessage) {
		List<String> commandsToExecute= new ArrayList<String>();
		List<LupsRule> triggerLupsRules= new ArrayList<LupsRule>();
		for(LupsRule lr : lupsRules) {
			if(lr.CheckLupsCondition(eventMessage)) {
				triggerLupsRules.add(lr);
				commandsToExecute.add(lr.TranslateToUmCommand());
			}
		}
		lupsRules.removeAll(triggerLupsRules);
		return commandsToExecute;
	}
	
}
