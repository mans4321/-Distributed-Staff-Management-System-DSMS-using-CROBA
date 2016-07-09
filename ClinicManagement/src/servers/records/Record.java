package servers.records;

import java.io.Serializable;

public abstract class Record implements RecordInterface, Serializable {
    
    private static final long serialVersionUID = 1L;
    private String id;
    private String firstName;
    private String lastName;
    private Character lastNameIndex;
    private String statusMessage;
    private boolean persistenceStatus;
    
    abstract void setField(String fieldName, String value);
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getId()
    {
        return this.id;
    }
    
    public void setFirstName(String name)
    {
        this.firstName = name;
    }
    public String getFirstName()
    {
    	return this.firstName;	
    }
    public void setLastName(String name)
    {
    	this.lastName = name;
    }
    public String getLastName()
    {
    	return this.lastName;
    }
    public void setLastNameIndex()
    {
    	this.lastNameIndex = lastName.toUpperCase().charAt(0);
    }
    public Character getLastNameIndex()
    {
    	if (this.lastNameIndex == null) {
    		setLastNameIndex();
    	}
    	return this.lastNameIndex;
    }

    public String getStatusMessage() 
    {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) 
    {
        this.statusMessage = statusMessage;
    }
    
    public void setPersistenceStatus(boolean status)
    {
        this.persistenceStatus = status;
    }
    
    public boolean isSuccessful() 
    {
        return this.persistenceStatus;
    }
    
    public abstract String toString();

}
