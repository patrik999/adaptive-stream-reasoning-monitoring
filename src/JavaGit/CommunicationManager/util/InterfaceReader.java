package util;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import editors.InterfaceEditor;

public final class InterfaceReader {
	    
    public static void  LoadInterface() throws ParserConfigurationException, SAXException, IOException
    {
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
        String startDirectory=System.getProperty("user.dir");
        String destPath= startDirectory.substring(0,startDirectory.lastIndexOf("\\"))+"\\Test_File.xml";
        Document xDoc = builder.parse(new File( destPath ));
		
		InterfaceEditor.LoadInterfaceEditor(xDoc);
		
    }
}
