package com.kpu.student.Project;
public class StaffAccount extends Account {

	public StaffAccount(String email, String password) {
		super(email, password);
		// TODO Auto-generated constructor stub
	}

	//move notifyUser from class "Account" because of professor's feedbacks
public void notifyUser(String message){
	}

	private void verifyPatient (PatientAccount patient) {
	}
	private void createAccount(String email, String password, int accountType) {	
	}
	private void deleteAccount(String email) {
	}
	private void generateReport(int reportType, boolean isAnnualReport) {	
	}
    private void inputLabResult() {
    	
    }
    private void modifyLabResult() {
    	
    }
    private void DeleteLabResult() {
    	
    }
}
