package com.kpu.student.Project;
public class ConfidentialRecord {
private int recordID;
private String relatedPatient;
private String prescribingDoctor;

//create constructor ConfidentialRecord
public ConfidentialRecord(int ID, PatientAccount patient, DoctorAccount doctor) {
	this.setRecordID(ID);
	this.setRelatedPatient(patient.getEmail());
	this.setPrescribingDoctor(doctor.getEmail());
}

public ConfidentialRecord(int ID, String patientEmail, String doctorEmail) {
	this.setRecordID(ID);
	this.setRelatedPatient(patientEmail);
	this.setPrescribingDoctor(doctorEmail);
}

public ConfidentialRecord(int ID) {
	this.setRecordID(ID);
}

public boolean isAccessAllowed(Account accessor) {
	if (accessor.getEmail().equals(this.getRelatedPatient())) {
		//If this is the patient referenced in the record
		return true;
	}
	if (accessor.getEmail().equals(this.getPrescribingDoctor())) {
		//If this is the doctor referenced in the record
		return true;
	}
	if (accessor instanceof StaffAccount) {
		//If this is a staff account (staff have read permissions for everything)
		return true;
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

public String getPrescribingDoctor() {
	return prescribingDoctor;
}

public void setPrescribingDoctor(String prescribingDoctor) {
	this.prescribingDoctor = prescribingDoctor;
}

public String getRelatedPatient() {
	return relatedPatient;
}

public void setRelatedPatient(String string) {
	this.relatedPatient = string;
}
}
