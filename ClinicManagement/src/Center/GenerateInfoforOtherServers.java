package Center;

public class GenerateInfoforOtherServers {

	
	ServersInfo server1;
	ServersInfo server2;
	ServersInfo server3;
	ServersInfo frontEnd;
	
	public GenerateInfoforOtherServers(String server){
		generateInfoforOtherServers(server);
	}

	private void generateInfoforOtherServers(String serverName) {
			
			switch(serverName){
			
			case"DDO":
				server1 = new ServersInfo(true, 30012  , 1);
				server2 = new ServersInfo(false, 30014  , 2);
				server3 = new ServersInfo(false, 30016 , 3);
				frontEnd = new ServersInfo(20012);
				
			case"MTL":
				server1 = new ServersInfo(true, 30018  , 1);
				server2 = new ServersInfo(false, 30020  , 2);
				server3 = new ServersInfo(false, 30022  , 3);
				frontEnd = new ServersInfo(20032);
				
			case"LVL":
				server1 = new ServersInfo(true, 30024  , 1);
				server2 = new ServersInfo(false, 30026  , 2);
				server3 = new ServersInfo(false, 30028  , 3);
				frontEnd = new ServersInfo(20040);
			}
	}

	public ServersInfo getServer1() {
		return server1;
	}

	public ServersInfo getServer2() {
		return server2;
	}

	public ServersInfo getServer3() {
		return server3;
	}

	public ServersInfo getFrontEnd() {
		return frontEnd;
	}
	
	
}
