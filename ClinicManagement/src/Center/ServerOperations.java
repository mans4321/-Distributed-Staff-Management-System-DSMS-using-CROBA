package Center;

import servers.records.RecordManager;

public interface ServerOperations {

	String createDRecord(String managerID, String firstName, String lastName, String address, String phone,
			String specialization, String location, int sequenceNum);

	String createNRecord(String managerID, String firstName, String lastName, String designation, String status,
			String statusDate, int sequenceNum);

	String editRecord(String managerID, String recordID, String fieldName, String newValue, int sequenceNum);

	String getRecordCount(String managerID, int countType, int sequenceNum);

	String transferRecord(String managerID, String recordID, String location, int sequenceNum);
	
	RecordManager getDatabase();
	
	void leaderChanged(boolean manager , int port);
}
