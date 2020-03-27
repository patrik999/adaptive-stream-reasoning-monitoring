package model;
import org.w3c.dom.*;
public class CommunicationMode {
    public String Mode;
    public Boolean Buffered;
    public int SameMessageDelay;
    
    public CommunicationMode(String mode, Boolean buffered, int burstBuffer)
    {
        Mode = mode;
        Buffered = buffered;
        SameMessageDelay = burstBuffer;
    }

    public CommunicationMode (Node xmlNode)
    {
        Mode = ((Element)xmlNode).getElementsByTagName("mode").item(0).getTextContent();
        Buffered = Boolean.parseBoolean(((Element)xmlNode).getElementsByTagName("buffered").item(0).getTextContent());
        SameMessageDelay = Integer.parseInt(((Element)xmlNode).getElementsByTagName("delay").item(0).getTextContent());
    }

    public String XmlOutput(int numberOfIndents)
    {
    	String xmlOutput = "";
        xmlOutput += "\t".repeat(numberOfIndents)+ "<mode>" + Mode + "</mode>\n";
        xmlOutput += "\t".repeat(numberOfIndents)+ "<buffered>" + Buffered + "</buffered>\n";
        xmlOutput += "\t".repeat(numberOfIndents)+ "<delay>" + SameMessageDelay + "</delay>\n";
        return xmlOutput;
    }
}
