package failuredetectionSubSystem;

import java.util.Timer;
import java.util.TimerTask;

import servers.ApplyOperations;
import udp.MessageExchange.PingListener;
import udp.MessageExchange.PingMessage;

public class PingServers {
	
	private PingServerInfo server1info;
	private PingServerInfo server2info;
	private ApplyOperations thisServer;
	private boolean server1Working =true;
	private boolean server2Working =true;
	private int portForPing;
	private int portForNewLeader;
	public PingServers(PingServerInfo server1info , PingServerInfo server2info , 
			ApplyOperations thisServer, int portForPing, int portForNewLeader ){
		this.server1info = server1info;
		this.server2info = server2info; 
		this.thisServer = thisServer;
		this.portForNewLeader = portForNewLeader;
		this.portForPing = portForPing;
		// statTimer();
		//startPing_newLeaderListener();
	}

	
	private void statTimer(){
		Timer timer;
	    timer = new Timer();
	    TimerTask TimerTask = new TimerTask(){
	    	public void run(){
	    		checkServes();
	    	}
	    };
	    timer.scheduleAtFixedRate(TimerTask, 1000 * 120 , 1000);
	  }

	private void checkServes(){
		if(server1Working){
			checkServeOne();
		}
		if(server2Working){
			checkServerTwo();
		}
	}
	
	private void checkServeOne(){
		PingMessage pingMessage = new PingMessage(server1info.getPort());
		pingMessage.start();
		
		try {
			pingMessage.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(pingMessage.getRespones().trim().equalsIgnoreCase("noResponse")){
			  if(server1info.isManager()){
				  new BullyAlgorithm(server1info , server2info ,thisServer );
				  
			  }
			  server1Working = false;
		}
	}
	
	private void checkServerTwo(){
		PingMessage pingMessage = new PingMessage(server2info.getPort());
		pingMessage.start();
		
		try {
			pingMessage.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(pingMessage.getRespones().trim().equalsIgnoreCase("noResponse")){
			  if(server1info.isManager()){
				  new BullyAlgorithm(server1info , server2info ,thisServer ); 
			  }
			  server1Working = false;
		}
		
	}
	
	private void startPing_newLeaderListener(){
		PingListener pingListener = new PingListener(portForPing);
		pingListener.start();
		//WaitForNewLeader
	}
	
	
}
