package Model;


public class ConnectedStreamReasoner {
	public int srID;
	public Thread cmThread;
	public Thread umThread;
	public Thread eventThread;
	
	public ConnectedStreamReasoner(int srID, Thread cmThread, Thread umThread, Thread eventThread) {
		this.srID= srID;
		this.cmThread= cmThread;
		this.umThread = umThread;
		this.eventThread=eventThread;
	}
	
}
