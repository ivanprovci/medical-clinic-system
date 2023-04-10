package com.kpu.student.Project;

import java.util.LinkedList;

public class DoctorAccount extends Account {
	
public DoctorAccount(String email, String password) {
		super(email, password);
		this.setRelatedRecords(new LinkedList<Integer>());
		// TODO Auto-generated constructor stub
	}
public DoctorAccount() {
	// TODO Auto-generated constructor stub
}
private String profile;
private LinkedList<Integer> relatedRecords;

//getters and setters
public String getProfile() {
	return profile;
}
public void setProfile(String profile) {
	this.profile = profile;
}
public LinkedList<Integer> getRelatedRecords() {
	return relatedRecords;
}
public void setRelatedRecords(LinkedList<Integer> relatedRecords) {
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
