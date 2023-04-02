package com.kpu.student.Project;

import java.sql.SQLException;

public class LabExamResult extends ConfidentialRecord {

	public LabExamResult(int ID, LabExam exam) {
		super(ID, exam.getRelatedPatient(), exam.getPrescribingDoctor());
		this.setRelatedExam(exam.getRecordID());
		// TODO Auto-generated constructor stub
	}
	
	public LabExamResult(int ID) {
		super(ID);
		// TODO Auto-generated constructor stub
	}
	
	private int relatedExam;
	private int result;
	private int normalUpperBound;
	private int normalLowerBound;
	private boolean isNormalResult;
	
	//getters and setters
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public boolean isNormalResult() {
		return isNormalResult;
	}
	public void setNormalResult(boolean isNormalResult) {
		this.isNormalResult = isNormalResult;
	}

	public int getNormalUpperBound() {
		return normalUpperBound;
	}

	public void setNormalUpperBound(int normalUpperBound) {
		this.normalUpperBound = normalUpperBound;
	}

	public int getNormalLowerBound() {
		return normalLowerBound;
	}

	public void setNormalLowerBound(int normalLowerBound) {
		this.normalLowerBound = normalLowerBound;
	}

	public int getRelatedExam() {
		return relatedExam;
	}

	public void setRelatedExam(int relatedExam) {
		this.relatedExam = relatedExam;
	}
	
	public void checkIfNormal() {
		if (this.getNormalLowerBound() <= this.getResult() && this.getResult() <= this.getNormalUpperBound()) {
			this.setNormalResult(true);
		}
		else {
			this.setNormalResult(false);
			try {
				DoctorAccount d = (DoctorAccount) DatabaseAccessor.retrieveAccountInfo(this.getPrescribingDoctor());
				PatientAccount p = (PatientAccount) DatabaseAccessor.retrieveAccountInfo(this.getRelatedPatient());
				LabExam e = (LabExam) DatabaseAccessor.retrieveRecord(this.getRelatedExam());
				d.notifyUser("Abnormal lab result detected for patient " + p.getFirstName() + " " +
				p.getLastName() + "!/nLab exam item: " + e.getExamItem() + ", result was " + this.getResult());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
