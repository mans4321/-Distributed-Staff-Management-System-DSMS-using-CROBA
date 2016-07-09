package utilities;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

import servers.records.Record;


public class RmiLogger {
    private Logger logger;
    private String fileName;

    public RmiLogger(String fileName, String origin) 
    {
        this.logger = Logger.getLogger(fileName);
        this.fileName = fileName;
        try {
            FileHandler handler = new FileHandler(this.fileName + ".log", true);
            logger.addHandler(handler);
        } catch (Exception e ) {
            e.getMessage();
        }       
    }
    
    public boolean log(boolean success, String message)
    {
        String type = "FAIL";
        if (success) {
            type = "SUCCESS";
        }
        
        String logMessage = new String(type + ": " + message);
        logger.info(logMessage);
        return true;
        
    }
    
    public boolean log(String message)
    {
        String logMessage = new String("message" + ": " + message);
        logger.info(logMessage);
        return true;
        
    }
    
    public boolean log(Record record)
    {
        String type = "FAIL";
        if (record.isSuccessful()) {
            type = "SUCCESS";
        }
        
        String logMessage = new String(type + ": " + record.getStatusMessage());
        logger.info(logMessage);
        return true;
        
    }
}