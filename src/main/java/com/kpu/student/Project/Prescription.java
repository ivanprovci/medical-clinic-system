package com.kpu.student.Project;
import java.util.Date;

public class Prescription extends ConfidentialRecord {
	
public Prescription(int ID, VisitRecord visit) {
	super(ID, visit.getRelatedPatient(), visit.getPrescribingDoctor());
	visit.addPrescription(this);
	this.setPrescribedDuring(visit.getRecordID());
		// TODO Auto-generated constructor stub
	}

public Prescription(int ID) {
	super(ID);
	// TODO Auto-generated constructor stub
}

private int prescribedDuring;
private Date datePrescribed;
private String medicineName;
private String medicineQuantity;
private String medicineDose;
private boolean isRefillable;


// getters and setters
public Date getDatePrescribed() {
    return datePrescribed;
}

public void setDatePrescribed(Date datePrescribed) {
    this.datePrescribed = datePrescribed;
}

public String getMedicineName() {
	return medicineName;
}

public void setMedicineName(String medicineName) {
	this.medicineName = medicineName;
}

public String getMedicineQuantity() {
	return medicineQuantity;
}

public void setMedicineQuantity(String medicineQuantity) {
	this.medicineQuantity = medicineQuantity;
}

public String getMedicineDose() {
	return medicineDose;
}

public void setMedicineDose(String medicineDose) {
	this.medicineDose = medicineDose;
}

public boolean isRefillable() {
	return isRefillable;
}

public void setRefillable(boolean isRefillable) {
	this.isRefillable = isRefillable;
}

public int getPrescribedDuring() {
	return prescribedDuring;
}

public void setPrescribedDuring(int prescribedDuring) {
	this.prescribedDuring = prescribedDuring;
}
}
