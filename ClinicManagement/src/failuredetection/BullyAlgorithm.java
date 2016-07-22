package failuredetection;

import Center.AllServersInfo;
import Center.Message;
import Center.ServerInfo;
import Center.ServerOperations;
import udp.SendingOperationMessage;

public class BullyAlgorithm {

	private AllServersInfo serversInfo;
	private ServerOperations thisServer;
	private int processID;
	private boolean respond;
	private boolean bullyAlgorithm;
	
	public BullyAlgorithm(int processID, AllServersInfo serversInfo, ServerOperations thisServer){
		
		this.processID = processID;
		this.serversInfo = serversInfo; 
		this.thisServer = thisServer;
		this.respond = false ;
		this.bullyAlgorithm = true;
		elect();
	}
	

		private void elect(){
		
			System.out.println("BullyAlgorithm  elect ");

			for( ServerInfo server : serversInfo.getAllserver() ){
				if(server.getprocessID() < processID ){
						SendingOperationMessage checkAvailability = 
							new SendingOperationMessage(server.getPort(), new Message(bullyAlgorithm));
						checkAvailability.start();
						try {
							checkAvailability.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						if( ! checkAvailability.getResultResponse().equals("noResponse")){
							respond = true; 
						}
				}
			}
			
			if(! respond){
				electMyselfAsLeader();
			}
		}
	
		
	private void electMyselfAsLeader(){
		// get this process port
		System.out.println("BullyAlgorithm  electMyselfAsLeader ");
		int thisProcessPort = 0; 
		for( ServerInfo server : serversInfo.getAllserver()  ){
			if(server.getprocessID() == processID){
				thisProcessPort = server.getPort();
				System.out.println("BullyAlgorithm  electMyselfAsLeader  thisProcessPort");
			}
		}
		
		// info the front End
		
		SendingOperationMessage toFrontEnd = 
				new SendingOperationMessage(serversInfo.getFrontEnd().getPort(), new Message(thisProcessPort));
		toFrontEnd.start();
		try {
			toFrontEnd.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// infom other process a new leader has elect
		for( ServerInfo server : serversInfo.getAllserver() ){
			if(server.getprocessID() != processID){
					SendingOperationMessage newLeader = 
							new SendingOperationMessage(server.getPort(), new Message(thisProcessPort));
					newLeader.start();
			}
		}
		
		thisServer.leaderChanged(true , 0 );
	}
	
}
