package Center;

import java.io.Serializable;

import servers.records.Record;

public class Message implements  Serializable
{

private static final long serialVersionUID = 1L;
private String response;	







	private boolean dealtWith = false;     // operation has cancled 
    private  boolean resend = false;	       // resend lost message 
    private boolean  pingSystem = false;       // sent by failure detection 
    private boolean  checkAvailability = false; // false means new leader has elected and 
    											// info about the new manager
    											// the port number to return message to 
   
   private boolean bullyAlgorithm = false; 
   private int leaderPort;
   private int senderPort;
   private int operationID;
   private int sequenceNum;
   private String managerID;
   private Record record;
   private String firstName;
   private String lastName;
   private String address;
   private String Phone;
   private String specialization;
   private String location;
   private String designation;
   private String status;
   private String statusDate;
   private String recordID;
   private String fieldName;
   private String newValue;
   private int countType;
   private String remoteClinicServerName;

   public String getRemoteClinicServerName() {
	return remoteClinicServerName;
}



   
   /**
    * ping
    */
   public Message(boolean bullyAlgorithm ){
	   this.checkAvailability = true;
	   this.pingSystem = true;
	   this.bullyAlgorithm = bullyAlgorithm;
   }
   
   /**
    * new leader
    * @param newManagerport
    */
   public Message(int newLeaderport){
	   this.leaderPort = newLeaderport;
	   this.pingSystem = true;
	   this.checkAvailability = false;      //#todo        
   }
   
   /**
    *  for lost messages 
    * @param operationID
    * @param sequenceNum
    */
   public Message(int operation , int sequenceNum )
   {
	  this.resend = true;
      this.sequenceNum = sequenceNum;
   }
   
   
   public Message(int operationID ,int sequenceNum,
		   String managerID ,Record record) {
	   
	   this.operationID = operationID;
	   this.sequenceNum = sequenceNum;
	   this.managerID = managerID;
	   this.record = record ;
			   
   }
    public Message(int operationID ,int sequenceNum,
    			   String managerID ,String firstName, 
    		       String lastName, String designation, 
    		       String status,String statusDate ){
    	
	  this.operationID = operationID;
	  this.sequenceNum = sequenceNum;
	  this.managerID = managerID;
	  this.firstName = firstName;
	  this.lastName = lastName;
	  this.designation = designation;
	  this.status = status;
	  this.statusDate = statusDate;
   }
    
    
    public Message(int operationID , int sequenceNum , 
    			   String managerID ,String firstName, 
		           String lastName, String address, 
		           String phone, String specialization , String location){

    	this.operationID = operationID;
    	this.sequenceNum =  sequenceNum;
    	this.managerID = managerID;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.address = address;
    	this.location = location;
    	this.specialization = specialization;
    	this.Phone = phone;
}
    
    public Message(int operationID , int sequenceNum ,
    				String managerID,String recordID, 
    				String fieldName, String newValue){

 	this.operationID = operationID;
 	this.sequenceNum =  sequenceNum;
 	this.managerID = managerID;
 	this.recordID = recordID;
 	this.fieldName = fieldName;
 	this.newValue = newValue;
}
    
    public Message(int operationID, int sequenceNum,
    				String managerID, int type){

 	this.operationID = operationID;
 	this.sequenceNum =  sequenceNum;
 	this.managerID = managerID;
 	this.countType = type;
}
    
    public Message(int operationID,int sequenceNum,
    				String managerID, String recordID, 
    				String remoteClinicServerName){

 	this.operationID = operationID;
 	this.sequenceNum =  sequenceNum;
 	this.managerID = managerID;
 	this.recordID = recordID;
 	this.remoteClinicServerName = remoteClinicServerName;
}
    

    public String getResponse() {
 	return response;
 }
    
   public boolean isDealtWith() {
	return dealtWith;
}

   public String getManagerID() {
	return managerID;
}

public String getFirstName() {
	return firstName;
}

public String getLastName() {
	return lastName;
}

public String getAddress() {
	return address;
}

public String getPhone() {
	return Phone;
}

public String getSpecialization() {
	return specialization;
}

public String getLocation() {
	return location;
}

public String getDesignation() {
	return designation;
}


public String getStatus() {
	return status;
}
public String getStatusDate() {
	return statusDate;
}

public String getRecordID() {
	return recordID;
}

public String getFieldName() {
	return fieldName;
}

public String getNewValue() {
	return newValue;
}
public int getCountType() {
	return countType;
}
public int getOperationID() {
	return operationID;
}
public int getSequenceNum() {
	return sequenceNum;
}
 
public boolean isResend() {
	return resend;
}

public void setDealtWith(boolean dealtWith) {
	this.dealtWith = dealtWith;
}
public void setSequenceNum(int sequenceNum) {
	this.sequenceNum = sequenceNum;
}
public void setResend(boolean resend) {
	this.resend = resend;
}

public int getSenderPort() {
	return senderPort;
}

public void setSenderPort(int senderPort) {
	this.senderPort = senderPort;
}

public boolean isPingSystem() {
	return pingSystem;
}

public void setPingSystem(boolean pingSystem) {
	this.pingSystem = pingSystem;
}

public Record getRecord() {
	return record;
}

public void setResponse(String response) {
	this.response = response;
}


public boolean isCheckAvailability() {
	return checkAvailability;
}

public int getLeaderPort() {
	return leaderPort;
}




public boolean isBullyAlgorithm() {
	return bullyAlgorithm;
}


}
