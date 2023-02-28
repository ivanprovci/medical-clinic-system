public class StaffAccount extends Account {

		
		public StaffAccount(String email, String password) {
		super(email, password);
		// TODO Auto-generated constructor stub
	}
		

		//Method to verifyPatient 
		public void verifyPatient(PatientAccount patient) {
			patient.setIsVerified(true);
			patient.setVerifiedBy(this);
			
		}
		//Method Create account 
		public Account createAccount(String email, String password, int accountType) {
			Account newAccount;
			switch(accountType) {
			case 1:
				newAccount = new PatientAccount(email, password);
				break;
			case 2:
				newAccount = new DoctorAccount(email, password);
				break;
			case 3:
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
