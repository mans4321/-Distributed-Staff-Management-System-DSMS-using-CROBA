package Center;

import java.util.ArrayList;

public class AllServersInfo {

	
	private ServerInfo server1;
	private ServerInfo server2;
	private ServerInfo server3;
	private ServerInfo frontEnd;
	private ArrayList<ServerInfo> allServers;
	
	public AllServersInfo(String server){
		generateInfoforOtherServers(server);
	}

	private void generateInfoforOtherServers(String serverName) {
			
			switch(serverName){
			
			case"DDO":
				server1 = new ServerInfo(true, 30012  , 1);
				server2 = new ServerInfo(false, 30014  , 2);
				server3 = new ServerInfo(false, 30016 , 3);
				frontEnd = new ServerInfo(20012);
				break;
			case"MTL":
				server1 = new ServerInfo(true, 30018  , 1);
				server2 = new ServerInfo(false, 30020  , 2);
				server3 = new ServerInfo(false, 30022  , 3);
				frontEnd = new ServerInfo(20032);
				break;
			case"LVL":
				server1 = new ServerInfo(true, 30024  , 1);
				server2 = new ServerInfo(false, 30026  , 2);
				server3 = new ServerInfo(false, 30028  , 3);
				frontEnd = new ServerInfo(20040);
				break;
			}
	}

	public ServerInfo getServer1() {
		return server1;
	}

	public ServerInfo getServer2() {
		return server2;
	}

	public ServerInfo getServer3() {
		return server3;
	}

	public ServerInfo getFrontEnd() {
		return frontEnd;
	}
	
	public ArrayList<ServerInfo> getAllserver(){
		allServers = new ArrayList<ServerInfo>();
		allServers.add(server1);
		allServers.add(server2);
		allServers.add(server3);
		return allServers;
		
	}
	
}
