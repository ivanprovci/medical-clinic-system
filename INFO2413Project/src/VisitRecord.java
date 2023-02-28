import java.util.Date;

public class VisitRecord {

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
