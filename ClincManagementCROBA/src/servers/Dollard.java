package servers;

import java.io.PrintWriter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import RemotInterfaceApp.RemotInterfacePOA;
import servers.records.Record;
import servers.records.RecordManager;
import udp.UDPClient;
import udp.UDPServerCount;
import udp.UDPSocketClient;
import udp.UDPWaitForTransferedRecord;
import utilities.RmiLogger;

public class Dollard extends RemotInterfacePOA {
	
    private RecordManager database;
    private RmiLogger logger;

    private final String serverName;

    
    
    public Dollard ()
    {

        this.serverName = "ddo";
        this.database = new RecordManager();
        this.logger = new RmiLogger(serverName, "server");
        startUdpServer();
    }
    
	public String createDRecord (
			String managerID,
			String firstName, 
			String lastName,
			String address, 
			String phone, 
			String specialization,
			String location
	)  {
	    Record result = null;
	    
	    try {
	        result = database.createDRecord(firstName, lastName, address, phone, specialization, location);
	      	if(result.getStatusMessage() == null)
	    		System.out.println("null");
	        logger.log(result);
	        System.out.println("ID "+ result.getId());
	      	return result.getStatusMessage();
	    } catch (Exception e) {
	        logger.log(false, e.getMessage());
	       return e.getMessage();
	    }finally{
	    	
	    }
	}
	    
	public String createNRecord (
		String managerID,
		String firstName, 
		String lastName, 
		String designation,
		String status,
		String statusDate
    ) {
	    Record result = null;
        try {
            result = database.createNRecord(firstName, lastName, designation, status, statusDate);
            logger.log(result);
            return result.getStatusMessage();
        } catch (Exception e) {
            logger.log(false, e.getMessage());
            return  result.getStatusMessage();
        }
	}
	
    public String editRecord (String managerID,String recordId, String fieldName, String newValue) {
        
        Record result = null;
        result = database.editRecord(recordId, fieldName, newValue);
        if(result != null){
        logger.log(result);
        return result.getStatusMessage();
        }
        else {
        	logger.log("could't update record");
        	return "could't update record";
        }
    } 
    
//    final ExecutorService service;
//    final Future<Integer>  task;
//    service = Executors.newFixedThreadPool(1);        
//    task    = service.submit(new getRecodCoutFromServer(9874));
//    try {
//        final Integer Recordcount;
//        Recordcount = task.get(); // this raises ExecutionException if thread dies
//        service.shutdown();
//        return Recordcount;  
//    } catch(final InterruptedException ex) {
//    	if(service != null)
//    	service.shutdown();
//    	return -1;
//    } catch(final ExecutionException ex) {
//    	if(service != null)
//    	service.shutdown();
//    	return -1;
//    } 
    
    public String getRecordCount (String managerID, int type) {
    	final ExecutorService service;
        final Future<Integer>  LVL;
        final Future<Integer>  MTL;
        Integer LvLCount = -1 ;
        Integer MTLcount = -1 ;
        service = Executors.newFixedThreadPool(2); 
        int localCount = database.getRecordCounts();
        LVL = service.submit(new UDPClient(9999));
        MTL = service.submit(new UDPClient(9998));
        try {
			LvLCount = LVL.get();
			MTLcount = MTL.get();
		} catch (InterruptedException | ExecutionException e) {
	    	if(service != null)
	    	service.shutdown();
		}finally{
          	if(service != null)
	    	service.shutdown();
		}
        
        String allCounts = "DDO: " + localCount + ", LVL: " + LvLCount + ", MTL: " + MTLcount;
        return allCounts;
    }
    
	
	public String transferRecord(String managerID, String recordID, String remoteClinicServerName) {
		String result; 
		if(database.transferRecord(managerID, recordID, remoteClinicServerName)){
			result = "Record "+recordID + "has been transferd";
			logger.log(result);
			return result;
		}
		else{ 
			result = "could't transfer record  " +recordID;
			logger.log(result);
			return result  ;
		}
	}
	
	private  void startUdpServer() 
	{
		UDPServerCount udp = new UDPServerCount(9997, database);
	    udp.start();      
	    UDPWaitForTransferedRecord transferedRecord = new UDPWaitForTransferedRecord(8000,database);
	    transferedRecord.start();
	}
//----------------------------------------------(RunCROBAobject)-------------------------------------------------------------------------
public static void main(String []args){
		
		try{
			 Runtime.getRuntime().exec("orbd -ORBInitialPort 30000 -ORBInitialHost 127.0.0.1");
	       		ORB orb = ORB.init(args, null);
	       		POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
	       		Dollard montreal = new Dollard();
	       		byte[]id = rootPOA.activate_object(montreal);
	       		org.omg.CORBA.Object ref = rootPOA.id_to_reference(id);
	       		rootPOA.the_POAManager().activate();
	       		String ior = orb.object_to_string(ref);
	       		PrintWriter file = new PrintWriter("Dollard.txt");
	       		file.println(ior);
	       		file.close();
	       		System.out.println("DDO clinic running");
	       		orb.run();
		}catch(Exception e){
			e.printStackTrace(System.out);	
		}
		System.out.println("bang bang !!");
	}

	private ORB orb ;
	public void setORB(ORB orb){
	this.orb = orb ;
	}
}