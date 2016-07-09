package frontend;



import java.io.PrintWriter;

import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import FIFOsubsystem.Message;
import FIFOsubsystem.SendMessage;
import RemotInterfaceApp.RemotInterfacePOA;
import servers.records.RecordManager;
import udp.MessageExchange.WaitDoOperationMessage;
import udp.changeLeader.WaitForNewLeader;
import utilities.RmiLogger;

public class MontrealFrontEnd extends RemotInterfacePOA {
    
    private RecordManager database;
    private RmiLogger logger;
    private final String serverName;
    private SendMessage sendMessage;
    private int sequenceNum;
    
    
    public MontrealFrontEnd ()
    {
        this.serverName = "mtl";
        this.database = new RecordManager();
        this.logger = new RmiLogger(serverName, "server");
        this.sendMessage = new SendMessage();
        sendMessage.setFrontEnd(true);
        sendMessage.setManagerPort(50005);
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
	    
		Message message = new Message(1, nextsequenceNum(),
									managerID , firstName,
									lastName, address, 	
									phone, specialization , location); 
		 String result = sendMessage.sendTo(message);
		 return  result ;
	}
	    
	public String createNRecord (
		String managerID,
		String firstName, 
		String lastName, 
		String designation,
		String status,
		String statusDate
    ) {
		
		int seqNumber = nextsequenceNum();
	    Message message = new Message(2, seqNumber ,
				managerID , firstName,
				lastName, designation, 	
				status, statusDate );
		 String result = sendMessage.sendTo(message);
		 return  result ;
	}
	
    public String editRecord (String managerID,String recordId, String fieldName, String newValue) {
    	int seqNumber = nextsequenceNum();
	    Message message = new Message(3, seqNumber,
				managerID , recordId,
				fieldName, newValue); 
		 String result = sendMessage.sendTo(message);
		 return  result ;
    } 

    
    public String getRecordCount (String managerID, int type) {
    	int seqNumber = nextsequenceNum();
	    Message message = new Message(4, seqNumber, managerID, type);
		 String result = sendMessage.sendTo(message);
		 return  result ;
    }
    
	
	public String transferRecord(String managerID, String recordID, String remoteClinicServerName) {
		int seqNumber = nextsequenceNum();
		Message message = new Message(5, seqNumber, managerID,
										recordID, remoteClinicServerName);   
		
		 String result = sendMessage.sendTo(message);
		 return  result ;
    }
	
	private synchronized int nextsequenceNum(){
		int num = sequenceNum;
		sequenceNum = sequenceNum++;
		return num;
	}
    private void startUdpServer() 
    {
//    	WaitForNewLeader waitForNewManager = new WaitForNewLeader(50015, this.sendMessage);
//		waitForNewManager.start();
    	
		WaitDoOperationMessage waitDoOperationMessage = new WaitDoOperationMessage(20040, sendMessage);
		waitDoOperationMessage.start();
    }
    
  //----------------------------------------------(RunCROBAobject)-------------------------------------------------------------------------
    public static void main(String []args){
    		
    		try{
    	    Runtime.getRuntime().exec("orbd -ORBInitialPort 900 - ORBInitialHost 127.0.0.1");
       		ORB orb = ORB.init(args, null);
       		POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
       		MontrealFrontEnd montrealFrontEnd = new MontrealFrontEnd();
       		byte[]id = rootPOA.activate_object(montrealFrontEnd);
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