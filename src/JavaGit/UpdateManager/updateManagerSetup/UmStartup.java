package updateManagerSetup;

import org.w3c.dom.*;

import editors.InterfaceEditor;
import receivers.CommandReceiver;

import javax.xml.parsers.*;
import java.io.*;

public class UmStartup {

	public static void main(String[] args) throws Exception {

		//Load interface file
        String startDirectory=System.getProperty("user.dir");
        String destPath= startDirectory.substring(0,startDirectory.lastIndexOf("\\"))+"\\Test_File.xml";
        
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document xDoc = builder.parse(new File( destPath ));
		
        InterfaceEditor.LoadInterfaceEditor(xDoc);
        System.out.println(java.time.LocalTime.now()+": "+"Interface loaded");
        
        new CommandReceiver().run();
        
        System.in.read();
        
		
		

	}

}
