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

public class CmStartup {
	

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		Printer.SetStartTime(LocalDateTime.now());
		InterfaceReader.LoadInterface();
        new EventReceiver().start();
        new RequestReceiver().start();
        new UpdateReceiver().start();
        EventChannel.init();
		
		System.in.read();
	}

}
