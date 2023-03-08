package com.kpu.student.Project;
public class ConfidentialRecord {
private int recordID;
private PatientAccount relatedPatient;
private DoctorAccount prescribingDoctor;

//create constructor ConfidentialRecord
public ConfidentialRecord(int ID, PatientAccount patient, DoctorAccount doctor) {
	this.setRecordID(ID);
	this.setRelatedPatient(patient);
	this.setPrescribingDoctor(doctor);
}

public boolean isAccessAllowed(Account accessor) {
	if (accessor.equals(this.getRelatedPatient())) {
		
	}
	return false;
}

//getters and setters
public int getRecordID() {
	return recordID;
}

public void setRecordID(int recordID) {
	this.recordID = recordID;
}

public DoctorAccount getPrescribingDoctor() {
	return prescribingDoctor;
}

public void setPrescribingDoctor(DoctorAccount prescribingDoctor) {
	this.prescribingDoctor = prescribingDoctor;
}

public PatientAccount getRelatedPatient() {
	return relatedPatient;
}

public void setRelatedPatient(PatientAccount relatedPatient) {
	this.relatedPatient = relatedPatient;
}
}
