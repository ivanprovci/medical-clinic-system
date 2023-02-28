import java.util.Scanner;

public class Account {
	
	//variable/attributes
	private String email;
	private String password;
	private String fistname;
	private String lastName;
	private String phoneNumber;
	
	//constructor 
	public Account(String email, String password, String firstname, String lastName, String phoneNumber) {
		super();
		this.email = email;
		this.password = password;
		this.fistname = firstname;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
	}
	
	
	public Account(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}


	//getters and setters
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

	public String getFistname() {
		return fistname;
	}

	public void setFistname(String fistname) {
		this.fistname = fistname;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	//Check the password method
	public Boolean checkPassword(String password) {
		return this.password.equals(password);
	}
	
	//Login in method check if he can login successfull or not maybe use a boolean with if statements 
	public Boolean login(String password) {
		if(checkPassword(password)) {
		
			return true; //once the check password method run and if the password match login will be successful
		}else {
			
			return false;	//password not correct meaning login insuccessful 
		}
	}
	//Not sure about the notify user method 
	public void notifyUser(String message) {
	}
	
	//Create the patient class for the regesterNewPatient Method
	public PatientAccount registerNewPatient(String email, String password, String fistname, String lastName, String phoneNumber) {
		PatientAccount newPatient = new PatientAccount(email,password);
		
		
		return newPatient;
	}
	
	
	
	
	
	//edit account details only added in email add the rest
	public void editAccoutDetails() {
		Scanner scanner = new Scanner(System.in);
		
	System.out.println("Enter New Email");
	String newEmail = scanner.nextLine();
	
	//update the record 
	//add in the other info the the accounts password,names, and phone numebr 
	if(!newEmail.isEmpty()) {	//if newEamil in not empty excute the code 
		this.email = newEmail;
	}
	}
	
	
	
	

}
