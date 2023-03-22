package com.kpu.student.Project;

import java.util.LinkedList;

public class PatientAccount extends Account{

public PatientAccount(String email, String password) {
		super(email, password);
		// TODO Auto-generated constructor stub
	}

private boolean isVerified;
private String verifiedBy; 
private String address;
private String healthNo;
private LinkedList<Integer> relatedRecords;

private void viewDoctorProfile(DoctorAccount doctor) {
}
//getters and setters
public boolean isVerified() {
	return isVerified;
}
public void setVerified(boolean isVerified) {
	this.isVerified = isVerified;
}
public String getVerifiedBy() {
	return verifiedBy;
}
public void setVerifiedBy(String verifiedBy) {
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
public LinkedList<Integer> getRelatedRecord() {
	return relatedRecords;
}
public void setRelatedRecord(LinkedList<Integer> relatedRecord) {
	this.relatedRecords = relatedRecord;
}
}
