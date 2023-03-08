package com.kpu.student.Project;
import java.util.Date;

public class VisitRecord extends ConfidentialRecord {

	public VisitRecord(int ID, PatientAccount patient, DoctorAccount doctor) {
		super(ID, patient, doctor);
		// TODO Auto-generated constructor stub
	}
	private Date visitDate;
	private Prescription prescribedMedications;
	private LabExam prescribedLabExams;
	
	//getters and setters
	public Date getVisitDate() {
		return visitDate;
	}
	
	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}
	public Prescription getPrescribedMedications() {
		return prescribedMedications;
	}
	public void setPrescribedMedications(Prescription prescribedMedications) {
		this.prescribedMedications = prescribedMedications;
	}
	public LabExam getPrescribedLabExams() {
		return prescribedLabExams;
	}
	public void setPrescribedLabExams(LabExam prescribedLabExams) {
		this.prescribedLabExams = prescribedLabExams;
	}
}
