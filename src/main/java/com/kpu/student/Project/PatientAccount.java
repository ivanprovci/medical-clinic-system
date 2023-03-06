package com.kpu.student.Project;
public class PatientAccount {

private boolean isVerified;
private StaffAccount verifiedBy; 
private String address;
private String healthNo;
private ConfidentialRecord relatedRecord;

//create constructor PatientAccount
public PatientAccount(String email, String password) {
}
private void viewDoctorProfile(DoctorAccount doctor) {
}
//getters and setters
public boolean isVerified() {
	return isVerified;
}
public void setVerified(boolean isVerified) {
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
public String getHealthNo() {
	return healthNo;
}
public void setHealthNo(String healthNo) {
	this.healthNo = healthNo;
}
public ConfidentialRecord getRelatedRecord() {
	return relatedRecord;
}
public void setRelatedRecord(ConfidentialRecord relatedRecord) {
	this.relatedRecord = relatedRecord;
}
}
