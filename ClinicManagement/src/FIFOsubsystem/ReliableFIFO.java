package FIFOsubsystem;

import java.util.ArrayList;

import servers.ApplyOperations;
import udp.MessageExchange.Send_Request_LostMessage;
import udp.MessageExchange.WaitDoOperationMessage;

public class ReliableFIFO {

	private int messageSeqnence;
	private int serverPort;
	private ArrayList<Message> messagess;
	private boolean manager = false;
	private ApplyOperations server;
	private InvolkeMethod involkeMethod; 
	private int listenerPort;
	
	public ReliableFIFO(boolean manager ,int listenerPort ,  int ManagerPort, ApplyOperations server){
		   messagess = new ArrayList<Message>();
		   this.serverPort = ManagerPort;
		   this.manager = manager;
	       messageSeqnence = 1;
	       this.server = server;
	       involkeMethod = new InvolkeMethod();
	       this.listenerPort = listenerPort;
	       activateListenerThread();
	}
	
	   public void recieive(Message message){
		   if(message.isResend()){
			   sendLostMessages(message);
		   }else{
		   messagess.add(message); 
		   deliver(message);
		   }
	   }
	   
	   private void deliver(Message message)
	   {
	      int expectedMessageSeqnence ;
	      expectedMessageSeqnence = messageSeqnence; 
	      if (expectedMessageSeqnence != message.getSequenceNum())
	      {
	    	  requestLostMessage(message , expectedMessageSeqnence);
	      }
	      	handleMessageBySequence();
	   }
	   
	   private void handleMessageBySequence()
	   {
	      boolean arrived = false;
	      ArrayList<Message> copy =  messagess;
	      for(int i =0 ; i< copy.size(); i ++){
	    	  Message message = copy.get(i);
	          if (message.getSequenceNum()== messageSeqnence)
	          {
	         	  dealwithMessage(message);
	         	  messageSeqnence = messageSeqnence + 1;
	         	   arrived = true;
	          }
	       }

	       if (arrived){ 
	    	   handleMessageBySequence();
	       }
	    }
	   
	   
	   public void dealwithMessage(Message message) {
		   if(manager){
			    involkeMethod.ApplyOperationToManager(server, message);
		   }else{
			   involkeMethod.ApplyOperationToBackup(server, message);
		   }
		  
	   }
	    

	  
	   private void requestLostMessage(Message message , int messageSequense)
	   {
		      int missingSeqNum = messageSequense;
		      while (missingSeqNum < message.getSequenceNum())
		      {
		         if (! tryToFindMessage(missingSeqNum))
		         {
		        	 Message requestMessage = new Message(0 , missingSeqNum );
		        	 requestMessage.setResend(true);
		        	 requestMessage.setSenderPort(serverPort);
		        	 new Send_Request_LostMessage(message).start();
		         }
		         missingSeqNum++;
		      }
	      
	   }
	   
	   
	   private boolean tryToFindMessage(int missingSeqNum ){
		   ArrayList<Message> copy =  messagess;
		   for(int i =0 ; i< copy.size(); i ++){
		    	Message message = copy.get(i);
		          if (message.getSequenceNum()== messageSeqnence)
		          {
		        	  return true;
		          }
		   }
		   return false;
	   }
	   
	   private void sendLostMessages(Message missingSeqNum){
		   ArrayList<Message> copy =  messagess;
		   for(int i =0 ; i< copy.size(); i ++){
			   Message message = copy.get(i);
			   if(message.getSequenceNum() == missingSeqNum.getSequenceNum()){
				   message.setSenderPort(missingSeqNum.getSenderPort());
				   new Send_Request_LostMessage(message).start();
			   }
		   }
		   
	   }
	   
	   /**
	    *  if the a backup became a leader it has to have the frontEnds port 
	    *  to request lost messages 
	    * @param manager   
	    * @param newPort 
	    */
	   public void managerHasChanged(boolean manager,int newPort){
		   if(manager){
			   this.serverPort = newPort;
			   this.manager = manager;
		   }else{
			   this.serverPort = newPort;
		   }
	   }
	   

		
		private void activateListenerThread(){
			new WaitDoOperationMessage(listenerPort, this).start();
		}
}
