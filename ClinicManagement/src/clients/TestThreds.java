package clients;

import corbafiles.RemotInterface;

public class TestThreds {

	volatile Integer    nurseUniqueId = 1;
	volatile Integer   doctorUniqueId = 1;
	
	public TestThreds() throws InterruptedException  {
		new TestDDO().start();
		Thread.sleep(5000);
		new TestMTL().start();
		Thread.sleep(5000);
		new TestLVL().start();
	}
	
	public static void main(String[]args) throws InterruptedException{
		new TestThreds();
	}
	
	
	  private class TestDDO extends Thread{
		  public void run(){
			  ClincManagerTestConcurrency  testConcurrency = new  ClincManagerTestConcurrency("DDO");
			  RemotInterface server = testConcurrency.getServer();
			  doAdd(server);
			  doTransfer(server,"LVL");
			  doGetCount(server);
		  }
	  }
	  
	  private class TestMTL extends Thread{
		  public void run(){
			  ClincManagerTestConcurrency  testConcurrency = new  ClincManagerTestConcurrency("MTL");
			  RemotInterface server = testConcurrency.getServer();
			  doAdd(server);
			  doTransfer(server,"LVL");
			  doGetCount(server);
		  }
		}

	  
	 private class TestLVL extends Thread{
		  public void run(){
			  ClincManagerTestConcurrency  testConcurrency = new  ClincManagerTestConcurrency("LVL");
			  RemotInterface server = testConcurrency.getServer();
			  doAdd(server);
			 // doEdit(server);
			  doGetCount(server);
		  }

	  }
	 
	 
		private void doTransfer(RemotInterface server , String location) {
			String result;
			for(int i = 0 ; i<50 ;i++){
		  	result = server.transferRecord("testManager", getNextId("Doctor"), location);
		  	System.out.println("transfer result :  "+ result );
			}
		}

		private void doGetCount(RemotInterface server) {
			for(int i = 0 ; i<10 ;i++){
			System.out.println(server.getRecordCount("",1));
			}
		}

		private void doEdit(RemotInterface server) {
			 String result;
			 for(int i = 0 ; i<50 ;i++){
			result = server.editRecord("", getNextId("doctor"), "firstname", "uuuuuu");
		  	System.out.println(result); 
			 }
			
		}

	  
	  public void doAdd(RemotInterface server) {

		  String result;
		  for(int i = 0 ; i<1000 ;i++){
			  result = server.createDRecord("testManager", "firstName", "lastName", "address", "phone", "specialization", "location"); 
			  result = server.createNRecord("testManager", "jjame", "alastName", "designation", "phone", "specialization");
           
		  }
		  System.out.println("inserted 1000 DR and 1000 Nurses");
  }
	  
	  public String getNextId(String type) {
          //    synchronized()  only IDs
    
        if (type.equals("nurse")) {
            synchronized(nurseUniqueId){
            nurseUniqueId++;
            return "NR" + String.format("%05d", nurseUniqueId);
            }
      
        }
        synchronized(doctorUniqueId){
        doctorUniqueId++;
        return "DR" + String.format("%05d", doctorUniqueId);
        }
      
    }
    
}
