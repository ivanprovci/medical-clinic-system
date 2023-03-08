package com.kpu.student.Project;
import java.util.Date;
import java.util.LinkedList;

public class VisitRecord extends ConfidentialRecord {

	public VisitRecord(int ID, PatientAccount patient, DoctorAccount doctor) {
		super(ID, patient, doctor);
		// TODO Auto-generated constructor stub
	}
	private Date visitDate;
	private LinkedList<Prescription> prescribedMedications;
	private LinkedList<LabExam> prescribedLabExams;
	
	//getters and setters
	public Date getVisitDate() {
		return visitDate;
	}
	
	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}
	public LinkedList<Prescription> getPrescribedMedications() {
		return prescribedMedications;
	}
	public void setPrescribedMedications(LinkedList<Prescription> prescribedMedications) {
		this.prescribedMedications = prescribedMedications;
	}
	public LinkedList<LabExam> getPrescribedLabExams() {
		return prescribedLabExams;
	}
	
	public void addExam(LabExam exam) {
		prescribedLabExams.add(exam);
	}
	
	public void addPrescription(Prescription med) {
		prescribedMedications.add(med);
	}
	
	public void setPrescribedLabExams(LinkedList<LabExam> prescribedLabExams) {
		this.prescribedLabExams = prescribedLabExams;
	}
}
