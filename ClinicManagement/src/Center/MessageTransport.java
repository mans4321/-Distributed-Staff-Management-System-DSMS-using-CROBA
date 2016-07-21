package Center;

import java.util.ArrayList;
import java.util.LinkedList;

import udp.MessageExchange.SendDoOperationMessage;
import udp.MessageExchange.Send_Requst_Result;

public class MessageTransport {


	private ArrayList<Message> messages;
	private boolean frontEnd;;
	private int managerPort;
	private int backupPort1;
	private int backupPort2;
	private boolean backup1Working;
	private boolean backup2Working;
	private LinkedList<Message> managerLost;
	
	public MessageTransport(int backup1Port, int backup2Port ){
		
		messages = new ArrayList<Message>(); 
		this.backupPort1 = backup1Port;
		this.backupPort2 = backup2Port;
		backup1Working = true;
		backup2Working = true;
		frontEnd = false;
		
	}
	
	public MessageTransport(int managerPort ){
		messages = new ArrayList<Message>(); 
		managerLost = new LinkedList<Message>();
		this.managerPort = managerPort; 
		frontEnd = true;
		
	}
	

	
	public String sendTo(Message message){
		if(frontEnd){
		return sendToManager(message) ;
		}else{
			 sendToBackup(message);
			 return"";
		}
	}
	public String sendToManager(Message message){
		messages.add(message);
		
		SendDoOperationMessage manager = new SendDoOperationMessage(managerPort , message);
		manager.start();
		try {
			manager.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("manager respobse :" + manager.getResultResponse());
		
		if(manager.getResultResponse().trim().equalsIgnoreCase("noResponse")){
			
			managerLost.add(message);                                                /// what should do 
		}
		return manager.getResultResponse();
	}
	
	public void sendToBackup(Message message){
		messages.add(message);
		if(backup1Working){
			sendToBackup1(message);
		}
		if(backup2Working){
			sendToBackup2(message);
		}
		
	}
	
	
	private void sendToBackup1(Message message){
		SendDoOperationMessage backup1 = new SendDoOperationMessage(backupPort1, message);
		backup1.start();
		try {
			backup1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("backup respobse :" + backup1.getResultResponse());
		if(backup1.getResultResponse().trim().equalsIgnoreCase("noResponse")){
			backup1Working = false;
		}
	}
	

	private void sendToBackup2(Message message){
		
		SendDoOperationMessage backup2 = new SendDoOperationMessage(backupPort2, message);
		backup2.start();
		try {
			backup2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("backup respobse :" +backup2.getResultResponse());
		if(backup2.getResultResponse().trim().equalsIgnoreCase("noResponse")){
			backup2Working = false;
		}
		
	}
	
///-----------------------------------(Front End only )	----------------------------------------------------------------
	
	/**
	 * this f() only used by the FE to receive lost messages request  & new Leader info
	 * @param message
	 */
	
	   public void recieive(Message message){
		   if(message.isPingSystem()){
			   ChangeLead(message);
		   }else{
			   sendLostMessages(message);
		   }
	   }
	
	
	 private void sendLostMessages(Message missingSeqNum){
		   ArrayList<Message> copy =  messages;
		   for(int i =0 ; i< copy.size(); i ++){
			   Message message = copy.get(i);
			   if(message.getSequenceNum() == missingSeqNum.getSequenceNum()){
				   message.setSenderPort(missingSeqNum.getSenderPort());
				   new Send_Requst_Result(message).start();
			   }
		   }
		 
	 }
		
	
	 private void ChangeLead(Message message){
		 managerPort = message.getLeaderPort();
		 
		 for(Message messageLost : managerLost ){
			 sendToManager(messageLost);
		 }
	 }
	 
	 
///---------------------------------------------------------------------------------------------------	 
	public void setBackupPort1(int backupPort1) {
		this.backupPort1 = backupPort1;
	}

	public void setBackupPort2(int backupPort2) {
		this.backupPort2 = backupPort2;
	}

	public void setManagerPort(int port) {
		this.managerPort = port;
	}

	public boolean isFrontEnd() {
		return frontEnd;
	}

	public void setFrontEnd(boolean frontEnd) {
		this.frontEnd = frontEnd;
	}
	
	
	
}