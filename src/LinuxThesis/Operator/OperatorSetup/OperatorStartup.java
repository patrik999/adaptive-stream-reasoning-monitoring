package OperatorSetup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Model.CmThread;
import Model.ConnectedStreamReasoner;
import Model.EventThread;
import Model.LupsRule;
import Model.UmThread;
import util.Printer;

public class OperatorStartup {

	public static int exp=2;
	public static int test=3;
	static final int CM_PORT = 1977;
	static final int UM_PORT = 1978;
	static final int EVENT_PORT = 1980;
	public static List<ConnectedStreamReasoner> openConnections = new ArrayList<ConnectedStreamReasoner>();

	public static void main(String args[]) throws IOException {
		ConnectedStreamReasoner sr0 = new ConnectedStreamReasoner();
		sr0.srID = 0;
		assignLupsRules(sr0);
		openConnections.add(sr0);

		Printer.SetStartTime(LocalDateTime.now());
		Printer.CustomPrint("Started server Program");

		ServerSocket cm_ServerSocket = null;
		ServerSocket um_ServerSocket = null;
		ServerSocket event_ServerSocket = null;

		try {
			cm_ServerSocket = new ServerSocket(CM_PORT);
			um_ServerSocket = new ServerSocket(UM_PORT);
			event_ServerSocket = new ServerSocket(EVENT_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}

		CreateCMReceiver(cm_ServerSocket);
		CreateUMReceiver(um_ServerSocket);
		CreateEventReceiver(event_ServerSocket);

		BufferedReader consoleBr = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			// BroadCastToCommunicationManagers(consoleBr.readLine());
			BroadCastToUpdateMangers(consoleBr.readLine());
		}
	}

	// Launches a thread whos job it is to listen for incoming Update Manager
	// connection requests.
	// When one is detected, a new thread is launched that will deal with messages
	// coming from that Update Manager.
	public static void CreateUMReceiver(ServerSocket serverSocket) {
		Runnable umServer = new Runnable() {
			public void run() {
				Socket socket = null;
				while (true) {
					try {
						socket = serverSocket.accept();
					} catch (IOException e) {
						System.out.println("I/O error: " + e);
					}
					// new thread for a client
					Thread t = new UmThread(socket);
					t.start();
					if (openConnections.stream().noneMatch(x -> x.srID == 0)) {
						openConnections.add(new ConnectedStreamReasoner(0, null, t, null));
					} else {
						openConnections.stream().filter(x -> x.srID == 0).findFirst().get().umThread = t;
					}
				}
			}
		};

		Thread umThread = new Thread(umServer);
		umThread.start();
		Printer.CustomPrint("Started um Receiver Thread");
	}

	public static void CreateCMReceiver(ServerSocket serverSocket) {
		Runnable cmServer = new Runnable() {
			public void run() {
				Socket socket = null;
				while (true) {
					try {
						socket = serverSocket.accept();
					} catch (IOException e) {
						System.out.println("I/O error: " + e);
					}
					// new thread for a client
					Thread t = new CmThread(socket);
					t.start();
					if (openConnections.stream().noneMatch(x -> x.srID == 0)) {
						openConnections.add(new ConnectedStreamReasoner(0, t, null, null));
					} else {
						openConnections.stream().filter(x -> x.srID == 0).findFirst().get().cmThread = t;
					}
				}
			}
		};
		Thread cmThread = new Thread(cmServer);
		cmThread.start();
		Printer.CustomPrint("Started cm Receiver Thread");
	}

	public static void CreateEventReceiver(ServerSocket serverSocket) {
		Runnable eventServer = new Runnable() {
			public void run() {
				Socket socket = null;
				while (true) {
					try {
						socket = serverSocket.accept();
					} catch (IOException e) {
						System.out.println("I/O error: " + e);
					}
					// new thread for a client
					Thread t = new EventThread(socket);
					t.start();
					if (openConnections.stream().noneMatch(x -> x.srID == 0)) {
						openConnections.add(new ConnectedStreamReasoner(0, null, null, t));
					} else {
						openConnections.stream().filter(x -> x.srID == 0).findFirst().get().eventThread = t;
					}
				}
			}
		};
		Thread eventChannelThread = new Thread(eventServer);
		eventChannelThread.start();
		Printer.CustomPrint("Started Event Channel Receiver Thread");
	}

	public static void assignLupsRules(ConnectedStreamReasoner sr0) {
		//Exp1Test1
		if(exp==1 && test==1) {
			sr0.lupsRules.add(new LupsRule("execute rule editPredicateCommunication(0,push,false,0) when trafficJam(l34)"));
		}
		
		//Exp1Test2
		if(exp==1 && test==2) {
		//manually input: interfaceCommand(addException^-1^0^0^[(0,[l20])]^push^false^0)
		}
		
		//Exp2Test1
		if(exp==2 && test==1) {
		sr0.lupsRules.add(new LupsRule("disable for -1 rule trafficCount when prideParade(l34)"));
		sr0.lupsRules.add(new LupsRule("enable for -1 rule trafficCount when prideParadeEnd(l34)"));
		}
		
		//Exp2Test2
		if(exp==2 && test==2) {
		sr0.lupsRules.add(new LupsRule("execute for 60 rule editParameterValue(0,001.00) when trafficJam(l20)"));
		}
		
		//Exp2Test3
		if(exp==2 && test==3) {
			sr0.lupsRules.add(new LupsRule("assert for 15 rule hasSignalGroup(L,T) :- &sql2[\"SELECT a,b FROM object_role_assertion WHERE object_role=151\"](L,T). when trafficJam(l20)"));
			sr0.lupsRules.add(new LupsRule("assert for 15 rule trafficLighState(T,S) :- &sql2[\"SELECT DISTINCT ON (iid) iid, x FROM v_signalstate_mrel WHERE tp > 0 ORDER BY iid, tp DESC\"](T,S). when trafficJam(l20)"));
			sr0.lupsRules.add(new LupsRule("assert for 15 rule @wsSend(\"trafficLightState(\",T,\",\",S,\")\") :- hasSignalGroup(L,T), trafficLighState(T,S). when trafficJam(l20)"));		
			
			}
	}

	public static void BroadCastToCommunicationManagers(String message) {
		for (ConnectedStreamReasoner sr : openConnections) {
			((CmThread) sr.cmThread).pushMessage(message);
		}
	}

	public static void BroadCastToUpdateMangers(String message) {
		for (ConnectedStreamReasoner sr : openConnections) {
			((UmThread) sr.umThread).pushMessage(message);
		}
	}

}
