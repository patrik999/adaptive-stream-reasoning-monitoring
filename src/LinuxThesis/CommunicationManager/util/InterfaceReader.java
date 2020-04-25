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

	public static void LoadInterface(String interfacePath) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		String startDirectory = System.getProperty("user.dir");


		Document xDoc = builder.parse(new File(interfacePath));
		InterfaceEditor.LoadInterfaceEditor(xDoc);
	}
}
