package FIFOsubsystem;

import java.util.ArrayList;

import udp.MessageExchange.SendDoOperationMessage;
import udp.MessageExchange.Send_Request_LostMessage;

public class SendMessage {


	private ArrayList<Message> messages;
	private boolean frontEnd = false;
	private int managerPort;
	private int backupPort1;
	private int backupPort2;
	private boolean backup1Working;
	private boolean backup2Working;
	
	public SendMessage(){
		messages = new ArrayList<Message>(); 
		backup1Working = true;
		backup2Working = true;
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
		System.out.println("DollardFrontEnd  sending message");
		SendDoOperationMessage manager = new SendDoOperationMessage(managerPort , message);
		manager.start();
		try {
			manager.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("manager respobse :" + manager.getResultResponse());
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
	
	/**
	 * this f() only used by the FE to send lost to leader
	 * @param message
	 */
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
	
	
	
	   public void sendLostMessages(Message missingSeqNum){
		   ArrayList<Message> copy =  messages;
		   for(int i =0 ; i< copy.size(); i ++){
			   Message message = copy.get(i);
			   if(message.getSequenceNum() == missingSeqNum.getSequenceNum()){
				   message.setSenderPort(missingSeqNum.getSenderPort());
				   new Send_Request_LostMessage(message).start();
			   }
		   }
		   
	   }
		
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
