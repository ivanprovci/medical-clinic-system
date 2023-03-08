package com.kpu.student.Project;

public class StaffAccount extends Account {

		
		public StaffAccount(String email, String password) {
		super(email, password);
		// TODO Auto-generated constructor stub
	}
		

		//Method to verifyPatient 
		public void verifyPatient(PatientAccount patient) {
			patient.setVerified(true);
			patient.setVerifiedBy(this);
			
		}
		//Method Create account 
		public Account createAccount(String email, String password, char accountType) {
			//Switched accountType from int to char because I think it's easier to understand - Liam
			Account newAccount;
			switch(accountType) {
			case 'p':
			case 'P': //'p' or 'P'
				newAccount = new PatientAccount(email, password);
				break;
			case 'd':
			case 'D'://'d' or 'D'
				newAccount = new DoctorAccount(email, password);
				break;
			case 's':
			case 'S'://'s' or 'S'
				newAccount = new StaffAccount(email, password);
				break;
			default:
				newAccount = null;
				break;	
			}
			return newAccount;
			
		//Method deleteAccount
			
		}
}
