package failuredetectionSubSystem;

import Center.ApplyOperations;
import Center.ServersInfo;

public class BullyAlgorithm {

	ServersInfo server1info;
	ServersInfo server2info;
	ApplyOperations thisServer;
	
	
	public BullyAlgorithm (ServersInfo server1info , ServersInfo server2info , ApplyOperations thisServer){
		this.server1info = server1info;
		this.server1info = server1info;
		this.thisServer = thisServer;
		elect();
	}
	
	private void elect(){
		
		
	}
	
	private void electMyselfAsLeader(){
		thisServer.leaderChanged(true , 0 );
	}
}
