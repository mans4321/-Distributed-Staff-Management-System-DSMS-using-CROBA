package servers;

import servers.records.RecordManager;

public interface ApplyOperations {

	String createDRecord(String managerID, String firstName, String lastName, String address, String phone,
			String specialization, String location);

	String createNRecord(String managerID, String firstName, String lastName, String designation, String status,
			String statusDate);

	String editRecord(String managerID, String recordID, String fieldName, String newValue);

	String getRecordCount(String managerID, int countType);

	String transferRecord(String managerID, String recordID, String location);
	
	RecordManager getDatabase();
}
