package servers.records;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import udp.UDPSocketClient;


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
    ) throws Exception {
      
        String nextId = getNextId("doctor");
        Record doctor = new DoctorRecord(nextId, firstName, lastName, address, phone, specialization, location);
        Character index = doctor.getLastNameIndex();
        
        if(addToRecord(doctor, index))
        	doctor.setStatusMessage("New Doctor Created.");
        else 
        	doctor.setStatusMessage("could not creat record");
        return doctor;
    }
    
    public Record createNRecord (
            String firstName, 
            String lastName, 
            String designation,
            String status,
            String statusDate
    ) throws Exception {
        String nextId = getNextId("nurse");
        Record nurse = new NurseRecord(nextId, firstName, lastName, designation, status, statusDate);
        Character index = nurse.getLastNameIndex();
        if(addToRecord(nurse, index))
        	nurse.setStatusMessage("New nurse Created.");
        else 
        	nurse.setStatusMessage("could not creat nurse record");
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
    
    public String getNextId(String type) throws Exception {
          //    synchronized()  only IDs
    	
        if (type.equals("nurse")) {
            if (nurseUniqueId >= 99999){
                throw new Exception("Too much nurses.");
            }
            synchronized(nurseUniqueId){
            nurseUniqueId++;
            }
            return "NR" + String.format("%05d", nurseUniqueId);
        }
        
        if (doctorUniqueId >= 99999) {
            throw new Exception("Too much doctors.");
        }
        synchronized(doctorUniqueId){
        doctorUniqueId++;
        }
        return "DR" + String.format("%05d", doctorUniqueId);
    }
    
    public boolean addToRecord(Record record, Character index)
    {
         // synchronized only the list 
    	 // However, I think you can just  synchronized key for the whole function which is going to lock the list also 
    	 // e.g synchronized(index){ ......(the whole function)	
    	 // tell  me what u think 
        if (records.containsKey(index)) {
        	 synchronized(records.get(index)){
            if (records.get(index).add(record)) {
                lastNames.put(record.getId(), record.getLastNameIndex());
            }
        	}
                record.setPersistenceStatus(true);
                return true;
            }        

        	synchronized(index){
        		ArrayList<Record> recordList = new ArrayList<Record>();
        		if(!recordList.add(record)){
        			return false;
        		}
        			records.put(index, recordList);
        		lastNames.put(record.getId(), record.getLastNameIndex());
        	}
            return true;
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
	   if(record == null){
		   return false;
	   }
	   final ExecutorService service;
       final Future<Boolean>  task;
       service = Executors.newFixedThreadPool(1); 
       boolean respoens;
	   switch(remoteClinicServerName.toUpperCase().trim()){
	   	case"DDO":
	   	  ///   CHANAGE PORT NUMBER
	   	  task  = service.submit(new UDPSocketClient(8000,record ,this));
	   	  break;
	   	case"MTL":	
	   	  ///   CHANAGE PORT NUMBER
	   	  task = service.submit(new UDPSocketClient(10000 ,record, this));
	   	  break;
	   	case"LVL": 
	   	  ///   CHANAGE PORT NUMBER
	   	  task = service.submit(new UDPSocketClient(9000,record ,this));
	   	  break;
	   	  default :
	   		  return false;
	   }
	   try {
           final boolean result;
           result = task.get(); // this raises ExecutionException if thread dies
           service.shutdown();
           return result;  
       } catch(final InterruptedException ex) {
       	return false;
       } catch(final ExecutionException ex) {
       	return false;
       } finally {
    		if(service != null)
    	       	service.shutdown();
	   }
   }
   
    public boolean deleteTranferedRecord (Record record){
    	String recordId = record.getId();
        if (lastNames.containsKey(recordId)) {
        	synchronized(records.get(lastNames.get(recordId))){
            ArrayList<Record> possibleRecords = records.get(lastNames.get(recordId));
            for (int index =0 ; index < possibleRecords.size();index++) {
                if (possibleRecords.get(index).getId().equals(recordId)) {
                	possibleRecords.remove(index);
                	return true;
                }
                }
            }
        }
        return false;
    }
    
    public Record findRecordToTransfer(String recordId){
    	
    	 if (lastNames.containsKey(recordId)) {
             ArrayList<Record> possibleRecords = records.get(lastNames.get(recordId));
             for (Record record : possibleRecords) {
                 if (record.getId().equals(recordId)) {
                     return record;
                 }
             }
         }
         return null;
    }
    
    public void addTransferedRecord(Record record) throws Exception {
    	String nextId;
    	System.out.println("addTransferedRecord-----------------------------------------");
    	String kind = record.getId().substring(0, 2);
    	System.out.println("Doctormmmm" + kind);
    	System.out.println("addTransferedRecord");
    	if(kind.equalsIgnoreCase("DR")){
			nextId = getNextId("doctor");
			System.out.println("addTransferedRecord :: Doctor");
    	}else{
    	    nextId = getNextId("nurse");
    	    System.out.println("addTransferedRecord :: Not Doctor");
    	}
		record.setId(nextId);
        Character index = record.getLastNameIndex();
        addToRecord(record, index);
    }
  }

