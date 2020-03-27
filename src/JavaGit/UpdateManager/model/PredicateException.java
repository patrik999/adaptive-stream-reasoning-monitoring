package model;

import java.util.List;

public class PredicateException {
	public int Id; 
    public List<ExceptionArgument> ExceptionArguments;
    public CommunicationMode ExceptionCommunicationMode;

    public PredicateException(int id, List<ExceptionArgument> arguments, CommunicationMode exceptionCommunicationMode)
    {
        Id = id;
        ExceptionArguments = arguments;
        ExceptionCommunicationMode = exceptionCommunicationMode;
    }
}
