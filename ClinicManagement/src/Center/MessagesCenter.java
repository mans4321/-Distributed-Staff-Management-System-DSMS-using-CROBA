package Center;

import FIFOSystem.FIFOMessages;
import udp.MessageExchange.Send_Requst_Result;
import udp.MessageExchange.WaitDoOperationMessage;

public class MessagesCenter {


	private ApplyOperations server;
	private int listenerPort;
	private FIFOMessages fifo; 
	
	public MessagesCenter(boolean manager ,int listenerPort ,  int ManagerPort, ApplyOperations server){
	       this.server = server;
	       this.listenerPort = listenerPort;
	       fifo = new FIFOMessages(manager,ManagerPort,server);
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
			        new Send_Requst_Result(message);  
		   	} else{
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
			new WaitDoOperationMessage(listenerPort, this).start();
		}
}
