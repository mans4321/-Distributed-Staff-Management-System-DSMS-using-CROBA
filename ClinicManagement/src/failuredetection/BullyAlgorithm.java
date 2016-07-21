package failuredetection;

import java.util.ArrayList;

import Center.Message;
import Center.ServerInfo;
import Center.ServerOperations;
import udp.SendingOperationMessage;

public class BullyAlgorithm {

	private ArrayList<ServerInfo> allServers;
	private ServerOperations thisServer;
	private int processID;
	private boolean respond;
	
	public BullyAlgorithm(int processID, ArrayList<ServerInfo> allServers, ServerOperations thisServer){
		
		this.processID = processID;
		this.allServers = allServers; 
		this.thisServer = thisServer;
		this.respond = false ;
		elect();
	}
	

		private void elect(){
		
			for( ServerInfo server : allServers ){
				if(processID < server.getprocessID()){
						SendingOperationMessage checkAvailability = 
							new SendingOperationMessage(server.getPort(), new Message());
						try {
							checkAvailability.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						if( checkAvailability.getResultResponse().equals("noResponse")){
							respond = true; 
						}
				}
			}
			
			if(! respond){
				electMyselfAsLeader();
			}
		}
	
		
	private void electMyselfAsLeader(){
		thisServer.leaderChanged(true , 0 );
	}
}
