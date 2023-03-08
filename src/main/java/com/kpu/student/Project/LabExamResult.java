package com.kpu.student.Project;
public class LabExamResult extends ConfidentialRecord {

	public LabExamResult(int ID, LabExam exam) {
		super(ID, exam.getRelatedPatient(), exam.getPrescribingDoctor());
		exam.setExamResults(this);
		// TODO Auto-generated constructor stub
	}
	private int result;
	private int normalRange;
	private boolean isNormalResult;
	
	//getters and setters
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public int getNormalRange() {
		return normalRange;
	}
	public void setNormalRange(int normalRange) {
		this.normalRange = normalRange;
	}
	public boolean isNormalResult() {
		return isNormalResult;
	}
	public void setNormalResult(boolean isNormalResult) {
		this.isNormalResult = isNormalResult;
	}
}
