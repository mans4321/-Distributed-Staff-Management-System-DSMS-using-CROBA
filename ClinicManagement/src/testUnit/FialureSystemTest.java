package testUnit;

import Center.AllServersInfo;

import failuredetection.PingServers;
import servers_frontends.Dollard;

public class FialureSystemTest {

	public FialureSystemTest() {
		
		AllServersInfo server = new AllServersInfo("DDO");
		
		new PingServers(server.getServer1() , server.getServer2() , 
				server, new Dollard(3),  3 );
				
	}

	
	
	public static void main(String[]args){
		new FialureSystemTest();
	}
}
