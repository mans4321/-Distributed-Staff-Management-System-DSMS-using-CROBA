package utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import servers.records.Record;


public class DealWithUndeletedRecord {
	

	    private String File;
	    private Record record;
	    public DealWithUndeletedRecord(Record record) {	
	    	this.File = record.getId();
	    	this.record = record ;
	    }
	    
	    public void doSaveAsString() {

	        File selectedFile = new File(File+".txt");
	        PrintWriter out  = null; 
	        try {
	            FileOutputStream stream = new FileOutputStream(selectedFile); 
	            out = new PrintWriter( stream );
	            out.println(record.toString());;
	            out.close();
	        }
	        catch (Exception e) {
	        	if(out !=null )
					out.close();
	        	}
	    }

		public Record getRecord() {
			return record;
		}

		public void setRecord(Record record) {
			this.record = record;
		}  
	    
}
