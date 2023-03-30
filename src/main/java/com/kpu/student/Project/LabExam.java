package com.kpu.student.Project;
import java.util.Date;

public class LabExam extends ConfidentialRecord{


	public LabExam(int ID, VisitRecord visit) {
		super(ID, visit.getRelatedPatient(), visit.getPrescribingDoctor());
		//TODO: retrieve patient's health number from database dynamically
		//this.setPatientHealthNo(visit.getRelatedPatient().getHealthNo());
		visit.addExam(this);
		this.setPrescribedDuring(visit.getRecordID());
	}
	
	public LabExam(int ID) {
		super(ID);
		// TODO Auto-generated constructor stub
	}
	
	private int prescribedDuring;
	private Date examDate;
	private String patientHealthNo;
	private String examItem;
	private int examResults;
	
	//getters and setters
	public Date getExamDate() {
		return examDate;
	}
	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}
	public String getPatientHealthNo() {
		return patientHealthNo;
	}
	public void setPatientHealthNo(String patientHealthNo) {
		this.patientHealthNo = patientHealthNo;
	}
	public String getExamItem() {
		return examItem;
	}
	public void setExamItem(String examItem) {
		this.examItem = examItem;
	}
	public int getExamResults() {
		return examResults;
	}
	public void setExamResults(int examResults) {
		this.examResults = examResults;
	}

	public int getPrescribedDuring() {
		return prescribedDuring;
	}

	public void setPrescribedDuring(int prescribedDuring) {
		this.prescribedDuring = prescribedDuring;
	}
}
