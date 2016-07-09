package failuredetectionSubSystem;

public class PingServerInfo {

	private boolean manager;
	private int port;
	
	public PingServerInfo(boolean manager , int port){
		this.manager = manager;
		this.port = port;
	}
	
	public boolean isManager() {
		return manager;
	}
	public void setManager(boolean manager) {
		this.manager = manager;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	
}
