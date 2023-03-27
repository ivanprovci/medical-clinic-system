package com.kpu.student.Project;
import java.util.Date;
import java.util.LinkedList;

public class VisitRecord extends ConfidentialRecord {

	public VisitRecord(int ID, PatientAccount patient, DoctorAccount doctor) {
		super(ID, patient, doctor);
		this.setPrescribedLabExams(new LinkedList<Integer>());
		this.setPrescribedMedications(new LinkedList<Integer>());
		// TODO Auto-generated constructor stub
	}
	
	public VisitRecord(int ID, String patient, String doctor) {
		super(ID, patient, doctor);
		this.setPrescribedLabExams(new LinkedList<Integer>());
		this.setPrescribedMedications(new LinkedList<Integer>());
		// TODO Auto-generated constructor stub
	}
	private Date visitDate;
	private LinkedList<Integer> prescribedMedications;
	private LinkedList<Integer> prescribedLabExams;
	
	//getters and setters
	public Date getVisitDate() {
		return visitDate;
	}
	
	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}
	public LinkedList<Integer> getPrescribedMedications() {
		return prescribedMedications;
	}
	public void setPrescribedMedications(LinkedList<Integer> prescribedMedications) {
		this.prescribedMedications = prescribedMedications;
	}
	public LinkedList<Integer> getPrescribedLabExams() {
		return prescribedLabExams;
	}
	
	public void addExam(LabExam exam) {
		prescribedLabExams.add(exam.getRecordID());
	}
	
	public void addPrescription(Prescription med) {
		prescribedMedications.add(med.getRecordID());
	}
	
	public void setPrescribedLabExams(LinkedList<Integer> prescribedLabExams) {
		this.prescribedLabExams = prescribedLabExams;
	}
}
