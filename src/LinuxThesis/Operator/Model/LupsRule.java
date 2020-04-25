package Model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.Printer;

public class LupsRule {
	public String originalPolicy;
	public String action;
	public String rule;
	public int duration=-1;
	public String condition;
	public String type;
		
	public boolean CheckLupsCondition(String eventMessage) {
		String eventName= eventMessage.substring(eventMessage.indexOf("(")+1,eventMessage.indexOf(","));
		String arguments= eventMessage.substring(eventMessage.indexOf("[")+1,eventMessage.indexOf("]"));
		String toCompare=eventName+"("+arguments+")";
		
		boolean result= condition.equals(toCompare);
		if (result) Printer.CustomPrint("The event message "+eventMessage+" triggered the policy "+originalPolicy);
		return result; 
	}
	public LupsRule(String Lups) {
		originalPolicy=Lups;
		//execute for 60 rule editParameterValue(1,l50) when Speeding(l50)
		if(Lups.contains("execute")) {
			type="interface";
			Matcher timeMatcher = Pattern.compile("for (\\d*\\s)").matcher(Lups);
			Matcher commandMatcher=Pattern.compile("rule .*? when").matcher(Lups);
			Matcher conditionMatcher = Pattern.compile("when .*").matcher(Lups);
			
			while(timeMatcher.find()) {
				String timePart=timeMatcher.group().trim();
				duration= Integer.parseInt(timePart.split(" ")[1]);
			}
			while (commandMatcher.find()) {
				String commandPart= commandMatcher.group().trim();
				rule = commandPart.replace("rule", "").replace("when", "").trim();
			}
			while(conditionMatcher.find()) {
				String conditionPart=conditionMatcher.group().trim();
				condition = conditionPart.substring(conditionPart.indexOf("when ")+5,conditionPart.length());
			}
		}
		
		//assert for 3 rule trafficJam(X)<-accident(X) when TrafficJam(U6, Karlsplatz)
		else {
			type="sr";
			action = Lups.split(" ")[0];
			Matcher timeMatcher = Pattern.compile("for (\\d*\\s)").matcher(Lups);
			Matcher ruleMatcher = Pattern.compile("rule .*? when",Pattern.DOTALL).matcher(Lups);
			Matcher conditionMatcher = Pattern.compile("when .*").matcher(Lups);

			while(timeMatcher.find()) {
				String timePart=timeMatcher.group().trim();
				duration= Integer.parseInt(timePart.split(" ")[1]);
			}
			while (ruleMatcher.find()) {
				String rulePart= ruleMatcher.group().trim();
				rule = rulePart.replace("rule", "").replace("when", "").trim();
			}
			while(conditionMatcher.find()) {
				String conditionPart=conditionMatcher.group().trim();
				condition = conditionPart.substring(conditionPart.indexOf("when ")+5,conditionPart.length());
			}
		}
			
	}
	
	public String TranslateToUmCommand() {
		String umCommand ="";
		if(type.equals("interface")) {
			umCommand+="interfaceCommand(";
			umCommand+=rule.substring(0, rule.indexOf("("));
			umCommand+="^";
			umCommand+=duration;
			umCommand+="^";
			umCommand+=rule.substring(rule.indexOf("(")+1,rule.lastIndexOf(")")).replace(",", "^");
			umCommand+=")";
		}
		else {
			umCommand+="srCommand(";
			umCommand+=action;
			umCommand+="^";
			umCommand+=duration;
			umCommand+="^";
			umCommand+=rule;
			umCommand+=")";
		}
		return umCommand;
		
	}
}
