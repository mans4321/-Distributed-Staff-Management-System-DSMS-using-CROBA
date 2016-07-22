package Center;

public class ServerInfo {

	private boolean leader;
	private int port;
	private int processID;
	private boolean stillWorking;
	
	public ServerInfo(boolean leader , int port, int processID){
		this.leader = leader;
		this.port = port;
		this.processID = processID;
		this.stillWorking = true;
	}

	/**
	 * in case of front end 
	 * @param port
	 */
	public ServerInfo( int port){
		this.port = port;
	}

	public boolean isLeader() {
		return leader;
	}
	public void setLeader(boolean leader) {
		this.leader = leader;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isStillWorking() {
		return stillWorking;
	}

	public void setStillWorking(boolean stillWorking) {
		this.stillWorking = stillWorking;
	}

	public int getprocessID() {
		return processID;
	}
	

	
}
