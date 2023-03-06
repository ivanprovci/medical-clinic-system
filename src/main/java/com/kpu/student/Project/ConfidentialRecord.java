
public class ConfidentialRecord {
private int recordID;
private PatientAccount relatedPatient;
private DoctorAccount prescribingDoctor;

//create constructor ConfidentialRecord
public ConfidentialRecord(int recordID) {
}

public boolean isAccessAllowed(Account accessor) {
	return null != null;
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
