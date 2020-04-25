package communicationManagerSetup;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import receivers.*;
import util.EventChannel;
import util.InterfaceReader;
import util.Printer;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class CmStartup {
	public static String exp= "2";
	public static String test="3";
	public static String startDirectory=System.getProperty("user.dir");
	public static String interfacePath = startDirectory.substring(0, startDirectory.lastIndexOf("/"))+ "/Exp"+exp+"Test"+test+"/interface_exp"+exp+"_test"+test+".xml";

	public static void main(String[] args) throws Exception {
		Printer.SetStartTime(LocalDateTime.now().plusSeconds(-1));
		InterfaceReader.LoadInterface(interfacePath);
		
		//FOR launching the OLD event receiver
        //new EventReceiver().start();
		
		Thread t = new Thread(() -> {
			try {
				startWsServer();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		t.start();
				
        new RequestReceiver().start();
        new UpdateReceiver().start();
        EventChannel.init();
		
		System.in.read();
	}
	
	public static void startWsServer() throws Exception {
		//JEtty
		Server server = new Server(1976);
        WebSocketHandler wsHandler = new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory factory) {
                factory.register(EventReceiver.class);
            }
        };
        server.setHandler(wsHandler);

        server.start();
        server.join();
		//JEtty
        
        Printer.CustomPrint("Started Event Receiver on Communication Manager");
	}

}
