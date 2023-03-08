
public class PatientAccount extends Account {
	
	private Boolean isVerified;
	private StaffAccount verifiedBy;
	private String address;
	private String healthNO;
	//private ConfidentialRecord relatedRecords;

	public PatientAccount(String email, String password) {
		super(email, password);
		// TODO Auto-generated constructor stub
	}
	//Getters and Setters

	public Boolean getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(Boolean isVerified) {
		this.isVerified = isVerified;
	}

	public StaffAccount getVerifiedBy() {
		return verifiedBy;
	}

	public void setVerifiedBy(StaffAccount verifiedBy) {
		this.verifiedBy = verifiedBy;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHealthNO() {
		return healthNO;
	}

	public void setHealthNO(String healthNO) {
		this.healthNO = healthNO;
	}
	
	
	
	//ViewDoctorProfile
	
	


	
	}


