package servers_frontends;

import java.io.PrintWriter;

import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import Center.AllServersInfo;
import Center.Message;
import Center.MessageTransport;
import corbafiles.RemotInterfacePOA;
import udp.ServerOperationMessage;

public class DollardFrontEnd extends RemotInterfacePOA  {
	

    private int sequenceNum = 1;
    private MessageTransport sendMessage;
    private AllServersInfo getInfo;

    public DollardFrontEnd ()
    {
    	 getInfo = new AllServersInfo("DDO");
        this.sendMessage = new MessageTransport(getInfo.getServer1().getPort());
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
	    
		System.out.println("DollardFrontEnd  createDRecord");
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
		
	    Message message = new Message(2, nextsequenceNum() ,
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
	    sequenceNum++;
		return num;
	}
	
	private void startUdpServer() {
		ServerOperationMessage waitDoOperationMessage = new ServerOperationMessage(getInfo.getFrontEnd().getPort(), sendMessage);
		waitDoOperationMessage.start();
	}

	
//----------------------------------------------(RunCROBAobject)-------------------------------------------------------------------------
public static void main(String []args){
		
		try{
			 Runtime.getRuntime().exec("orbd -ORBInitialPort 30000 -ORBInitialHost 127.0.0.1");
	       		ORB orb = ORB.init(args, null);
	       		POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
	       		DollardFrontEnd dollardFrontEnd = new DollardFrontEnd();
	       		byte[]id = rootPOA.activate_object(dollardFrontEnd);
	       		org.omg.CORBA.Object ref = rootPOA.id_to_reference(id);
	       		rootPOA.the_POAManager().activate();
	       		String ior = orb.object_to_string(ref);
	       		PrintWriter file = new PrintWriter("Dollard.txt");
	       		file.println(ior);
	       		file.close();
	       		System.out.println("DDO forntEnd clinic running");
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