package Center;

import java.util.ArrayList;

import failuredetection.BullyAlgorithm;
import fifo.FIFOMessages;
import udp.ClientOperationMessage;
import udp.ServerOperationMessage;

public class MessagesCenter {


	private ServerOperations server;
	private int listenerPort;
	private FIFOMessages fifo; 
	private int processID;
	private AllServersInfo allServers;
	
	public MessagesCenter(boolean manager ,int listenerPort ,  
							int ManagerPort, ServerOperations server, 
							int processID, AllServersInfo allServers){
	      
		   this.server = server;
	       this.listenerPort = listenerPort;
	       fifo = new FIFOMessages(manager,ManagerPort, server);
	       this.processID = processID;
	       this.allServers = allServers;
	       activateListenerThread();
	}
	
	
	
	   public void recieive(Message message){
		   
		   if(message.isPingSystem()){
			   handle(message);
		   }else{
			   fifo.recieive(message);
		   }
	   }
	   
	   
	   private void handle(Message message){
		   
		   if(message.isCheckAvailability()){
			   		message.setResponse("A_live");
			        new ClientOperationMessage(message);  
		   	}else if(message.isBullyAlgorithm()){
		   		new BullyAlgorithm(processID, allServers, server);
		   	}else{
		   		newLeaderMessage( false , message.getLeaderPort() );
		   	}
		   
	   }
	   
	   
	   private void newLeaderMessage(boolean manager, int port){
		   server.leaderChanged(manager, port);
	   }
	   

	   /**
	    *  if the a backup became a leader it has to have the frontEnds port 
	    *  to request lost messages 
	    * @param manager   
	    * @param newPort 
	    */
	   public void managerHasChanged(boolean manager,int newPort){
		   fifo.managerHasChanged(manager, newPort);
	   }
	   
		private void activateListenerThread(){
			new ServerOperationMessage(listenerPort, this).start();
		}
}
