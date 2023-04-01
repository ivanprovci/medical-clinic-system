package com.kpu.student.Project.controller;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kpu.student.Project.Account;
import com.kpu.student.Project.ConfidentialRecord;
import com.kpu.student.Project.DatabaseAccessor;
import com.kpu.student.Project.DoctorAccount;
import com.kpu.student.Project.LabExam;
import com.kpu.student.Project.LabExamResult;
import com.kpu.student.Project.PatientAccount;
import com.kpu.student.Project.Prescription;
import com.kpu.student.Project.StaffAccount;
import com.kpu.student.Project.VisitRecord;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class Controller {
    

    @RequestMapping("/getRecords")
    public List<ConfidentialRecord> getRecords(@RequestParam(value = "email", required = true) String email,
    		@RequestParam(value = "password", required = true) String password,
    		@RequestParam(value = "recordType", defaultValue = "all") String recordType) {
    	// Gets all the records that this account is allowed to see
    	List<ConfidentialRecord> recordList = new ArrayList<ConfidentialRecord>();
    	char type = ' ';

    	if (recordType.toLowerCase().equals("all")) {
    		type = 'a';
    	} else if (recordType.toLowerCase().equals("visit")) {
    		type = 'v';
    	} else if (recordType.toLowerCase().equals("prescription")) {
    		type = 'p';
    	} else if (recordType.toLowerCase().equals("labexam")) {
    		type = 'e';
    	} else if (recordType.toLowerCase().equals("labexamresult")) {
    		type = 'r';
    	}
		try {
			if (password.equals(DatabaseAccessor.retrievePasswordHash(email))) {
				System.out.println("Account verified successfully");
				List<Integer> idList = DatabaseAccessor.getRecordsForAccount(email, type);
				System.out.println("Getting records for email " + email + " of type " + type);
				for (Integer i : idList) {
					System.out.println("Adding record to return list with id = " + i);
		    		recordList.add(DatabaseAccessor.retrieveRecord(i));
		    	}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return recordList;
    }
	 

    @RequestMapping("/checkPassword")
    public Boolean checkPassword(@RequestParam(value = "email", required = true) String email,
    		@RequestParam(value = "password", required = true) String password) {
        //Sends an email/hashed password combo to be checked
        //Returns true if the combination is valid, false otherwise - Liam
    	String s;
		try {
			s = DatabaseAccessor.retrievePasswordHash(email);
	    	Boolean val = (password.equals(s));
	    	System.out.println("CHECKING PASSWORD for email " + email + " (given password is " 
	    			+ password + ") - retrieved value is " + s + ", match " + val);
	    	return val;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

    }
    
    @RequestMapping("/checkAccountType")
    public Character checkAccountType(@RequestParam(value = "email", defaultValue = "") String email) {
    	// Takes an email and returns the type of account associated with it
    	// p for patient, d for doctor, s for staff, null if none
    	Character accountType = null;
    	try {
			Account a = DatabaseAccessor.retrieveAccountInfo(email);
			if (a instanceof PatientAccount) {
				accountType = 'p';
			} else if (a instanceof DoctorAccount) {
				accountType = 'd';
			} else if (a instanceof StaffAccount) {
				accountType = 's';
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  	
    	
    	return accountType;
    }
    
    @RequestMapping("/checkRecordType")
    public Character checkRecordType(@RequestParam(value = "recordID") int id) {
    	// Takes a record id and returns the type of record
    	// v for visit record, p for prescription, e for lab exam,
    	// r for exam result, null if invalid id
    	Character recordType = null;
    	try {
			ConfidentialRecord r = DatabaseAccessor.retrieveRecord(id);
			if (r instanceof VisitRecord) {
				recordType = 'v';
			} else if (r instanceof Prescription) {
				recordType = 'p';
			} else if (r instanceof LabExam) {
				recordType = 'e';
			} else if (r instanceof LabExamResult) {
				recordType = 'r';
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  	
    	
    	return recordType;
    }
    
    @RequestMapping("/registerNewPatient")
    public void registerNewPatient(@RequestParam(value = "newPatientEmail", required = true) String email, 
    		@RequestParam(value = "newPatientPassword", required = true) String password,
    		@RequestParam(value = "firstName", defaultValue = "") String fName,
    		@RequestParam(value = "lastName", defaultValue = "") String lName,
    		@RequestParam(value = "address", defaultValue = "") String address,
    		@RequestParam(value = "healthNo", defaultValue = "") String healthNo) {
    	// Adds a new patient account to the database
    	PatientAccount p = new PatientAccount(email, password);
    	p.setFirstName(fName);
    	p.setLastName(lName);
    	p.setAddress(address);
    	p.setHealthNo(healthNo);
    	p.setVerified(false);
    	try {
			DatabaseAccessor.addAccount(p);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @RequestMapping("/registerNewDoctor")
    public void registerNewDoctor(@RequestParam(value = "newDoctorEmail", required = true) String email, 
    		@RequestParam(value = "newDoctorPassword", required = true) String password,
    		@RequestParam(value = "firstName", defaultValue = "") String fName,
    		@RequestParam(value = "lastName", defaultValue = "") String lName,
    		@RequestParam(value = "profile", defaultValue = "") String profile,
    		@RequestParam(value = "staffEmail", required = true) String staffEmail,
    		@RequestParam(value = "staffPassword", required = true) String staffPassword) {
    	
    	try {
			if (staffPassword.equals(DatabaseAccessor.retrievePasswordHash(staffEmail)) &&
					(DatabaseAccessor.retrieveAccountInfo(email)) instanceof StaffAccount) {
				DoctorAccount d = new DoctorAccount(email, password);
				d.setFirstName(fName);
				d.setLastName(lName);
				d.setProfile(profile);
				DatabaseAccessor.addAccount(d);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @RequestMapping("/registerNewStaff")
    public void registerNewStaff(@RequestParam(value = "newStaffEmail", required = true) String email, 
    		@RequestParam(value = "newStaffPassword", required = true) String password,
    		@RequestParam(value = "firstName", defaultValue = "") String fName,
    		@RequestParam(value = "lastName", defaultValue = "") String lName,
    		@RequestParam(value = "staffEmail", required = true) String staffEmail,
    		@RequestParam(value = "staffPassword", required = true) String staffPassword) {
    	
    	try {
			if ((staffPassword.equals(DatabaseAccessor.retrievePasswordHash(staffEmail))) && 
					(DatabaseAccessor.retrieveAccountInfo(email)) instanceof StaffAccount) {
				StaffAccount s = new StaffAccount(email, password);
				s.setFirstName(fName);
				s.setLastName(lName);
				DatabaseAccessor.addAccount(s);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @RequestMapping("/addTestStaffAccount")
    public void addTestStaffAccount() {
    	StaffAccount s = new StaffAccount("staff@test.com", "TestStaffPassword");
		try {
			DatabaseAccessor.addAccount(s);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    @RequestMapping("/updatePatient")
    public void updatePatient(@RequestParam(value = "email", required = true) String email, 
    		@RequestParam(value = "password", required = true) String password,
    		@RequestParam(value = "firstName", defaultValue = "") String fName,
    		@RequestParam(value = "lastName", defaultValue = "") String lName,
    		@RequestParam(value = "address", defaultValue = "") String address,
    		@RequestParam(value = "healthNo", defaultValue = "") String healthNo) {
    	// Updates an existing patient account's information
    	
    	try {
			if (password.equals(DatabaseAccessor.retrievePasswordHash(email)) &&
					(DatabaseAccessor.retrieveAccountInfo(email)) instanceof PatientAccount) {
				PatientAccount p = (PatientAccount) DatabaseAccessor.retrieveAccountInfo(email);
		    	if (!(fName.equals(""))) {
					p.setFirstName(fName);
		    	}
		    	if (!(lName.equals(""))) {
					p.setLastName(lName);
		    	}
		    	if (!(address.equals(""))) {
			    	p.setAddress(address);
		    	}
		    	if (!(healthNo.equals(""))) {
		    		p.setHealthNo(healthNo);
		    	}
		    	DatabaseAccessor.updateAccount(p);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
    
    @RequestMapping("/updateDoctor")
    public void updateDoctor(@RequestParam(value = "email", required = true) String email, 
    		@RequestParam(value = "password", required = true) String password,
    		@RequestParam(value = "firstName", defaultValue = "") String fName,
    		@RequestParam(value = "lastName", defaultValue = "") String lName,
    		@RequestParam(value = "profile", defaultValue = "") String profile) {
    	// Updates an existing patient account's information
    	
    	try {
			if (password.equals(DatabaseAccessor.retrievePasswordHash(email)) &&
					(DatabaseAccessor.retrieveAccountInfo(email)) instanceof DoctorAccount) {
				DoctorAccount d = (DoctorAccount) DatabaseAccessor.retrieveAccountInfo(email);
		    	if (!(fName.equals(""))) {
					d.setFirstName(fName);
		    	}
		    	if (!(lName.equals(""))) {
					d.setLastName(lName);
		    	}
		    	if (!(profile.equals(""))) {
			    	d.setProfile(profile);
		    	}
		    	DatabaseAccessor.updateAccount(d);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
    
    @RequestMapping("/updateStaff")
    public void updateStaff(@RequestParam(value = "email", required = true) String email, 
    		@RequestParam(value = "password", required = true) String password,
    		@RequestParam(value = "firstName", defaultValue = "") String fName,
    		@RequestParam(value = "lastName", defaultValue = "") String lName) {
    	// Updates an existing patient account's information
    	
    	try {
			if (password.equals(DatabaseAccessor.retrievePasswordHash(email)) &&
					(DatabaseAccessor.retrieveAccountInfo(email)) instanceof StaffAccount) {
				StaffAccount s = (StaffAccount) DatabaseAccessor.retrieveAccountInfo(email);
		    	if (!(fName.equals(""))) {
					s.setFirstName(fName);
		    	}
		    	if (!(lName.equals(""))) {
					s.setLastName(lName);
		    	}
		    	DatabaseAccessor.updateAccount(s);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
    
    @RequestMapping("/retrieveAccount")
    public Account retrieveAccount(@RequestParam(value = "email", required = true) String email, 
    		@RequestParam(value = "password", required = true) String password) {
    	try {
			if (password.equals(DatabaseAccessor.retrievePasswordHash(email))) {
				return DatabaseAccessor.retrieveAccountInfo(email);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    
    @RequestMapping("/getDoctorProfile")
    public String getDoctorProfile(@RequestParam(value = "email", required = true) String doctorEmail) {
    	try {
			if ((DatabaseAccessor.retrieveAccountInfo(doctorEmail)) instanceof DoctorAccount) {
				return ((DoctorAccount) DatabaseAccessor.retrieveAccountInfo(doctorEmail)).getProfile();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return "Error: could not retrieve doctor profile.";
    }
    
    @RequestMapping("/deleteAccount")
    public void deleteAccount(@RequestParam(value = "email", required = true) String email, 
    		@RequestParam(value = "password", required = true) String password,
    		@RequestParam(value = "emailToDelete", required = true) String emailToDelete) {
    	try {
			if (password.equals(DatabaseAccessor.retrievePasswordHash(email)) &&
					((DatabaseAccessor.retrieveAccountInfo(email)) instanceof StaffAccount) || 
					(email.equals(emailToDelete))) {
				// If the email/password combination is valid AND one of the following is true
				// - The user requesting account deletion is a staff member
				// - The user is requesting to delete their own account
				DatabaseAccessor.deleteAccount(DatabaseAccessor.retrieveAccountInfo(email));
			}
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @RequestMapping("/createVisitRecord")
    //Takes doctor email/password, date, related patient email
    public void createVisitRecord(@RequestParam(value = "doctorEmail", required = true) String doctorEmail,
    		@RequestParam(value = "doctorPassword", required = true) String doctorPassword,
    		@RequestParam(value = "visitDate", required = true) Date visitDate,
   			@RequestParam(value = "patientEmail", required = true) String patientEmail) {
    	
    	try {
			if (doctorPassword.equals(DatabaseAccessor.retrievePasswordHash(doctorEmail)) &&
					(DatabaseAccessor.retrieveAccountInfo(doctorEmail) instanceof DoctorAccount) &&
					((DatabaseAccessor.retrieveAccountInfo(patientEmail)) instanceof PatientAccount)) {				
				VisitRecord newVisit = new VisitRecord(-1, patientEmail, doctorEmail);
				newVisit.setVisitDate(visitDate);
				DatabaseAccessor.addRecord(newVisit);				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
    
    @RequestMapping("/createPrescription")
    //Takes doctor email/password, id of related visit record, medicine name, quantity, dose, refillable
    public void createPrescription(@RequestParam(value = "doctorEmail", required = true) String doctorEmail,
        		@RequestParam(value = "doctorPassword", required = true) String doctorPassword,
        		@RequestParam(value = "relatedVisitRecord", required = true) int relatedVisitRecord,
       			@RequestParam(value = "medName", defaultValue= "") String medName,
       			@RequestParam(value = "medDose", defaultValue= "") String medDose,
       			@RequestParam(value = "medQuantity", defaultValue= "") String medQuantity,
       			@RequestParam(value = "refillable", defaultValue= "false") Boolean refillable) {
        	
        	try {
    			if (doctorPassword.equals(DatabaseAccessor.retrievePasswordHash(doctorEmail)) &&
    					(DatabaseAccessor.retrieveAccountInfo(doctorEmail) instanceof DoctorAccount) &&
    					((DatabaseAccessor.retrieveRecord(relatedVisitRecord)) instanceof VisitRecord)) {
    				
    				VisitRecord relatedVisit = (VisitRecord) DatabaseAccessor.retrieveRecord(relatedVisitRecord);
    				Prescription newPrescription = new Prescription(-1, relatedVisit);
    				newPrescription.setPrescribingDoctor(doctorEmail);
    				newPrescription.setRelatedPatient(relatedVisit.getRelatedPatient());
    				newPrescription.setDatePrescribed(relatedVisit.getVisitDate());
    				newPrescription.setMedicineName(medName);
    				newPrescription.setMedicineDose(medDose);
    				newPrescription.setMedicineQuantity(medQuantity);
    				newPrescription.setRefillable(refillable);   				
    				DatabaseAccessor.addRecord(newPrescription);
    				DatabaseAccessor.updateRecord(relatedVisit);    				
    			}
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	
    }
    
    @RequestMapping("/createLabExam")
    //Takes doctor email/password, id of related visit record, exam item, date of exam
    public void createLabExam(@RequestParam(value = "doctorEmail", required = true) String doctorEmail,
    		@RequestParam(value = "doctorPassword", required = true) String doctorPassword,
    		@RequestParam(value = "relatedVisitRecord", required = true) int relatedVisitRecord,
   			@RequestParam(value = "examItem", defaultValue= "") String examItem,
   			@RequestParam(value = "examDate", required = true) Date examDate) {
    	
    	try {
			if (doctorPassword.equals(DatabaseAccessor.retrievePasswordHash(doctorEmail)) &&
					(DatabaseAccessor.retrieveAccountInfo(doctorEmail) instanceof DoctorAccount)) {
				
				VisitRecord relatedVisit = (VisitRecord) DatabaseAccessor.retrieveRecord(relatedVisitRecord);
				LabExam newExam = new LabExam(-1, relatedVisit);
				newExam.setPrescribingDoctor(doctorEmail);
				newExam.setRelatedPatient(relatedVisit.getRelatedPatient());
				newExam.setExamItem(examItem);
				newExam.setExamDate(examDate);
				DatabaseAccessor.addRecord(newExam);
				DatabaseAccessor.updateRecord(relatedVisit);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    @RequestMapping("/createLabExamResult")
    //Takes staff email/password, id of related lab exam, result, upper bound, lower bound
    public void createLabExamResult(@RequestParam(value = "doctorEmail", required = true) String staffEmail,
    		@RequestParam(value = "doctorPassword", required = true) String staffPassword,
    		@RequestParam(value = "relatedLabExam", required = true) int relatedLabExam,
   			@RequestParam(value = "result", defaultValue= "") int result,
   			@RequestParam(value = "upperBound", defaultValue= "") int upperBound,
   			@RequestParam(value = "lowerBound", defaultValue= "") int lowerBound) {
    	
    	try {
			if (staffPassword.equals(DatabaseAccessor.retrievePasswordHash(staffEmail)) &&
					(DatabaseAccessor.retrieveAccountInfo(staffEmail) instanceof StaffAccount)) {
				
				LabExam relatedExam = (LabExam) DatabaseAccessor.retrieveRecord(relatedLabExam);
				LabExamResult newResult = new LabExamResult(-1, relatedExam);
				newResult.setResult(result);
				newResult.setNormalLowerBound(lowerBound);
				newResult.setNormalUpperBound(upperBound);
				DatabaseAccessor.addRecord(newResult);
				DatabaseAccessor.updateRecord(relatedExam);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    @RequestMapping("/updateVisitRecord")
    //Takes staff or doctor email/password, record id, optional - date, related patient email
    public void updateVisitRecord(@RequestParam(value = "email", required = true) String email,
    		@RequestParam(value = "password", required = true) String password,
    		@RequestParam(value = "recordID", required = true) int recordID,
    		@RequestParam(value = "visitDate", required = false) Date visitDate,
    		@RequestParam(value = "patientEmail", defaultValue = "") String patientEmail) {
    	try {
			if (password.equals(DatabaseAccessor.retrievePasswordHash(email)) &&
					// If the email/password combo is valid AND one of the following is true
					((DatabaseAccessor.retrieveAccountInfo(email)) instanceof StaffAccount) || 
					// If the email/password combo is for a staff account
					(DatabaseAccessor.retrieveRecord(recordID).getPrescribingDoctor().equals(email))) {
					// If the email/password combo is for the doctor who wrote this visit record
				
				VisitRecord updateRecord = (VisitRecord) DatabaseAccessor.retrieveRecord(recordID);
				// Retrieve the current values of the visit record from database
				
				if (!(Objects.isNull(visitDate))) {
					// If visitDate is not null/if we were given a new value for visitDate
					// Update the value of visitDate
					updateRecord.setVisitDate(visitDate);
				}
				if (!(patientEmail.equals(""))) {
					// If patientEmail does not equal the default value/if we were given a new value for patientEmail
					// Update the value of patientEmail
					updateRecord.setRelatedPatient(patientEmail);
				}
				// Add the newly updated values to the database
				DatabaseAccessor.updateRecord(updateRecord);
				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
   //@RequestMapping("/updatePrescription")
    //Takes staff or doctor email/password, record id, optional - medicine name, quantity, dose, refillable
	@RequestMapping("/updatePrescription")
	public void updatePrescription(@RequestParam(value = "email", required = true) String email,
                @RequestParam(value = "password", required = true) String password,
                @RequestParam(value = "recordID", required = true) int recordID,
                @RequestParam(value = "medicineName", required = false) String medicineName,
                @RequestParam(value = "quantity", required = false) String quantity,
                @RequestParam(value = "dose", required = false) String dose,
                @RequestParam(value = "refillable", required = false) Boolean refillable) {
    try {
        if (password.equals(DatabaseAccessor.retrievePasswordHash(email)) &&
                // If the email/password combo is valid AND one of the following is true
                ((DatabaseAccessor.retrieveAccountInfo(email)) instanceof StaffAccount) || 
                // If the email/password combo is for a staff account
                (DatabaseAccessor.retrieveRecord(recordID).getPrescribingDoctor().equals(email))) {
                // If the email/password combo is for the doctor who wrote this prescription record
            
            Prescription updateRecord = (Prescription) DatabaseAccessor.retrieveRecord(recordID);
            // Retrieve the current values of the prescription record from database
            
            if (!(Objects.isNull(medicineName))) {
                // If medicineName is not null/if we were given a new value for medicineName
                // Update the value of medicineName
                updateRecord.setMedicineName(medicineName);
            }
            if (!(Objects.isNull(quantity))) {
                // If quantity is not null/if we were given a new value for quantity
                // Update the value of quantity
                updateRecord.setMedicineQuantity(quantity);
            }
            if (!(Objects.isNull(dose))) {
                // If dose is not null/if we were given a new value for dose
                // Update the value of dose
                updateRecord.setMedicineDose(dose);
            }
            if (!(Objects.isNull(refillable))) {
                // If refillable is not null/if we were given a new value for refillable
                // Update the value of refillable
                updateRecord.setRefillable(refillable);
            }
            
            // Add the newly updated values to the database
            DatabaseAccessor.updateRecord(updateRecord);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

        
    //@RequestMapping("/updateLabExam")
    //Takes staff or doctor email/password, record id, optional - exam item, date of exam
	@RequestMapping("/updateLabExam")
	public void updateLabExam(@RequestParam(value = "email", required = true) String email,
        		@RequestParam(value = "password", required = true) String password,
        		@RequestParam(value = "recordID", required = true) int recordID,
        		@RequestParam(value = "examItem", required = false) String examItem,
        		@RequestParam(value = "examDate", required = false) Date examDate) {

    try {
        // Check if the email and password are valid and authorized to update the lab exam record
        if (password.equals(DatabaseAccessor.retrievePasswordHash(email)) &&
            ((DatabaseAccessor.retrieveAccountInfo(email)) instanceof StaffAccount) || 
            (DatabaseAccessor.retrieveRecord(recordID).getPrescribingDoctor().equals(email))) {
            
            // Retrieve the current lab exam record from the database
            LabExam updateRecord = (LabExam) DatabaseAccessor.retrieveRecord(recordID);
            
            // Update the exam item if a new value was provided
            if (examItem != null) {
                updateRecord.setExamItem(examItem);
            }
            
            // Update the exam date if a new value was provided
            if (examDate != null) {
                updateRecord.setExamDate(examDate);
            }
            
            // Update the lab exam record in the database
            DatabaseAccessor.updateRecord(updateRecord);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    
    //@RequestMapping("/updateLabExamResult")
    //Takes staff email/password, record id, optional - result, upper bound, lower bound
	@RequestMapping("/updateLabExamResult")
	public Boolean updateLabExamResult(@RequestParam(value = "email", required = true) String email,
	                                   @RequestParam(value = "password", required = true) String password,
	                                   @RequestParam(value = "recordID", required = true) int id,
	                                   @RequestParam(value = "result", required = false) Integer result,
	                                   @RequestParam(value = "upperBound", required = false) Integer upperBound,
	                                   @RequestParam(value = "lowerBound", required = false) Integer lowerBound) {
	    if (checkAccountType(email) == 's' && checkPassword(email, password)) {
	        try {
	            ConfidentialRecord r = DatabaseAccessor.retrieveRecord(id);
	            if (r instanceof LabExamResult) {
	                LabExamResult labResult = (LabExamResult) r;
	                if (!result.equals(null)) {
	                    labResult.setResult(result);
	                }
	                if (!upperBound.equals(null)) {
	                    labResult.setNormalUpperBound(upperBound);
	                }
	                if (!lowerBound.equals(null)) {
	                    labResult.setNormalLowerBound(lowerBound);
	                }
	                DatabaseAccessor.updateRecord(labResult);
	                return true;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return false;
	}
    
    //@RequestMapping("/deleteRecord")
    //Takes staff or doctor email/password, record id
    
    @RequestMapping("/getUnverifiedPatients")
    //Takes staff email/password
    public List<String> getUnverifiedPatients(@RequestParam(value = "staffEmail", required = true) String staffEmail,
    		@RequestParam(value = "staffPassword", required = true) String staffPassword) {
    	
    	List<String> patients = new LinkedList<String>();
    	
    	try {
			if (staffPassword.equals(DatabaseAccessor.retrievePasswordHash(staffEmail)) &&
					// If the email/password combo is valid AND it's linked to a staff account
					((DatabaseAccessor.retrieveAccountInfo(staffEmail)) instanceof StaffAccount)) {
				patients = DatabaseAccessor.getUnverifiedPatients();
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return patients;
    }
    
    @RequestMapping("/verifyPatient")
    //Takes staff email/password, email of patient to be verified
    public void verifyPatient(@RequestParam(value = "staffEmail", required = true) String staffEmail,
    		@RequestParam(value = "staffPassword", required = true) String staffPassword,
    		@RequestParam(value = "patientEmail", required = true) String patientEmail) {
    	try {
			if (staffPassword.equals(DatabaseAccessor.retrievePasswordHash(staffEmail)) &&
					// If the email/password combo is valid AND it's linked to a staff account
					((DatabaseAccessor.retrieveAccountInfo(staffEmail)) instanceof StaffAccount) &&
					// AND the patient's email is linked to a patient account
					((DatabaseAccessor.retrieveAccountInfo(patientEmail)) instanceof PatientAccount)) {
				PatientAccount p = (PatientAccount) DatabaseAccessor.retrieveAccountInfo(patientEmail);
				StaffAccount s = (StaffAccount) DatabaseAccessor.retrieveAccountInfo(staffEmail);
				s.verifyPatient(p);
				DatabaseAccessor.updateAccount(p);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @RequestMapping("/generateDoctorReport")
    //Takes staff email/password, date, boolean annualReport (true = annual, false = yearly), doctor email
    public List<VisitRecord> generateDoctorReport(@RequestParam(value = "staffEmail", required = true) String staffEmail,
    		@RequestParam(value = "staffPassword", required = true) String staffPassword,
    		@RequestParam(value = "isAnnualReport", required = true) boolean isAnnualReport,
    		@RequestParam(value = "doctorEmail", required = true) String doctorEmail) {
    	
    	List<VisitRecord> visits = new LinkedList<VisitRecord>();
    	
    	try {
    		if (staffPassword.equals(DatabaseAccessor.retrievePasswordHash(staffEmail)) &&
					// If the email/password combo is valid AND it's linked to a staff account
					((DatabaseAccessor.retrieveAccountInfo(staffEmail)) instanceof StaffAccount) &&
					((DatabaseAccessor.retrieveAccountInfo(doctorEmail)) instanceof DoctorAccount)) {
    			visits = DatabaseAccessor.staffReport_visitsByDoctor(doctorEmail, isAnnualReport);
    		}			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return visits;
    	
    }
    
    @RequestMapping("/generatePatientReport")
    //Takes staff email/password, date, boolean annualReport (true = annual, false = yearly), doctor email
    public HashMap<String, Integer> generatePatientReport(@RequestParam(value = "staffEmail", required = true) String staffEmail,
    		@RequestParam(value = "staffPassword", required = true) String staffPassword,
    		@RequestParam(value = "isAnnualReport", required = true) boolean isAnnualReport,
    		@RequestParam(value = "patientEmail", required = true) String patientEmail) {
    	
    	HashMap<String, Integer> visits = new HashMap<String, Integer>();
    	
    	try {
    		if (staffPassword.equals(DatabaseAccessor.retrievePasswordHash(staffEmail)) &&
					// If the email/password combo is valid AND it's linked to a staff account
					((DatabaseAccessor.retrieveAccountInfo(staffEmail)) instanceof StaffAccount) &&
					((DatabaseAccessor.retrieveAccountInfo(patientEmail)) instanceof PatientAccount)) {
    			visits = DatabaseAccessor.staffReport_visitsByPatient(patientEmail, isAnnualReport);
    		}			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return visits;
    	
    }
    
    @RequestMapping("/generatePrescriptionReport")
    //Takes staff email/password, date, boolean annualReport (true = annual, false = yearly), doctor email
    public HashMap<String, Integer> generatePrescriptionReport(@RequestParam(value = "staffEmail", required = true) String staffEmail,
    		@RequestParam(value = "staffPassword", required = true) String staffPassword,
    		@RequestParam(value = "isAnnualReport", required = true) boolean isAnnualReport) {
    	
    	HashMap<String, Integer> prescriptions = new HashMap<String, Integer>();
    	
    	try {
    		if (staffPassword.equals(DatabaseAccessor.retrievePasswordHash(staffEmail)) &&
					// If the email/password combo is valid AND it's linked to a staff account
					((DatabaseAccessor.retrieveAccountInfo(staffEmail)) instanceof StaffAccount)) {
    			prescriptions = DatabaseAccessor.staffReport_commonMeds(isAnnualReport);
    		}			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return prescriptions;
    	
    }
    
	@GetMapping("/viewRecords")
	public ModelAndView viewRecords (Model model) {
		List<ConfidentialRecord> recordList = getRecords("staff@test.com", "TestStaffPassword", "all");
		model.addAttribute("records", recordList);
		return new ModelAndView("New");
	}

}
