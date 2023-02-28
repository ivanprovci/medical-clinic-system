import java.util.Date;

public class LabExam {

	private Date examDate;
	private String patientHealthNo;
	private String examItem;
	private LabExamResult examResults;
	
	//getters and setters
	public Date getExamDate() {
		return examDate;
	}
	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}
	public String getPatientHealthNo() {
		return patientHealthNo;
	}
	public void setPatientHealthNo(String patientHealthNo) {
		this.patientHealthNo = patientHealthNo;
	}
	public String getExamItem() {
		return examItem;
	}
	public void setExamItem(String examItem) {
		this.examItem = examItem;
	}
	public LabExamResult getExamResults() {
		return examResults;
	}
	public void setExamResults(LabExamResult examResults) {
		this.examResults = examResults;
	}
}
