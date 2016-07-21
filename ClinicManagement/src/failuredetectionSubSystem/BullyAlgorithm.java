package failuredetectionSubSystem;

import java.util.ArrayList;

import Center.ApplyOperations;
import Center.ServersInfo;

public class BullyAlgorithm {

	private ArrayList<ServersInfo> allServers;
	private ApplyOperations thisServer;
	private int priority;

	public BullyAlgorithm(int priority, ArrayList<ServersInfo> allServers, ApplyOperations thisServer) {
		this.priority = priority;
		this.allServers = allServers; 
		this.thisServer = thisServer;
		elect();
	}

	private void elect(){
		
		
	}
	
	private void electMyselfAsLeader(){
		thisServer.leaderChanged(true , 0 );
	}
}
