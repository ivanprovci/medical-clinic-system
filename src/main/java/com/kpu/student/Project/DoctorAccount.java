package com.kpu.student.Project;
public class DoctorAccount extends Account {
	
public DoctorAccount(String email, String password) {
		super(email, password);
		// TODO Auto-generated constructor stub
	}
private String profile;
private ConfidentialRecord relatedRecords;

//getters and setters
public String getProfile() {
	return profile;
}
public void setProfile(String profile) {
	this.profile = profile;
}
public ConfidentialRecord getRelatedRecords() {
	return relatedRecords;
}
public void setRelatedRecords(ConfidentialRecord relatedRecords) {
	this.relatedRecords = relatedRecords;
}
//methods I created because of professor's feedbacks
private void inputVisitDetails() {
	
}
private void inputPrescription() {
	
}
private void inputLabExam() {
	
}
}