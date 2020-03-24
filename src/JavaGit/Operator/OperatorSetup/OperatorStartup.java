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
import Model.UmThread;
import util.Printer;

public class OperatorStartup {
	
	 static final int CM_PORT = 1977;
	 static final int UM_PORT = 1978;
	 static final int EVENT_PORT=1980;
	 static List<ConnectedStreamReasoner> openConnections= new ArrayList<ConnectedStreamReasoner>();
	 
	    public static void main(String args[])throws IOException {
	    	Printer.SetStartTime(LocalDateTime.now());
	    	Printer.CustomPrint("Started server Program");
	    	
	        ServerSocket cm_ServerSocket = null;
	        ServerSocket um_ServerSocket = null;
	        ServerSocket event_ServerSocket=null;
	        
	        try {
		        cm_ServerSocket  = new ServerSocket(CM_PORT);
		        um_ServerSocket  = new ServerSocket(UM_PORT);
		        event_ServerSocket= new ServerSocket(EVENT_PORT);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        CreateCMReceiver(cm_ServerSocket);
	        CreateUMReceiver(um_ServerSocket);
	        CreateEventReceiver(event_ServerSocket);
	       
	        
			BufferedReader consoleBr = new BufferedReader(new InputStreamReader(System.in));
			 while(true) {
				 //BroadCastToCommunicationManagers(consoleBr.readLine());
				 BroadCastToUpdateMangers(consoleBr.readLine());
			 }
	    }
	    //Launches  a thread whos job it is to listen for incoming Update Manager connection requests. 
	    //When one is detected, a new thread is launched that will deal with messages coming from that Update Manager.
	    public static void CreateUMReceiver(ServerSocket serverSocket){
	    	Runnable umServer = new Runnable()  {
				public void run() {
			    	Socket socket = null;
					while (true) {
			            try {
			                socket = serverSocket.accept();
			            } catch (IOException e) {
			                System.out.println("I/O error: " + e);
			            }
			            // new thread for a client
			            Thread t= new UmThread(socket);
			            t.start();
			            if(openConnections.stream().noneMatch(x->x.srID==0)) {
				            openConnections.add(new ConnectedStreamReasoner(0,null,t,null));	
			            }
			            else {
				            openConnections.stream().filter(x->x.srID==0).findFirst().get().umThread=t;
			            }
					}
				}
			};
			
			Thread umThread = new Thread (umServer);
			umThread.start();
			System.out.println("Started um Receiver Thread");
	    }
	    
	    public static void CreateCMReceiver (ServerSocket serverSocket) {
	    	Runnable cmServer= new Runnable()  {
				public void run() {
					Socket socket = null;
					while (true) {
			            try {
			                socket = serverSocket.accept();
			            } catch (IOException e) {
			                System.out.println("I/O error: " + e);
			            }
			            // new thread for a client
			            Thread t= new CmThread(socket);
			            t.start();
			            if(openConnections.stream().noneMatch(x->x.srID==0)) {
				            openConnections.add(new ConnectedStreamReasoner(0,t,null,null));	
			            }
			            else {
				            openConnections.stream().filter(x->x.srID==0).findFirst().get().cmThread=t;
			            }
					}
				}
			};
			Thread cmThread = new Thread (cmServer);
			cmThread.start();
			Printer.CustomPrint("Started cm Receiver Thread");
	    }
	    
	    public static void CreateEventReceiver(ServerSocket serverSocket) {
	    	Runnable eventServer= new Runnable()  {
				public void run() {
					Socket socket = null;
					while (true) {
			            try {
			                socket = serverSocket.accept();
			            } catch (IOException e) {
			                System.out.println("I/O error: " + e);
			            }
			            // new thread for a client
			            Thread t= new EventThread(socket);
			            t.start();
			            if(openConnections.stream().noneMatch(x->x.srID==0)) {
				            openConnections.add(new ConnectedStreamReasoner(0,null,null,t));	
			            }
			            else {
				            openConnections.stream().filter(x->x.srID==0).findFirst().get().cmThread=t;
			            }
					}
				}
			};
			Thread eventChannelThread = new Thread (eventServer);
			eventChannelThread.start();
			Printer.CustomPrint("Started Event Channel Receiver Thread");
	    }
	    
	    public static void BroadCastToCommunicationManagers(String message) {
	    	for(ConnectedStreamReasoner sr : openConnections) {
	    		((CmThread)sr.cmThread).pushMessage(message);
	    	}
	    }
	    
	    public static void BroadCastToUpdateMangers(String message) {
	    	for(ConnectedStreamReasoner sr : openConnections) {
	    		((UmThread)sr.umThread).pushMessage(message);
	    	}
	    }

}
