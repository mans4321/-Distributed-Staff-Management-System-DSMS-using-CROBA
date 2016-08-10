package servers_frontends;


public class BootBackUps2 {

	public static void main(String args[]) {
		new BootBackUps2();
	}

	
	public BootBackUps2(){
		new StartDDOThread().start();
		new StartLVLThread().start();
		new StartMTLThread().start();
	}
	
	private class StartDDOThread extends Thread {
		public void run(){
			new Dollard(3);
		}
	}
	
	private class StartMTLThread extends Thread {
		public void run(){
			new Montreal(3);
		}
	}
	
	private class StartLVLThread extends Thread {
		public void run(){
			new Laval(3);
		}
	}
}

