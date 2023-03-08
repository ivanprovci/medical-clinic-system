package com.kpu.student.Project;
public class DatabaseAccessor {

	public static String retrievePasswordHash(String email) {
		return "";
	}
	public static Account retrieveAccountInfo(String email) {
		Account retrievedAccount = new Account("", "");
		
		return retrievedAccount;
	}
	public static ConfidentialRecord retrieveRecord (int recordID) {
		ConfidentialRecord retrievedRecord = new ConfidentialRecord(0, null, null);
		return retrievedRecord;
	}
	public static void addAccount (Account newAccount) {
		
	}
	public static void addRecord(ConfidentialRecord newRecord) {
		
	}
}
