package editors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.w3c.dom.*;

import model.CommunicationMode;
import model.ExceptionArgument;
import model.Parameter;
import model.Predicate;
import model.PredicateException;
import model.Settings;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class InterfaceEditor {
	public static Settings Settings;
	public static List<Parameter> Parameters;
	public static List<Predicate> Predicates;

	public static void LoadInterfaceEditor(Document xDoc) {
		Settings streamReasonerSettings = new Settings();
		List<Parameter> parameters = new ArrayList<Parameter>();
		List<Predicate> predicates = new ArrayList<Predicate>();

		// Extract stream reasoner settings
		streamReasonerSettings.SrId = Integer.parseInt(xDoc.getElementsByTagName("SR_ID").item(0).getTextContent());
		streamReasonerSettings.DefaultCommunication = new CommunicationMode(
				xDoc.getElementsByTagName("sr_default").item(0));

		// Extract parameters
		NodeList xmlParameters = xDoc.getElementsByTagName("Parameter");
		for (Node n : iterable(xmlParameters)) {
			Element node = (Element) n;
			parameters.add(new Parameter(Integer.parseInt(node.getElementsByTagName("id").item(0).getTextContent()),
					node.getElementsByTagName("name").item(0).getTextContent(),
					node.getElementsByTagName("type").item(0).getTextContent(),
					node.getElementsByTagName("value").item(0).getTextContent(),
					node.getElementsByTagName("comment").item(0).getTextContent()));
		}

		// Extract predicates
		NodeList xmlPredicates = xDoc.getElementsByTagName("Predicate");
		for (Node n : iterable(xmlPredicates)) {
			Element node = (Element) n;
			List<PredicateException> exceptions = new ArrayList<PredicateException>();
			for (Node en : iterable(node.getElementsByTagName("exception"))) {
				Element exceptionNode = (Element) en;
				// For each exception extracts its arguments into a list of ExceptionArgument
				List<ExceptionArgument> arguments = new ArrayList<ExceptionArgument>();
				for (Node an : iterable(exceptionNode.getElementsByTagName("argument"))) {
					Element argumentNode = (Element) an;
					arguments.add(new ExceptionArgument(Integer.parseInt(argumentNode.getAttribute("nr")),
							Arrays.asList(argumentNode.getTextContent().split(","))));
				}

				// Create the exception object
				exceptions.add(new PredicateException(
						Integer.parseInt(exceptionNode.getElementsByTagName("id").item(0).getTextContent()), arguments,
						new CommunicationMode(exceptionNode)));
			}

			// Create the Predicate object
			predicates.add(new Predicate(Integer.parseInt(node.getElementsByTagName("id").item(0).getTextContent()),
					node.getElementsByTagName("name").item(0).getTextContent(),
					Integer.parseInt(node.getElementsByTagName("number_of_arguments").item(0).getTextContent()),
					node.getElementsByTagName("type").item(0).getTextContent(),
					node.getElementsByTagName("comment").item(0).getTextContent(),
					new CommunicationMode(node.getElementsByTagName("default").item(0)), exceptions));
		}

		Settings = streamReasonerSettings;
		Parameters = parameters;
		Predicates = predicates;
	}

	public static void AddParameter(int id, String name, String type, String value, String comment) {
		Parameters.add(new Parameter(id, name, type, value, comment));
	}

	public static void AddPredicateBase(int id, String name, int numberOfArguments, String type, String comment, String mode,
			Boolean buffered, int delay) {
		Predicates.add(new Predicate(id, name, numberOfArguments, type, comment,
				new CommunicationMode(mode, buffered, delay), new ArrayList<PredicateException>()));
	}

	public static void AddException(int predicateId, int id, String arguments, String mode, Boolean buffered, int delay) throws Exception
    {
        if(Predicates.stream().noneMatch(x -> x.Id == predicateId))
        {
            throw new Exception("No Predicate with id: " + predicateId + " found.");
        }
        else
        {
            List<ExceptionArgument> argumentsList = new ArrayList<ExceptionArgument>();
            Pattern regex= Pattern.compile("\\(.*?\\]\\)");
            
            Matcher m = regex.matcher(arguments);

            while(m.find()) {
            	regex= Pattern.compile("[0-9]+");
            	String singleArgument = m.group(0);
            	Matcher m2=regex.matcher(singleArgument);
            	m2.find();
            	String argumentId = m2.group(0);
            	int argumentNr = Integer.parseInt(argumentId);
            	
            	regex= Pattern.compile("\\[.*\\]");
            	m2=regex.matcher(singleArgument);
            	m2.find();
            	String commaSeparatedValues=m2.group(0);
            	List<String> argumentValues= Arrays.asList(commaSeparatedValues.replace("[", "").replace("]", "").split(",")).stream().map(String::trim).collect(Collectors.toList());

                argumentsList.add(new ExceptionArgument(argumentNr, argumentValues));
            }
            Predicates.stream().filter(x -> x.Id == predicateId).findFirst().get().PredicateExceptions
            .add(new PredicateException(id, argumentsList, new CommunicationMode(mode, buffered, delay)));
        }
    }

	public static void DeleteParameter(int parameterId) {
		Parameters.removeIf(x -> x.Id == parameterId);
	}

	public static void DeletePredicate(int predicateId) {
		Predicates.removeIf(x -> x.Id == predicateId);
	}

	public static void DeleteException(int predicateId, int exceptionId) throws Exception {
		if (Predicates.stream().noneMatch(x -> x.Id == predicateId)) {
			throw new Exception("No Predicate with id: " + predicateId + " found.");
		} else {
			if (Predicates.stream().filter(x -> x.Id == predicateId).findFirst().get().PredicateExceptions.stream()
					.noneMatch(x -> x.Id == exceptionId)) {
				throw new Exception(
						"No Exception with id: " + exceptionId + " found for predicate with id: " + predicateId + ".");
			} else {
				Predicates.stream().filter(x -> x.Id == predicateId).findFirst().get().PredicateExceptions
						.removeIf(x -> x.Id == exceptionId);
			}
		}
	}

	public static void EditParameterValue(int parameterId, String value) throws Exception {
		if (Parameters.stream().noneMatch(x -> x.Id == parameterId)) {
			throw new Exception("No Parameter with id: " + parameterId + " found.");
		} else {
			Parameters.stream().filter(x -> x.Id == parameterId).findFirst().get().Value = value;
		}
	}

	public static void EditPredicateCommunication(int predicateId, String mode, Boolean buffered, int delay) throws Exception {
		if (Predicates.stream().noneMatch(x -> x.Id == predicateId)) {
			throw new Exception("No Predicate with id: " + predicateId + " found.");
		} else {
			Predicate toEdit = Predicates.stream().filter(x -> x.Id == predicateId).findFirst().get();
			toEdit.PredicateDefaultCommunicationMode = new CommunicationMode(mode, buffered, delay);
		}
	}

	public static void SaveChanges() throws IOException
    {
        String xmlOutput = "";
        int numberOfIndents = 0;
        
        xmlOutput += "\t".repeat(numberOfIndents)+ "<InterfaceDescription>\n";
        numberOfIndents++;
        xmlOutput += "\t".repeat(numberOfIndents)+ "<SR_ID>" + Settings.SrId + "</SR_ID>\n";

        xmlOutput += "\t".repeat(numberOfIndents)+ "<Parameters>\n";
        numberOfIndents++;

        for (Parameter p : Parameters)
        {
            xmlOutput += "\t".repeat(numberOfIndents)+ "<Parameter>\n";
            numberOfIndents++;
            xmlOutput += "\t".repeat(numberOfIndents)+ "<id>" + p.Id + "</id>\n";
            xmlOutput += "\t".repeat(numberOfIndents)+ "<name>" + p.Name + "</name>\n";
            xmlOutput += "\t".repeat(numberOfIndents)+ "<type>" + p.Type + "</type>\n";
            xmlOutput += "\t".repeat(numberOfIndents)+ "<value>" + p.Value + "</value>\n";
            xmlOutput += "\t".repeat(numberOfIndents)+ "<comment>" + p.Comment + "</comment>\n";
            numberOfIndents--;
            xmlOutput += "\t".repeat(numberOfIndents)+ "</Parameter>\n";
        }
        numberOfIndents--;
        xmlOutput += "\t".repeat(numberOfIndents)+ "</Parameters>\n";

        xmlOutput += "\t".repeat(numberOfIndents)+ "<Data>\n";
        numberOfIndents++;
        xmlOutput += "\t".repeat(numberOfIndents)+ "<sr_default>\n";
        numberOfIndents++;
        xmlOutput +=  Settings.DefaultCommunication.XmlOutput(numberOfIndents);
        numberOfIndents--;
        xmlOutput += "\t".repeat(numberOfIndents)+ "</sr_default>\n";

        for (Predicate p : Predicates)
        {
            xmlOutput += "\t".repeat(numberOfIndents)+ "<Predicate>\n";
            numberOfIndents++;
            xmlOutput += "\t".repeat(numberOfIndents)+ "<id>" + p.Id + "</id>\n";
            xmlOutput += "\t".repeat(numberOfIndents)+ "<name>" + p.Name + "</name>\n";
            xmlOutput += "\t".repeat(numberOfIndents)+ "<number_of_arguments>" + p.NumberOfArguments + "</number_of_arguments>\n";
            xmlOutput += "\t".repeat(numberOfIndents)+ "<type>" + p.Type + "</type>\n";
            xmlOutput += "\t".repeat(numberOfIndents)+ "<comment>" + p.Comment + "</comment>\n";

            xmlOutput += "\t".repeat(numberOfIndents)+ "<communication>\n";
            numberOfIndents++;
            xmlOutput += "\t".repeat(numberOfIndents)+ "<default>\n";
            numberOfIndents++;
            xmlOutput += p.PredicateDefaultCommunicationMode.XmlOutput(numberOfIndents);
            numberOfIndents--;
            xmlOutput += "\t".repeat(numberOfIndents)+ "</default>\n";

            xmlOutput += "\t".repeat(numberOfIndents)+ "<exceptions>\n";
            numberOfIndents++;

            for (PredicateException ex : p.PredicateExceptions)
            {
                xmlOutput += "\t".repeat(numberOfIndents)+ "<exception>\n";
                numberOfIndents++;
                xmlOutput += "\t".repeat(numberOfIndents)+ "<id>" + ex.Id + "</id>\n";
                xmlOutput += "\t".repeat(numberOfIndents)+ "<arguments>\n";
                numberOfIndents++;

                for (ExceptionArgument arg : ex.ExceptionArguments)
                {
                    xmlOutput += "\t".repeat(numberOfIndents)+ "<argument nr='"+arg.Number+"'>"+String.join(",",arg.Values)+ "</argument>\n";
                }
                numberOfIndents--;
                xmlOutput += "\t".repeat(numberOfIndents)+ "</arguments>\n";
                xmlOutput +=ex.ExceptionCommunicationMode.XmlOutput(numberOfIndents);
                numberOfIndents--;
                xmlOutput += "\t".repeat(numberOfIndents)+ "</exception>\n";
            }
            numberOfIndents--;
            xmlOutput += "\t".repeat(numberOfIndents)+ "</exceptions>\n";
            numberOfIndents--;
            xmlOutput += "\t".repeat(numberOfIndents)+ "</communication>\n";
            numberOfIndents--;
            xmlOutput += "\t".repeat(numberOfIndents)+ "</Predicate>\n";
        }
        numberOfIndents--;
        xmlOutput += "\t".repeat(numberOfIndents)+ "</Data>\n";
        numberOfIndents--;
        xmlOutput += "\t".repeat(numberOfIndents)+ "</InterfaceDescription>";

        String startDirectory=System.getProperty("user.dir");
        String destPath= startDirectory.substring(0,startDirectory.lastIndexOf("\\"))+"\\Test_File.xml";
        
        FileWriter writer = new FileWriter(destPath,false);
        writer.write(xmlOutput);
        writer.close();

        System.out.println("Changes saved.");
        PingCommunicationManager();
    }

	public static void PingCommunicationManager() throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", 1979);
		
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
    	out.writeBytes("alert(interface_updated)" + "\n");
        out.flush();

		socket.close();
		//CommunicationManagerService.WCFCommunicationManagerServiceClient proxy = new CommunicationManagerService.WCFCommunicationManagerServiceClient();
		//proxy.UpdateInterface();
	}

	public static Iterable<Node> iterable(final NodeList nodeList) {
		return () -> new Iterator<Node>() {

			private int index = 0;

			@Override
			public boolean hasNext() {
				return index < nodeList.getLength();
			}

			@Override
			public Node next() {
				if (!hasNext())
					throw new NoSuchElementException();
				return nodeList.item(index++);
			}
		};
	}
}
