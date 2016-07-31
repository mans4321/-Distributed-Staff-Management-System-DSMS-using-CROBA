package testUnit;

import Center.Message;
import fifo.FIFOMessages;
import servers_frontends.Dollard;

public class TestFIFO {

	
	FIFOMessages fifoMessages;
	Dollard ddo ;
	
	public TestFIFO() throws InterruptedException{
		
		ddo = new Dollard(1);
		
		fifoMessages = new FIFOMessages(true, 40077, ddo);
		
		Message message = new Message(1, 2, "testManager", "mansour","Alzahrani","231 Murry", 
				"0551900","Not Doctor", "MTL");
		message.setResponse("test");
		
		fifoMessages.recieivefifo(message);
		
		Thread.sleep(3000);

		
		Message message2 = new Message(1, 1, "testManager", "Saad","Alzahrani","231 Murry", 
				"0551900","Not Doctor", "MTL");
		message2.setResponse("test");
		fifoMessages.recieivefifo(message2);
		
		
	}
	
	
	public static void main(String []args) throws InterruptedException{
		new TestFIFO();
	}
	

}
