package servers.records;

import java.util.ArrayList;
import java.util.HashMap;

import udp.TwoPhaseProtocolClient;
import utilities.DealWithUndeletedRecord;


public class RecordManager {
    private HashMap<Character, ArrayList<Record>> records;
    private HashMap<String, Character> lastNames;
    private Integer doctorUniqueId = 0;
    private Integer nurseUniqueId = 0;
    
    public RecordManager()
    {
        this.records = new HashMap<Character, ArrayList<Record>>();
        this.lastNames = new HashMap<String, Character>();
        
    }
    
    public Record createDRecord (
            String firstName, 
            String lastName,
            String address, 
            String phone, 
            String specialization,
            String location
    )  {
      
        String nextId = getNextId("doctor");
        System.out.println(nextId);
        Record doctor = new DoctorRecord(nextId, firstName, lastName, address, phone, specialization, location);
        Character index = doctor.getLastNameIndex();
        
        if(addToRecord(doctor, index)){
        	doctor.setStatusMessage("New Doctor Created.");
        	doctor.setPersistenceStatus(true);
        }else{ 
        	doctor.setStatusMessage("could not creat record");
        	doctor.setPersistenceStatus(false);
        }
        return doctor;
    }
    
    public Record createNRecord (
            String firstName, 
            String lastName, 
            String designation,
            String status,
            String statusDate
    ) {
        String nextId = getNextId("nurse");
        Record nurse = new NurseRecord(nextId, firstName, lastName, designation, status, statusDate);
        Character index = nurse.getLastNameIndex();
        if(addToRecord(nurse, index)){
        	nurse.setStatusMessage("New nurse Created." + "ID :" + nextId);
        	nurse.setPersistenceStatus(true);
        }else{ 
        	nurse.setStatusMessage("could not creat nurse record");
        	nurse.setPersistenceStatus(false);	
        }
        return nurse;
    }
    
    public Record editRecord (String recordId, String fieldName, String newValue) { 
   
        /// only synchronized(record) when its update
        if (lastNames.containsKey(recordId)) {
            ArrayList<Record> possibleRecords = records.get(lastNames.get(recordId));
            for (Record record : possibleRecords) {
                if (record.getId().equals(recordId)) {
                	synchronized(record){
                    record.setField(fieldName, newValue);
                    record.setStatusMessage("Record has update");
                	}
                    return record;
                }
            }
        }
        
        return null;
    }
    
    public String getNextId(String type)  {
          //    synchronized()  only IDs
    	
        if (type.equals("nurse")) {
 
            synchronized(nurseUniqueId){
            nurseUniqueId++;
            }
            return "NR" + String.format("%05d", nurseUniqueId);
        }
        
        synchronized(doctorUniqueId){
        doctorUniqueId++;
        }
        return "DR" + String.format("%05d", doctorUniqueId);
    }
    
    public HashMap<String, Character> getLastNames() {
		return lastNames;
	}

	public boolean addToRecord(Record record, Character index)
    {
		
		 synchronized (index){
           if (records.containsKey(index)) {
            if (records.get(index).add(record)) {
                lastNames.put(record.getId(), record.getLastNameIndex()); 
            
                record.setPersistenceStatus(true);
                return true;
            }  
          }
        		ArrayList<Record> recordList = new ArrayList<Record>();
        		if(!recordList.add(record)){
        			return false;
        		}
        		records.put(index, recordList);
        		lastNames.put(record.getId(), record.getLastNameIndex());
        		return true;
		 }
    }
	
    public  int getRecordCounts()
    {
    	int counter = 0; 
        for(ArrayList<Record> storedRecords : records.values()){
            counter += storedRecords.size();
        }
        return counter;
    }
    
    
  ///-----------------------------(transferRecord)----------------------------------------------
   
   public boolean transferRecord(String managerId ,String recordId, String remoteClinicServerName){
	 
	    Record record = findRecordToTransfer(recordId);
	    Record transferedRecord = record;
	    boolean respoens;
	   if(record != null){
		   synchronized(record){   
	   switch(remoteClinicServerName.toUpperCase().trim()){
	   	case"DDO":
	   		TwoPhaseProtocolClient thread	=new TwoPhaseProtocolClient(8000,transferedRecord, this );
	   		thread.start();
		try {
			thread.join();
			respoens = thread.isResult();
		} catch (InterruptedException e) {
			respoens = false;
		}
	   	  break;
	   	case"MTL":	
	   		TwoPhaseProtocolClient thread2	=new TwoPhaseProtocolClient(10000,transferedRecord, this );
	   		thread2.start();
		try {
			thread2.join();
			respoens = thread2.isResult();
		} catch (InterruptedException e) {
			respoens = false;
		}
	   	  break;
	   	case"LVL": 
	   		TwoPhaseProtocolClient thread3	=new TwoPhaseProtocolClient(9000,transferedRecord, this );
	   		thread3.start();
		try {
			thread3.join();
			respoens = thread3.isResult();
		} catch (InterruptedException e) {
			respoens = false;
		}
	   	  break;
	   	  default :
	   		  return false; 
	   }
		   }
		   
	   }else{
	    respoens = false;
	   }
	   return respoens;
   }
   
    public boolean deleteTranferedRecord (Record record, DealWithUndeletedRecord backupRecord){
    	String recordId = record.getId();
        if (lastNames.containsKey(recordId)) {
        	synchronized(records.get(lastNames.get(recordId))){
	            ArrayList<Record> possibleRecords = records.get(lastNames.get(recordId));
	            for (int index =0 ; index < possibleRecords.size();index++) {
	                if (possibleRecords.get(index).getId().equals(recordId)) {
	                	possibleRecords.remove(index);
	                	records.put(lastNames.get(recordId), possibleRecords);
	                	lastNames.remove(lastNames.get(recordId));
	                	return true;
	                }
               	}
            }
        }
        record.setStatusMessage("could't delet record");
        backupRecord.setRecord(record);
        backupRecord.doSaveAsString();
        return false;
    }
    
    public Record findRecordToTransfer(String recordId){
    	final ArrayList<Record> possibleRecords ;
    	 if (lastNames.containsKey(recordId)) {
              possibleRecords = records.get(lastNames.get(recordId));
             for (int i =0 ; i < possibleRecords.size(); i++ ) {
                 if (possibleRecords.get(i).getId().equals(recordId)) {
                     return possibleRecords.get(i);
                 }
             }
         }
         return null;
    }
   
    
    
    public boolean addTransferedRecord(Record record)  {
    	String nextId;
    	String kind = record.getId().substring(0, 2);
    	if(kind.equalsIgnoreCase("DR")){
			try {
				nextId = getNextId("doctor");
			} catch (Exception e) {
				return false;
			}
			
    	}else{
    	    try {
				nextId = getNextId("nurse");
			} catch (Exception e) {
				return false;
			}
    	    
    	}
		record.setId(nextId);
        Character index = record.getLastNameIndex();
        return(addToRecord(record, index));
    }
    
    
    
    public String addRecord(Record record){
        Character index = record.getLastNameIndex();
        if(addToRecord(record, index)){
        	return  "done";
        }else{
        	return "notDone";
        }
        	
    }
    
    public String deleteRecord (String recordId){
        if (lastNames.containsKey(recordId)) {
        	synchronized(records.get(lastNames.get(recordId))){
	            ArrayList<Record> possibleRecords = records.get(lastNames.get(recordId));
	            for (int index =0 ; index < possibleRecords.size();index++) {
	                if (possibleRecords.get(index).getId().equals(recordId)) {
	                	possibleRecords.remove(index);
	                	records.put(lastNames.get(recordId), possibleRecords);
	                	lastNames.remove(lastNames.get(recordId));
	                	return "done";
	                }
               	}
            }
        }

        return "notDone";
    }
  }

