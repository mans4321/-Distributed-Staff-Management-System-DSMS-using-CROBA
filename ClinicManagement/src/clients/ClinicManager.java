package clients;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

import org.omg.CORBA.ORB;

import corbafiles.RemotInterface;
import corbafiles.RemotInterfaceHelper;
import utilities.RmiLogger;

public class ClinicManager {

    static HashMap<String,Integer> availableServers =  new HashMap<String, Integer>();
	static RemotInterface server = null;
    //Return basic menu.
    public static void identifyMenu()
    {
        System.out.println("Please enter manager ID: ");
    }
    
    public static void showMenu()
    {
        System.out.println("\n****Welcome to RMI****\n");
        System.out.println("Please select an option (1-4)");
        System.out.println("1. Create dummy doctor.");
        System.out.println("2. Create dummy nurse.");
        System.out.println("3. Edit dummy doctor");
        System.out.println("4. Edit dummy nurse");
        System.out.println("5. Get count");
        System.out.println("6.Transfered dummy Record");
        System.out.println("7. Exit");
    }
    
    public static void main(String[] args)  {
        ORB orb ;
        	
        setAvailableServers();
        int userChoice = 0;
        String userInput = "";
        Scanner keyboard = new Scanner(System.in);
        String managerIdLocation = "";
        String managerId = null;
        String message;
        RmiLogger logger = null;
        String result = null;
        identifyMenu();
        
        Boolean valid = false;
        
        // Enforces a valid integer input.
        while(!valid) {
            try {
                userInput = keyboard.nextLine();
                managerIdLocation = userInput.substring(0, 3);
                managerId = userInput.substring(3);

                if (validateLocation(managerIdLocation) && validateId(managerId)) {
                    valid = true;
                    logger = new RmiLogger(userInput, "client");
                } else {
                    throw new Exception();
                }
            } catch(Exception e) {
                System.out.println("Invalid Input, please enter an ID of the form ABC01234");
                valid = false;
                keyboard.nextLine();
            }
        }

        
        connectToServer(managerIdLocation = userInput.substring(0, 3));
        showMenu();

        while(true) {
            
            Boolean validAction = false;
            while(!validAction)
            {
                try{
                    System.out.println("getting valid input");

                    userChoice = keyboard.nextInt();
                    validAction = true;
                }
                catch(Exception e)
                {
                    System.out.println("Invalid Input, please enter an Integer");
                    validAction = false;
                    keyboard.nextLine();
                }
            }
            System.out.println(userChoice);
            // Manage user selection.
            switch(userChoice)
            {
            case 1: 
                message = "Creating dummy doctors";
                System.out.println(message);  
                result = server.createDRecord(managerId, "firstName", "lastName", "address", "phone", "specialization", "location");
                System.out.println(result);  
                logger.log(result);
                showMenu();
                break;
            case 2:
                message = "Creating dummy nurses";
                System.out.println(message);
                result = server.createNRecord(managerId, "jjame", "alastName", "designation", "phone", "specialization");
                System.out.println(result); 
                logger.log(result);
                showMenu();
                break;
            case 3:
                message = "Edit 00001 Doctor";
                System.out.println(message);
                result = server.editRecord("", "DR00001", "firstName", "uuuuuu");
                System.out.println(result); 
                logger.log(result);
                showMenu();
                break;
            case 4:
                message = "Edit 00001 Nurse";
                System.out.println(message);
                result = server.editRecord(managerId,"NR00001", "firstName", "aaaaaa");
                System.out.println(result); 
                logger.log(result);
                showMenu();
                break;
            case 5:
                message = "Getting record counts";
                System.out.println(message);
//                userInput = keyboard.next();
                System.out.println(server.getRecordCount("",1));
                showMenu();
                break;
            case 6:
            	System.out.println(server.getRecordCount(managerId,1));
            	System.out.println(result);
            	result = server.createDRecord(managerId, "Mansour", "saad", "321Murray", "05519340", "nothing", "DOD");
                System.out.println(result);  
                logger.log(result);
                result = server.transferRecord(managerId, "DR00001", "LVL");
                System.out.println("transfer result :  "+ result );
                result = server.getRecordCount(managerId, 1);
                System.out.println(""+result);
                break;
            case 7:
                System.out.println("Have a nice day!");
                keyboard.close();
                System.exit(0);
            default:
                System.out.println("Invalid Input, please try again.");
            }
        }
    }
    
    private static void connectToServer(String server){
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
    private static void connectToServer(String []args, String fileName)  {
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

	public static void setAvailableServers()
    {
        if (availableServers.isEmpty()) {
            availableServers.put("DDO", 30000);
            availableServers.put("MTL", 900);
            availableServers.put("LVL", 1050);
        }
    }

    public static String getServerAddress(String location) 
    {
        Integer serverInfo = availableServers.get(location);

        return "rmi://localhost:" + serverInfo + "/" + location.toLowerCase();
    }
    
    public static boolean validateLocation(String location) 
    {
    		location = location.toUpperCase().trim();
//        System.out.println(location);
        return availableServers.containsKey(location);
    }

    public static boolean validateId(String id)
    {
        
        if (id.matches("^\\d+$") && id.length() == 4) {
            return true;
        }

        return false;
    }
}
