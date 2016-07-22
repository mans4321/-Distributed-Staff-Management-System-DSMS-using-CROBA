package clients;

import java.io.BufferedReader;
import java.io.FileReader;

import org.omg.CORBA.ORB;

import corbafiles.RemotInterface;
import corbafiles.RemotInterfaceHelper;

public class ClincManagerTestConcurrency {
	
	private RemotInterface server = null;
	public ClincManagerTestConcurrency(String server){
		connectToServer(server);
		
	}
	
	 private  void connectToServer(String server){
	    	String []arg = new String[2];
	    	switch(server){
	    	case"MTL":
	    		arg[0]="ORBInitialPort 900";
	    		arg[1]="ORBInitialHost localhost";
	    		connectToServer(arg , "Montreal.txt");
	    		break;
	    	case"LVL":
	    		arg[0]="ORBInitialPort 1050";
	    		arg[1]="ORBInitialHost localhost";
	    		connectToServer(arg ,"Laval.txt");
	    		break;
	    	case"DDO":
	    		arg[0]="ORBInitialPort 30000";
	    		arg[1]="ORBInitialHost localhost";
	    		connectToServer(arg, "Dollard.txt");
	    		break;
	    	}
	    	
	    	}
	    	  private  void connectToServer(String []args, String fileName)  {
	    			ORB orb = ORB.init(args, null);
	    			String ior = null;
	    			try{
	    			BufferedReader br = new BufferedReader(new FileReader(fileName));
	    			ior = br.readLine();
	    			br.close();
	    			org.omg.CORBA.Object obj = orb.string_to_object(ior);
	    			server = RemotInterfaceHelper.narrow(obj);
	    			}catch(Exception e){
	    				e.printStackTrace(System.out);
	    			}
	    			
	    		}

			public RemotInterface getServer() {
				return server;
			}

	    	  
	    	  
	    	
}
