package com.kpu.student.Project;
public class Account {

	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private String phoneNo;
	
	//create constructor "Account" to pass "Account" object containing email to "retrieveAccountInfo" method (DatabaseAccessor class)
	public Account(String email, String password) {
		this.setEmail(email);
		this.setPassword(password);
	}
	
	public boolean checkPassword(String password) {
		return null != null;
	}
	
	public void login(String password){
		
	}
	
    public static PatientAccount registerNewPatient(String email, String password) {
    	PatientAccount newPatient = new PatientAccount(email,password);
    	newPatient.setVerified(false);
    	return newPatient;
    }
    
    public void editAccountDetails() {}
   //methods I created because of professor's feedbacks
    public void viewPrescription() {
    	
    }
    public void viewLabResult() {
    	
    }
    public void viewLabExam() {
    	
    }
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}

