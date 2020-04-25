package updateManagerSetup;

import org.w3c.dom.*;

import editors.InterfaceEditor;
import editors.StreamReasonerEditor;
import receivers.CommandReceiver;
import util.Printer;

import javax.xml.parsers.*;
import java.io.*;
import java.time.LocalDateTime;

public class UmStartup {
	public static String exp= "2";
	public static String test="3";
	public static String startDirectory=System.getProperty("user.dir");
	public static String interfacePath = startDirectory.substring(0, startDirectory.lastIndexOf("/"))+ "/Exp"+exp+"Test"+test+"/interface_exp"+exp+"_test"+test+".xml";
	public static String streamReasonerPath="Exp"+exp+"Test"+test+"/stream_reasoner_exp"+exp+"_test"+test+"_template.hex";
	public static int streamReasonerSleep=1000;
	public static void main(String[] args) throws Exception {
		
		Printer.SetStartTime(LocalDateTime.now());
        
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document xDoc = builder.parse(new File( interfacePath ));
		
        InterfaceEditor.LoadInterfaceEditor(xDoc);
        Printer.CustomPrint("Interface loaded");
        
        Thread.sleep(3000);
        //StreamReasonerEditor.start("encoding_stream_ws_template.hex",2000);
        //StreamReasonerEditor.start("encoding_stream_ws_exp_1_test2_template.hex",1000);
        StreamReasonerEditor.start(streamReasonerPath,streamReasonerSleep);
        new CommandReceiver().run();
        
        System.in.read();
        
	}

}
