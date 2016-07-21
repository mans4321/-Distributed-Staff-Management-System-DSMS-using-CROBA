package failuredetectionSubSystem;

import java.util.Timer;
import java.util.TimerTask;

import Center.ApplyOperations;
import Center.Message;
import Center.ServersInfo;
import udp.MessageExchange.SendDoOperationMessage;

public class PingServers {
	
	private ServersInfo server1info;
	private ServersInfo server2info;
	private ApplyOperations thisServer;

	public PingServers(ServersInfo server1info , ServersInfo server2info , 
			ApplyOperations thisServer, int priority ){
		this.server1info = server1info;
		this.server2info = server2info; 
		this.thisServer = thisServer;
		statTimer();
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
		if(server1info.isStillWorking()){
			checkServeOne();
		}
		if(server2info.isStillWorking()){
			checkServerTwo();
		}
	}
	
	private void checkServeOne(){
		SendDoOperationMessage pingMessage = new SendDoOperationMessage(server1info.getPort(), new Message());
		pingMessage.start();
		
		try {
			pingMessage.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(pingMessage.getResultResponse().trim().equalsIgnoreCase("noResponse")){
			  if(server1info.isLeader()){
				  new BullyAlgorithm(server1info , server2info ,thisServer );
			  }
			  	server1info.setStillWorking(false);
		}
	}
	
	private void checkServerTwo(){
		SendDoOperationMessage pingMessage = new SendDoOperationMessage(server2info.getPort(),new Message());
		pingMessage.start();
		
		try {
			pingMessage.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(pingMessage.getResultResponse().trim().equalsIgnoreCase("noResponse")){
			  if(server1info.isLeader()){
				  new BullyAlgorithm(server1info , server2info ,thisServer ); 
			  }
			  server2info.setStillWorking(false);
		}
		
	}
	
	public void newLeader(int LeaderPortport){
		
		if(server1info.getPort() == LeaderPortport){
			server1info.setLeader(true);
		}else if(server2info.getPort() == LeaderPortport){
			server2info.setLeader(true);
		}else{
			server1info.setLeader(false);
			server2info.setLeader(false);
		}
	}
}
