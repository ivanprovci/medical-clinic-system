package com.kpu.student.Project;
public class LabExamResult extends ConfidentialRecord {

	public LabExamResult(int ID, LabExam exam) {
		super(ID, exam.getRelatedPatient(), exam.getPrescribingDoctor());
		exam.setExamResults(this.getRecordID());
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
}
