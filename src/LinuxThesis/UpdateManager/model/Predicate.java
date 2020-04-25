package model;

import java.util.List;

public class Predicate {
    public int Id;
    public String Name;
    public int NumberOfArguments;
    public String Type;
    public String Comment;
    public CommunicationMode PredicateDefaultCommunicationMode;
    public List<PredicateException> PredicateExceptions;

    public Predicate(int id, String name, int numberOfArguments, String type, String comment, CommunicationMode predicateDefaultCommunicationMode, List<PredicateException> exceptions)
    {
        Id = id;
        Name = name;
        NumberOfArguments = numberOfArguments;
        Type = type;
        Comment = comment;
        PredicateDefaultCommunicationMode = predicateDefaultCommunicationMode;
        PredicateExceptions = exceptions;
    }

    public int GetBurstBuffer(int exceptionId)
    {
        if (PredicateExceptions.stream().noneMatch(x->x.Id==exceptionId))
        {
        	return PredicateDefaultCommunicationMode.SameMessageDelay;
        }
        else{
        	return PredicateExceptions.stream().filter(x->x.Id==exceptionId).findFirst().get().ExceptionCommunicationMode.SameMessageDelay;
        }
    }

    public CommunicationMode GetCommunicationModeByPredicateException(int exceptionId)
    {
        if (PredicateExceptions.stream().noneMatch(x->x.Id==exceptionId))
        {
        	return PredicateDefaultCommunicationMode;
             
        }
        else {
        	return PredicateExceptions.stream().filter(x->x.Id==exceptionId).findFirst().get().ExceptionCommunicationMode;
        }
    }
    public int GetPredicateExceptionGivenArguments(List<String> arguments)
    {
        int exceptionId= -1;
        Boolean checkmark = false;
        for(PredicateException e : PredicateExceptions)
        {
            checkmark = true;
            for(ExceptionArgument a : e.ExceptionArguments)
            {
                if (!a.Values.contains(arguments.get(a.Number))){
                    checkmark = false;
                }
            }
            if (checkmark)
            {
                return(e.Id);
            }
        }

        return exceptionId;
    }
}
