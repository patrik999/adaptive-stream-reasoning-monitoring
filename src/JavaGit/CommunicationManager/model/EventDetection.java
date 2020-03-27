package model;

import java.util.Date;
import java.util.List;

public class EventDetection {
    public int PredicateId;
    public List<String> Arguments;
    public Date TimeOccurred;
    public String Location;
    public int ExceptionId;

    public EventDetection(int predicateId, Date timeOccurred, String location, List<String> arguments,  int exceptionId)
    {
        PredicateId = predicateId;
        TimeOccurred = timeOccurred;
        Location = location;
        Arguments = arguments;
        ExceptionId = exceptionId;
    }

}
