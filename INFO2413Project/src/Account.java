
public class Account {

	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private String phoneNo;
	
	//create constructor "Account" to pass "Account" object containing email to "retrieveAccountInfo" method (DatabaseAccessor class)
	public Account(String email) {
	}
	public boolean checkPassword(String password) {
		return null != null;
	}
	public void login(String password){
		
	}
    public PatientAccount registerNewPatient(String email, String password) {
    	PatientAccount newPatient = new PatientAccount(email,password);
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
}

