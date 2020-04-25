package editors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import model.Parameter;
import util.Printer;

public class StreamReasonerEditor {
	public static long pid = -1;
	public static String startDirectory;
	public static String reasonerTemplate;
	public static String reasonerLive;
	public static String status;
	public static String lock="lock";

	public static void addRule(int duration, String rule) throws IOException, InterruptedException {
		// Read all lines and store them into an array
		Map<Integer, List<String>> lines = new HashMap<Integer, List<String>>();
		

		synchronized (lock) {
			FileInputStream fstream = new FileInputStream(reasonerTemplate);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

			String strLine;
			int counter = 0;
			List<String> temp = new ArrayList<String>();
			while ((strLine = br.readLine()) != null) {
				strLine = strLine.trim();
				temp.add(strLine);
				if (strLine.endsWith(".")) {
					lines.put(counter, temp);
					counter++;
					temp = new ArrayList<String>();
				}
			}
			br.close();

			// Write contents back to file
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(reasonerTemplate), false));
			bw.write(rule);
			bw.newLine();
			for (Map.Entry<Integer, List<String>> ruleLines : lines.entrySet()) {
				for (String line : ruleLines.getValue()) {
					bw.write(line);
					bw.newLine();
				}
			}
			bw.close();

		}
		if (duration != -1) {
			Thread t = new Thread(() -> {
				try {
					Thread.sleep(duration * 1000);
					deleteRule(-1, rule);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			t.start();
		}
		parameterAndRestart();

		Printer.CustomPrint("Rule " + rule + " has been asserted.");
	}

	public static void deleteRule(int duration, String rule) throws IOException, InterruptedException {
		// Read all lines and store them into an array

		Map<Integer, List<String>> lines = new HashMap<Integer, List<String>>();
		

		synchronized (lock) {
			FileInputStream fstream = new FileInputStream(reasonerTemplate);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

			String strLine;
			int counter = 0;
			List<String> temp = new ArrayList<String>();
			while ((strLine = br.readLine()) != null) {
				strLine = strLine.trim();
				temp.add(strLine);
				if (strLine.endsWith(".")) {
					lines.put(counter, temp);
					counter++;
					temp = new ArrayList<String>();
				}
			}
			br.close();

			// Iterate through lines and look for the rule
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(reasonerTemplate), false));

			for (Map.Entry<Integer, List<String>> ruleLines : lines.entrySet()) {
				boolean matchFound = false;
				int matchFoundAt = -1;
				List<String> singleLines = ruleLines.getValue().stream().filter(x -> true).collect(Collectors.toList());
				Collections.reverse(singleLines);

				for (int i = 0; i < singleLines.stream().count(); i++) {
					String testString = "";
					for (int j = i; j >= 0; j--) {
						testString += singleLines.get(j);
						if (j != 0)
							testString += "\r\n";
					}
					if (testString.equals(rule)) {
						matchFound = true;
						matchFoundAt = (int) singleLines.stream().count() - 1 - i;
					}
				}

				if (!matchFound) {
					for (String line : ruleLines.getValue()) {
						bw.write(line);
						bw.newLine();
					}
				} else {
					for (int i = 0; i < ruleLines.getValue().stream().count(); i++) {
						if (i < matchFoundAt) {
							bw.write(ruleLines.getValue().get(i));
							bw.newLine();
						}

					}
				}
			}
			bw.close();
		}
		if (duration != -1) {
			Thread t = new Thread(() -> {
				try {
					Thread.sleep(duration * 1000);
					addRule(-1, rule);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			t.start();
		}
		parameterAndRestart();
		Printer.CustomPrint("Rule " + rule + " has been removed.");
	}

	public static void disableRule(int duration, String rule) throws IOException, InterruptedException {
		// Read all lines and store them into an array

		Map<Integer, List<String>> lines = new HashMap<Integer, List<String>>();
		FileInputStream fstream = new FileInputStream(reasonerTemplate);

		synchronized (fstream) {

			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

			String strLine;
			int counter = 0;
			List<String> temp = new ArrayList<String>();
			while ((strLine = br.readLine()) != null) {
				strLine = strLine.trim();
				temp.add(strLine);
				if (strLine.endsWith(".")) {
					lines.put(counter, temp);
					counter++;
					temp = new ArrayList<String>();
				}
			}
			br.close();

			// Iterate through lines and look for the rule
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(reasonerTemplate), false));

			for (Map.Entry<Integer, List<String>> ruleLines : lines.entrySet()) {
				if (ruleLines.getValue().stream().noneMatch(x -> x.contains(rule))) {
					for (String line : ruleLines.getValue()) {
						bw.write(line);
						bw.newLine();
					}
				} else {
					for (String line : ruleLines.getValue()) {
						if (!line.equals(""))
							bw.write("%" + line);
						bw.newLine();
					}
				}
			}
			bw.close();
		}
		if (duration != -1) {
			Thread t = new Thread(() -> {
				try {
					Thread.sleep(duration * 1000);
					enableRule(-1, rule);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			t.start();
		}
		parameterAndRestart();
		Printer.CustomPrint("Rule " + rule + " has been disabled.");
	}

	public static void enableRule(int duration, String rule) throws IOException, InterruptedException {
		// Read all lines and store them into an array

		Map<Integer, List<String>> lines = new HashMap<Integer, List<String>>();
		FileInputStream fstream = new FileInputStream(reasonerTemplate);

		synchronized (fstream) {
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

			String strLine;
			int counter = 0;
			List<String> temp = new ArrayList<String>();
			while ((strLine = br.readLine()) != null) {
				strLine = strLine.trim();
				temp.add(strLine);
				if (strLine.endsWith(".")) {
					lines.put(counter, temp);
					counter++;
					temp = new ArrayList<String>();
				}
			}
			br.close();
			// Iterate through lines and look for the rule
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(reasonerTemplate), false));

			for (Map.Entry<Integer, List<String>> ruleLines : lines.entrySet()) {
				if (ruleLines.getValue().stream().noneMatch(x -> x.contains(rule))) {
					for (String line : ruleLines.getValue()) {
						bw.write(line);
						bw.newLine();
					}
				} else {
					for (String line : ruleLines.getValue()) {
						bw.write(line.replace("%", ""));
						bw.newLine();
					}
				}
			}
			bw.close();
		}
		if (duration != -1) {
			Thread t = new Thread(() -> {
				try {
					Thread.sleep(duration * 1000);
					disableRule(-1, rule);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			t.start();
		}
		parameterAndRestart();
		Printer.CustomPrint("Rule " + rule + " has been enabled.");
	}

	private static void substituteParameter() throws IOException {

		FileInputStream fstream = new FileInputStream(reasonerTemplate);

		synchronized (fstream) {
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

			String strLine;
			boolean added = false;
			List<String> temp = new ArrayList<String>();

			while ((strLine = br.readLine()) != null) {
				added = false;
				Matcher m = Pattern.compile("!.*?!").matcher(strLine);
				while (m.find()) {
					String parameterName = m.group().substring(1, m.group().length() - 1);
					Parameter p = InterfaceEditor.Parameters.stream().filter(x -> x.Name.equals(parameterName))
							.findFirst().get();
					temp.add(strLine.replace(m.group(), p.Value));
					added = true;
				}
				if (added == false) {
					temp.add(strLine);
				}
			}
			br.close();

			// Iterate through lines and look for the rule
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(reasonerLive), false));

			for (String line : temp) {
				bw.write(line);
				bw.newLine();
			}
			bw.close();
		}
		Printer.CustomPrint("Parameters substituted");
	}

	public static void start(String templateFile, int sleep) throws IOException {
		Process p;
		startDirectory = System.getProperty("user.dir");
		// Windows Directory
		if (startDirectory.contains("\\")) {
			reasonerTemplate = startDirectory.substring(0, startDirectory.lastIndexOf("\\")) + "/" + templateFile;
			reasonerLive = reasonerTemplate.replace("_template", "");
		} else {
			// Ubuntu Directory
			reasonerTemplate = startDirectory.substring(0, startDirectory.lastIndexOf("/")) + "/" + templateFile;
			reasonerLive = reasonerTemplate.replace("_template", "");
		}
		
		substituteParameter();
		String temp=startDirectory.substring(0, startDirectory.lastIndexOf("/"));
		String plugins = temp.substring(0, temp.lastIndexOf("/"))+"/adaptive-stream-reasoning-monitoring/stream-reasoner/plugins/";
		// String clienTest =
		// "/home/stefan/Desktop/install/adaptive-stream-reasoning-monitoring-master/stream-reasoner/pgms/test.hex";
		String clienTest = reasonerLive;

		try {
			p = Runtime.getRuntime().exec("acthex" + " --flpcheck=none --pluginpath=" + plugins + " \n"
					+ "--plugin=stream-reasoning-plugin " + clienTest + " --hidemodels --sleep " + sleep);
			////////////////////////////////
			// p.waitFor();
			// if(p.exitValue()!=0) {
			// try (final BufferedReader b = new BufferedReader(new
			//////////////////////////////// InputStreamReader(p.getErrorStream()))) {
			// String line;
			// if ((line = b.readLine()) != null)
			// System.out.println(line);
			// } catch (final IOException e) {
			// e.printStackTrace();
			// }
			// }
			///////////////////////////////
			pid = p.pid();
			Printer.CustomPrint("Stream reasoner started on PID: " + pid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		status = "running";
	}

	private static void sendRestart() throws IOException, InterruptedException {
		if (status.equals("restarting")) {

		} else {
			status = "restarting";
			Thread.sleep(100);
			Process killProcess;
			killProcess = Runtime.getRuntime().exec("kill -SIGHUP " + pid);
			Printer.CustomPrint("Restart message sent to Stream Reasoner");
			status = "running";
		}

	}

	public static void parameterAndRestart() throws IOException, InterruptedException {
		substituteParameter();
		sendRestart();
	}
}
