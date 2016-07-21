package failuredetection;

import java.util.ArrayList;

import Center.ServerOperations;
import Center.ServerInfo;

public class BullyAlgorithm {

	private ArrayList<ServerInfo> allServers;
	private ServerOperations thisServer;
	private int processID;
	private boolean responding;
	
	public BullyAlgorithm(int processID, ArrayList<ServerInfo> allServers, ServerOperations thisServer) {
		this.processID = processID;
		this.allServers = allServers; 
		this.thisServer = thisServer;
		this.responding = false ;
		elect();
	}

		private void elect(){
		
			for( ServerInfo server : allServers ){
				if(processID < server.getprocessID()){
					
				}
			}
		}
	
	private void electMyselfAsLeader(){
		thisServer.leaderChanged(true , 0 );
	}
}
