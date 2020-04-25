package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Printer {
	public static LocalDateTime StartTime;
	public static void SetStartTime(LocalDateTime startTime) {
		StartTime=startTime;
	}
	public static void CustomPrint(String message) {
		long time= ChronoUnit.SECONDS.between(StartTime, LocalDateTime.now());
		System.out.println(time+": "+message);
	}
}
