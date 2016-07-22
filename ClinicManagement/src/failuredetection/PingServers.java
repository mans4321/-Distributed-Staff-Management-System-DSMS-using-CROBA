package failuredetection;

import java.util.Timer;
import java.util.TimerTask;

import Center.AllServersInfo;
import Center.Message;
import Center.ServerInfo;
import Center.ServerOperations;
import udp.SendingOperationMessage;

public class PingServers {
	
	private ServerInfo server1info;
	private ServerInfo server2info;
	private AllServersInfo allServers;
	private ServerOperations thisServer;
	private int processID;  
	public PingServers(ServerInfo server1info , ServerInfo server2info , 
			AllServersInfo allServers, ServerOperations thisServer, int processID ){
		this.server1info = server1info;
		this.server2info = server2info; 
		this.allServers =  allServers;
		this.thisServer = thisServer;
		this.processID = processID;
		statTimer();
		System.out.println("PingServers " +  processID);
	}

	
	private void statTimer(){
		Timer timer;
	    timer = new Timer();
	    TimerTask TimerTask = new TimerTask(){
	    	public void run(){
	    		checkServes();
	    	}
	    };
	    timer.scheduleAtFixedRate(TimerTask, 1000 * 10 , 5000);
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
		SendingOperationMessage pingMessage = new SendingOperationMessage(server1info.getPort(), 
																			new Message(false));
		pingMessage.setTimeOut(1000);
		pingMessage.start();
		
		try {
			pingMessage.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(pingMessage.getResultResponse().trim().equalsIgnoreCase("noResponse")){
			  if(server1info.isLeader()){
				  new BullyAlgorithm(processID, allServers ,thisServer );
				  System.out.println("PingServers, BullyAlgorithm " +  processID);
			  }
			  	server1info.setStillWorking(false);
		}
	}
	
	private void checkServerTwo(){
		SendingOperationMessage pingMessage = new SendingOperationMessage(server2info.getPort(),new Message(false));
		pingMessage.setTimeOut(1000);
		pingMessage.start();
		
		try {
			pingMessage.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(pingMessage.getResultResponse().trim().equalsIgnoreCase("noResponse")){
			  if(server1info.isLeader()){
				  new BullyAlgorithm(processID , allServers , thisServer ); 
				  System.out.println("PingServers, BullyAlgorithm " +  processID);
			  }
			  server2info.setStillWorking(false);
		}
		
	}
	
	public void newLeader(int LeaderPortport){
		
		 System.out.println("PingServers, newLeader " +  processID);
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
