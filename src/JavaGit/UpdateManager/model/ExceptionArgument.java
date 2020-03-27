package model;

import java.util.List;

public class ExceptionArgument {
    public int Number;
    //List of accepted values for the parameter identified by the number
    public List<String> Values;

    public ExceptionArgument(int number, List<String> values)
    {
        Number = number;
        Values = values;
    }
}
