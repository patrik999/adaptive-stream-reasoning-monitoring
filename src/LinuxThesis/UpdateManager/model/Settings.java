package model;

public class Settings {
	public int SrId;
	public CommunicationMode DefaultCommunication;
	
	public Settings() {
		
	}
    public Settings(int srId, CommunicationMode defaultCommunication)
    {
        SrId = srId;
        DefaultCommunication = defaultCommunication;
    }
}
