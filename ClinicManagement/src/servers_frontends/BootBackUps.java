package servers_frontends;

import Center.ServerOperations;

public class BootBackUps {

	public static void main(String args[]) {
			new BootBackUps();
	}

	
	public BootBackUps(){
		new StartDDOThread().start();
		new StartLVLThread().start();
		new StartMTLThread().start();
	}
	
	private class StartDDOThread extends Thread {
		public void run(){
			new Dollard(2);
		}
	}
	
	private class StartMTLThread extends Thread {
		public void run(){
			new Montreal(2);
		}
	}
	
	private class StartLVLThread extends Thread {
		public void run(){
			new Laval(2);
		}
	}
}
