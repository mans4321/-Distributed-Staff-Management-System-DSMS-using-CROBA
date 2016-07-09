package clients;

import RemotInterfaceApp.RemotInterface;

public class TestThreds {

	volatile Integer    nurseUniqueId = 000000;
	volatile Integer   doctorUniqueId = 000000;
	
	public TestThreds(){
	for(int i =0 ; i<2; i++){
			new TestDDO().start();
//			new TestMTL().start();
//			new TestLVL().start();
		}
	}
	public static void main(String[]args){
		new TestThreds();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			System.out.println("----------------------Couldnot sleep");
		}
	}
	
	  private class TestDDO extends Thread{
		  public void run(){
			  ClincManagerTestConcurrency  testConcurrency = new  ClincManagerTestConcurrency("DDO");
			  RemotInterface server = testConcurrency.getServer();
			  doOpertion(server);
		  }
	  }
	  
//	  private class TestMTL extends Thread{
//		  public void run(){
//			  ClincManagerTestConcurrency  testConcurrency = new  ClincManagerTestConcurrency("MTL");
//			  RemotInterface server = testConcurrency.getServer();
//			  doOpertion(server);
//		  }
//	  }
//	  
//	  private class TestLVL extends Thread{
//		  public void run(){
//			  ClincManagerTestConcurrency  testConcurrency = new  ClincManagerTestConcurrency("LVL");
//			  RemotInterface server = testConcurrency.getServer();
//			  doOpertion(server);
//		  }
//	  }
	  
	  public void doOpertion(RemotInterface remoteInterface) {
		  RemotInterface server = remoteInterface; 
		  String result;
           result = server.createDRecord("testManager", "firstName", "lastName", "address", "phone", "specialization", "location");
          System.out.println(result);  
          
//
//          result = server.createNRecord("testManager", "jjame", "alastName", "designation", "phone", "specialization");
//          System.out.println(result); 
          
//
//		  	result = server.editRecord("", getNextId("doctor"), "firstname", "uuuuuu");
//          	System.out.println(result); 
////          
//
             System.out.println(server.getRecordCount("",1));
          
		  	result = server.transferRecord("testManager", "DR00002", "LVL");
		  	System.out.println("transfer result :  "+ result );
//		  	try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		  	 System.out.println(server.getRecordCount("",1));
  }
	  
	  public String getNextId(String type) {
          //    synchronized()  only IDs
    
        if (type.equals("nurse")) {
            synchronized(nurseUniqueId){
            nurseUniqueId++;
            }
            return "NR" + String.format("%05d", nurseUniqueId);
        }
        
        synchronized(doctorUniqueId){
        doctorUniqueId++;
        }
        return "DR" + String.format("%05d", doctorUniqueId);
    }
    
}
