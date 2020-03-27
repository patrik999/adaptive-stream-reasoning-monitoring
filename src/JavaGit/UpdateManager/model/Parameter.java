package model;

public class Parameter {
    public int Id;
    public String Name;
    public String Type;
    public String Value;
    public String Comment;
    
    public Parameter(int id, String name, String type, String value, String comment)
    {
        Id = id;
        Name = name;
        Type = type;
        Value = value;
        Comment = comment;
    }
}
