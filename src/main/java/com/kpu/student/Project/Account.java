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
	
	public Account(String email, String password, String firstname, String lastName, String phoneNumber) {
		this.email = email;
		this.password = password;
		this.firstName = firstname;
		this.lastName = lastName;
		this.phoneNo = phoneNumber;
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
	
	//Check the password method
		public Boolean checkPassword(String password) {
			return this.password.equals(password);
			//This method might be redundant, we already have retrievePasswordHash() in DatabaseAccessor - Liam
		}
		
		//Login in method check if he can login successfull or not maybe use a boolean with if statements 
		public Boolean login(String password) {
			if(checkPassword(password)) {
			
				return true; //once the check password method run and if the password match login will be successful
			}else {
				
				return false;	//password not correct meaning login insuccessful 
			}
		}
		
		public void notifyUser(String message) {
			//Send an email to this.getEmail() with 'message' as the contents
			//Not sure how to implement this yet - Liam
		}
		
		//Create the patient class for the regesterNewPatient Method
		public PatientAccount registerNewPatient(String email, String password, String firstName, String lastName, String phoneNumber) {
			PatientAccount newPatient = new PatientAccount(email,password);
			
			
			return newPatient;
		}
}

