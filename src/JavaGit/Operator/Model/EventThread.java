package Model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import OperatorSetup.OperatorStartup;
import util.Printer;

public class EventThread extends Thread{
	protected Socket socket;

    public EventThread(Socket clientSocket) {
        this.socket = clientSocket;
        Printer.CustomPrint("Event Channel connected");
    }

    public void run() {
    	int counter=0;
        InputStream inp = null;
        BufferedReader brinp = null;
        try {
            inp = socket.getInputStream();
            brinp = new BufferedReader(new InputStreamReader(inp));
        } catch (IOException e) {
            return;
        }
        String line;
        while (true) {
            try {
                line = brinp.readLine();
                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
                    socket.close();
                    return;
                } else {
                	synchronized(this){
                		Printer.CustomPrint("Event Channel says: "+line);
                		counter++;
                		if(counter==5) {
                			Printer.CustomPrint("Setting to push 3s delay");
                			OperatorStartup.BroadCastToUpdateMangers("interfaceCommand(editPredicateCommunication,0, push, true, 3)");
                		}
                		if(counter==10) {
                			Printer.CustomPrint("Setting to push 10s delay");
                			OperatorStartup.BroadCastToUpdateMangers("interfaceCommand(editPredicateCommunication,0, push, true, 10)");
                		}
                		if(counter==15) {
                			Printer.CustomPrint("Setting to push 0s delay");
                			OperatorStartup.BroadCastToUpdateMangers("interfaceCommand(editPredicateCommunication,0, push, false, 5)");
                		}
                		if(counter==20) {
                			Printer.CustomPrint("Setting to push 5s delay");
                			OperatorStartup.BroadCastToUpdateMangers("interfaceCommand(editPredicateCommunication,0, push, true, 5)");
                		}
                	}
                	
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
