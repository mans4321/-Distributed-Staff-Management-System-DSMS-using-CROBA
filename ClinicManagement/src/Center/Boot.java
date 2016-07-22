package Center;

import servers_frontends.Dollard;
import servers_frontends.Laval;
import servers_frontends.Montreal;

public class Boot {

	
	public Boot(){
		
		for(int i = 1 ; i < 4 ; i++){
			new RunDDOServer(i).start();
			new RunMTLServer(i).start();
			new RunLVLServer(i).start();
		}
		
	}
	
	
	public void RunMTL(){
		for(int i = 1 ; i < 4 ; i++){
			new RunDDOServer(i).start();
			new RunMTLServer(i).start();
			new RunLVLServer(i).start();
		}
	}
	
	public void RunDDO(){
		for(int i = 1 ; i < 4 ; i++){
			new RunDDOServer(i).start();
			new RunMTLServer(i).start();
			new RunLVLServer(i).start();
		}
	}
	
	public void RunLVL(){
		for(int i = 1 ; i < 4 ; i++){
			new RunDDOServer(i).start();
			new RunMTLServer(i).start();
			new RunLVLServer(i).start();
		}
	}
	
	
	 class RunDDOServer extends Thread{
			
		int serverPriority;
		
		private RunDDOServer(int serverPriority){
			this.serverPriority = serverPriority;
		}
		public void run(){
			new Dollard(serverPriority);
		}
		
	}
	 
	 
	 class RunMTLServer extends Thread{
			
		int serverPriority;
		
		private RunMTLServer(int serverPriority){
			this.serverPriority = serverPriority;
		}
		public void run(){
			new Montreal(serverPriority);
		}
		
	}
	 
	 class RunLVLServer extends Thread{
			
		int serverPriority;
		
		private RunLVLServer(int serverPriority){
			this.serverPriority = serverPriority;
		}
		public void run(){
			new Laval(serverPriority);
		}
		
	}
	 
	
	public static void main(String [] args){
		new Boot();
	}
	
	
}
