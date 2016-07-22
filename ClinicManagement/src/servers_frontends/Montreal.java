package servers_frontends;



import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import Center.ServerOperations;
import Center.AllServersInfo;
import Center.Message;
import Center.MessageTransport;
import Center.MessagesCenter;
import Center.ServerInfo;
import failuredetection.PingServers;
import servers.records.Record;
import servers.records.RecordManager;
import udp.TowPhaseProtocolServer;
import udp.UDPClient;
import udp.UDPServerCount;
import utilities.RmiLogger;

public class Montreal implements ServerOperations {
	private AllServersInfo serversInfo;
	private RecordManager database;
    private RmiLogger logger;
    private final String serverName;
    private boolean manager;
    private MessageTransport sendMessage;
    private MessagesCenter messageCenter;
    private PingServers pingServers;
    private ServerInfo server1;
    private ServerInfo server2;
    private int listenOnPort;
    private int front1Port;
    
    public Montreal (int processID )
    {
        this.serverName = "Montreal" + processID ;
        this.database = new RecordManager();
        this.logger = new RmiLogger(serverName, "server");
        this.database = new RecordManager();
        this.logger = new RmiLogger(serverName, "server");
      
        initializeVaules(processID);

        if(processID == 1){
        	this.manager = true;
        	startUdpForLeaderServer();
        }
    }
    
    private void initializeVaules(int processID){
   	 
    	 serversInfo = new AllServersInfo("MTL");
    	 
   	 switch(processID){
   	 
   	 	case 1 :
   	 		server1 = serversInfo.getServer2();
	   		 server2 =  serversInfo.getServer3();
	   		 listenOnPort = serversInfo.getServer1().getPort();
	   		 front1Port = serversInfo.getFrontEnd().getPort();
	   		 break;
   	 	case 2 :
	   		 server1 = serversInfo.getServer1();
	   		 server2 =  serversInfo.getServer3();
	   		 listenOnPort = serversInfo.getServer2().getPort();
	   		 front1Port = serversInfo.getServer1().getPort();
	   		break;
   	 	case 3 :
	   		 server1 = serversInfo.getServer1();
	   		 server2 =  serversInfo.getServer2();
	   		 listenOnPort = serversInfo.getServer3().getPort();
	   		 front1Port = serversInfo.getServer1().getPort();
	   		break;
   	 }
   	 
   	 	sendMessage = new MessageTransport(server1.getPort(), 
   	 										server2.getPort());
   	 	pingServers = new PingServers(server1, server2 , serversInfo , this, processID);
   	 	messageCenter = new MessagesCenter(manager, listenOnPort , front1Port,
   	 											 this, processID, serversInfo  );
    }
    
    public String createDRecord (
			String managerID,
			String firstName, 
			String lastName,
			String address, 
			String phone, 
			String specialization,
			String location,
			int sequenceNum
	)  {
	    Record record;
	    record = database.createDRecord(firstName, lastName, address, phone, specialization, location);
	    logger.log(record);
	      	if(record.isSuccessful()){
	      		Message message = new Message(1, sequenceNum , managerID , record);
	      		sendMessage.sendTo(message);
	      	}
	      	return record.getStatusMessage();
	}
	    
	public String createNRecord (
		String managerID,
		String firstName, 
		String lastName, 
		String designation,
		String status,
		String statusDate,
		int sequenceNum
    ) {

            Record record;
            record = database.createNRecord(firstName, lastName, designation, status, statusDate);
    	    logger.log(record);
    	      	if(record.isSuccessful()){
    	      		Message message = new Message(2, sequenceNum , managerID , record);
    	      		sendMessage.sendTo(message);
    	      	}
    	      	return record.getStatusMessage();
	}
	
    public String editRecord (String managerID,String recordId, String fieldName, String newValue, int sequenceNum) {
        
        Record record;
        record = database.editRecord(recordId, fieldName, newValue);
	    logger.log(record);
	      	if(record.isSuccessful()){
	      		Message message = new Message(3, sequenceNum, managerID , recordId,fieldName, newValue );
	      		sendMessage.sendTo(message);
	      	}
	      	return record.getStatusMessage();
    } 

    
    public String getRecordCount (String managerID, int type, int sequenceNum) {
    	Message message = new Message(4, sequenceNum, managerID, type );
    	sendMessage.sendTo(message);

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
    
	
	public String transferRecord(String managerID, String recordID, String remoteClinicServerName, int sequenceNum) {
		String result; 
		if(database.transferRecord(managerID, recordID, remoteClinicServerName)){
			result = "Record "+recordID + " has been transferd";
			logger.log(result);
      		Message message = new Message(5, sequenceNum, managerID , recordID,remoteClinicServerName);
      		sendMessage.sendTo(message);
			return result;
		}
		else{ 
			result = " could't transfer record  " +recordID;
			logger.log(result);
			return result  ;
		}
	}
	
	
	private  void startUdpForLeaderServer() 
	{
        UDPServerCount udp = new UDPServerCount(9998, database);
        udp.start();
        TowPhaseProtocolServer transferThread = new TowPhaseProtocolServer(10000,database);
        transferThread.start();
	}
	
	public void leaderChanged(boolean manager , int port){
		
		if(manager){
			startUdpForLeaderServer();
			messageCenter.managerHasChanged(manager, front1Port);
		}else{
			pingServers.newLeader(port);
			messageCenter.managerHasChanged(manager, port);
		}
	}
	public RecordManager getDatabase() {
		return database;
	}

    public static void main(String []args){
    	Montreal montreal = new Montreal(1);
    	}
    }