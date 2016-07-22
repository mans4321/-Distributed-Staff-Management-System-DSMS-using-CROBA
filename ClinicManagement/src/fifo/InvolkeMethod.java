package fifo;

import Center.ServerOperations;
import Center.Message;
import udp.ClientOperationMessage;

public class InvolkeMethod {

	public InvolkeMethod(){
	}
	
		 
	 public void ApplyOperationToManager( ServerOperations server , Message message){
		 
		 int operationID = message.getOperationID();
		 String result = "";
			
		 switch(operationID){
		 
		 	case 1:
		 		result = server.createDRecord(message.getManagerID(), message.getFirstName(), 
		 	    		 message.getLastName(), message.getAddress(), message.getPhone(), 
		 	    		 message.getSpecialization(), message.getLocation(), message.getSequenceNum());
		 		System.out.println("ApplyOperationToManager  "  + message.getSequenceNum() );
		 		message.setResponse(result);
		 		sendResult(message);
		 		
		 		break;
		 	case 2: 
		 		result = server.createNRecord(message.getManagerID(), message.getFirstName(),
		 				 message.getLastName(), message.getDesignation(), 
		 				 message.getStatus() , message.getStatusDate(), message.getSequenceNum());
		 		message.setResponse(result);
		 		sendResult(message);
		 		break;
		 	case 3 :
		 		result = server.editRecord(message.getManagerID(), message.getRecordID(),
		 				 message.getFieldName(), message.getNewValue(), message.getSequenceNum());
		 		message.setResponse(result);
		 		sendResult(message);
		 		break;
		 	case 4:
		 		result = server.getRecordCount(message.getManagerID(), message.getCountType(), message.getSequenceNum());
		 		message.setResponse(result);
		 		sendResult(message);
		 		break;
		 	case 5 :
		 	    result = server.transferRecord(message.getManagerID(), message.getRecordID(),
		 	    		 message.getLocation(), message.getSequenceNum());
		 	    message.setResponse(result);
		 	    sendResult(message);
		 	    break;
		 }
	 }

	 public void ApplyOperationToBackup( ServerOperations server , Message message){
		 
		 int operationID = message.getOperationID();
		 String result = "";
			
		 switch(operationID){
		 
		 	case 1:
		 		result = server.getDatabase().addRecord(message.getRecord());
		 		message.setResponse(result);
		 		System.out.println("ApplyOperationToBackup  "  + message.getResponse()  + message.getSequenceNum()  );
		 		
		 		sendResult(message);
		 		
		 		break;
		 	case 2: 
		 		result = server.getDatabase().addRecord(message.getRecord());
		 		message.setResponse(result);
		 		sendResult(message);
		 		break;
		 	case 3 :
		 		result = server.getDatabase().editRecord(message.getRecordID(),
		 				 message.getFieldName(), message.getNewValue()).getStatusMessage();
		 		message.setResponse(result);
		 		sendResult(message);
		 		break;
		 	case 4:
		 		message.setResponse("done");
		 		sendResult(message);
		 		break;
		 	case 5 :
		 	    result = server.getDatabase().deleteRecord(message.getRecordID());
		 	    message.setResponse(result);
		 	    sendResult(message);
		 	    break;
		 }
	 }


   private void sendResult(Message message){
	   System.out.println("sendResult  "  + message.getResponse()  + message.getSequenceNum()  );
	   new ClientOperationMessage(message).start();
   }
	 
	 
}
