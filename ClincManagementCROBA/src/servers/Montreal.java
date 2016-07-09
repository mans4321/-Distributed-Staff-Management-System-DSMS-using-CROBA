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
import udp.UDPWaitForTransferedRecord;
import utilities.RmiLogger;

public class Montreal extends RemotInterfacePOA {
    
    private RecordManager database;
    private RmiLogger logger;

    private final String serverName;

    
    
    public Montreal ()
    {
        this.serverName = "mtl";

        this.database = new RecordManager();
        this.logger = new RmiLogger(serverName, "server");
        startUdpServer() ;
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
            logger.log(result);
            
        } catch (Exception e) {
            logger.log(false, e.getMessage());
        }     
        return result.getStatusMessage();
         
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
        } catch (Exception e) {
            logger.log(false, e.getMessage());
        }
              
        return result.getStatusMessage();
    }
    

    public String editRecord (String managerID, String recordId, String fieldName, String newValue) {
        
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
    
    public String getRecordCount (String managerID , int recordType) {
    	
    	final ExecutorService service;
        final Future<Integer>  DDO;
        final Future<Integer>  LVL;
        Integer DDOCount = -1;
        Integer LVLCount = -1;
        service = Executors.newFixedThreadPool(2); 
        int localCount = database.getRecordCounts();
        DDO = service.submit(new UDPClient(9997));
        LVL = service.submit(new UDPClient(9999));
        try {
        	DDOCount = DDO.get();
			LVLCount = LVL.get();
		} catch (InterruptedException | ExecutionException e) {
	    	if(service != null)
	    	service.shutdown();
		}finally{
          	if(service != null)
	    	service.shutdown();
		}
        
        String allCounts = "MTL: " + localCount + ", DDO: " + DDOCount + ", LVL: " + LVLCount;
        return allCounts;
    }
    
	@Override
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
	
    private void startUdpServer() 
    {
        UDPServerCount udp = new UDPServerCount(9998, database);
        udp.start();
        UDPWaitForTransferedRecord transferThread = new UDPWaitForTransferedRecord(10000,database);
        transferThread.start();
    }
    
  //----------------------------------------------(RunCROBAobject)-------------------------------------------------------------------------
    public static void main(String []args){
    		
    		try{
    	    Runtime.getRuntime().exec("orbd -ORBInitialPort 900 - ORBInitialHost 127.0.0.1");
       		ORB orb = ORB.init(args, null);
       		POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
       		Montreal montreal = new Montreal();
       		byte[]id = rootPOA.activate_object(montreal);
       		org.omg.CORBA.Object ref = rootPOA.id_to_reference(id);
       		rootPOA.the_POAManager().activate();
       		String ior = orb.object_to_string(ref);
       		PrintWriter file = new PrintWriter("Montreal.txt");
       		file.println(ior);
       		file.close();
       		System.out.println("Montreal clinic running");
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