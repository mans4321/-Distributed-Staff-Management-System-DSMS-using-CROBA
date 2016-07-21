package fifo;

import java.util.ArrayList;

import Center.ServerOperations;
import Center.Message;
import udp.ClientOperationMessage;

public class FIFOMessages {

	private int messageSeqnence;
	private int serverPort;
	private ArrayList<Message> messagess;
	private boolean manager = false;
	private InvolkeMethod involkeMethod; 
	private ServerOperations server;
	
	public FIFOMessages(boolean manager, int managerPort, ServerOperations server){
		   messagess = new ArrayList<Message>();
		   involkeMethod = new InvolkeMethod();
		   this.server = server;
		   this.serverPort = managerPort;
		   this.manager = manager;
	       messageSeqnence = 1;
	}
	
	
	public void recieivefifo(Message message){
		
		System.out.println("recieive fifo  " + message.getSequenceNum()  + "   " + messageSeqnence);
		if(message.isResend()){
			System.out.println("message.isResend()" + message.getSequenceNum()  + "" + messageSeqnence);
			sendLostMessages(message);
		}else{
			System.out.println("else " + message.getSequenceNum()  + "  " + messageSeqnence);
			messagess.add(message);
			deliver(message);
		}
	
	}
	
	   private void deliver(Message message)
	   {
		  
		   System.out.println("deliver  " + message.getSequenceNum() + "    " + messageSeqnence);
	      int expectedMessageSeqnence ;
	      expectedMessageSeqnence = messageSeqnence; 
	      if (expectedMessageSeqnence != message.getSequenceNum())
	      {
	    	  System.out.println("requestLostMessage" + message.getSequenceNum() + "" + messageSeqnence);
	    	  requestLostMessage(message , expectedMessageSeqnence);
	      }
	      	handleMessageBySequence();
	   }
	   
	   private void handleMessageBySequence()
	   {
		   System.out.println("handleMessageBySequence  " + messageSeqnence);
	      boolean arrived = false;
	      ArrayList<Message> copy =  messagess;
	      for(int i =0 ; i< copy.size(); i ++){
	    	  Message message = copy.get(i);
	          if (message.getSequenceNum() ==  messageSeqnence)
	          {
	        	  System.out.println("deliver" + messageSeqnence);
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
			    System.out.println("dealwithMessage  ApplyOperationToManager " + messageSeqnence);
			 
		   }else{
			   involkeMethod.ApplyOperationToBackup(server, message);
			   System.out.println("dealwithMessage  ApplyOperationToBackup " + messageSeqnence);
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
		        	 new ClientOperationMessage(message).start();
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
				   new ClientOperationMessage(message).start();
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
	   
	   
}
