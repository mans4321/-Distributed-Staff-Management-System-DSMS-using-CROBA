package servers.records;

import java.io.Serializable;

public class DoctorRecord extends Record implements  Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String address;
    private String phone;
    private String specialization;
    private String location;
    
    public DoctorRecord(
            String id,
            String firstName,
            String lastName,
            String address,
            String phone,
            String specialization,
            String location)
    {
        
        super.setId(id);
        super.setFirstName(firstName);
        super.setLastName(lastName);
        this.address = address;
        this.phone = phone;
        this.specialization = specialization;
        this.location = location;
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
                super.setLastName(value);
                this.setPersistenceStatus(true);
                this.setStatusMessage("Edited Last Name");
                break;
            case "address" :
                this.setAddress(value);
                this.setPersistenceStatus(true);
                this.setStatusMessage("Edited Address");
                break;
            case "phone" :
                this.setPhone(value);
                this.setPersistenceStatus(true);
                this.setStatusMessage("Edited Phone");
                break;
            case "specialization" :
                this.setSpecialization(value);
                this.setPersistenceStatus(true);
                this.setStatusMessage("Edited Specialization");
                break;
            case "location" :
                this.setLocation(value);
                this.setPersistenceStatus(true);
                this.setStatusMessage("Edited Location");
                break;
            default :
                this.setPersistenceStatus(false);
                this.setStatusMessage("Field " + fieldName + " does not exist");
                break;
        }
        
    }
    
    public void setAddress(String address)
    {
        this.address = address;
    }
    
    public String getAddress()
    {
        return this.address;
    }
    
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    
    public String getPhone()
    {
        return this.phone;
    }
    
    public void setSpecialization(String specialization)
    {
        this.specialization = specialization;
    }
    
    public String getSpecialization()
    {
        return this.specialization;
    }
    
    public void setLocation(String location)
    {
        this.location = location;
    }
    
    public String getLocation()
    {
        return this.location;
    }

	@Override
	public String toString() {
		
		return "Nurse Record : " + "ID: " + getId() +   "   fname: " + getFirstName() + "  lname: " + 
				super.getFirstName() + "  address: " + address + "  phone: " + phone + "  specialization:" + specialization+ "  Location:" +specialization+ "/n"
				+ "StatusMessage: " + getStatusMessage();
	}
    
    
}
