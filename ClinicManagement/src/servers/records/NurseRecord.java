package servers.records;

import java.io.Serializable;

public class NurseRecord extends Record implements  Serializable {

    private static final long serialVersionUID = 5806936105679036972L;
    private String designation;
    private String status;
    private String date;
    
    public NurseRecord(
            String id,
            String firstName, 
            String lastName, 
            String designation,
            String status,
            String statusDate)
    {
        super.setId(id);
        super.setFirstName(firstName);
        super.setLastName(lastName);
        this.designation = designation;
        this.status = status;
        this.date = statusDate;
    }
    
    public void setField(String fieldName, String value)
    {
        switch (fieldName.toLowerCase()) {
            case "id" :
                super.setId(value);
                this.setPersistenceStatus(true);
                this.setStatusMessage("Edited Id");
                break;
            case "firstname" :
                super.setFirstName(value);
                this.setPersistenceStatus(true);
                this.setStatusMessage("Edited First Name");
                break;
            case "lastname" :
                this.setPersistenceStatus(true);
                this.setStatusMessage("Edited Last Name");
                break;
            case "designation" :
                this.setPersistenceStatus(true);
                this.setStatusMessage("Edited Designation");
                break;
            case "status" :
                this.setPersistenceStatus(true);
                this.setStatusMessage("Edited Status");
                break;
            case "statusdate" :
                this.setPersistenceStatus(true);
                this.setStatusMessage("Edited Status Date");
                break;
            default :
                this.setPersistenceStatus(false);
                this.setStatusMessage("Field " + fieldName + " does not exist");
                break;
        }
    }
    public String getDesignation()
    {
        return this.designation;
    }
    
    public void setDesignation(String designation)
    {
        this.designation = designation;
    }
    
    public String getStatus()
    {
        return this.status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public String getDate()
    {
        return this.date;
    }
    
    public void setDate(String date)
    {
        this.date = date;
    }

	@Override
	public String toString() {
		return "Nurse Record : " + "ID: " + getId() +   " fname: " + getFirstName() + "lname: " + 
		super.getFirstName() + "designation: " + designation + "status: " + status + "statusdate:" + date+ "/n"
		+ "StatusMessage: " + getStatusMessage();
				
	}
}
