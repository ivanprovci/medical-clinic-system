package com.kpu.student.Project.controller;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public List<ConfidentialRecord> getRecords(@RequestParam(value = "email", required = true) String requestingEmail,
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
			List<Integer> idList = DatabaseAccessor.getRecordsForAccount(requestingEmail, type);
			for (Integer i : idList) {
	    		recordList.add(DatabaseAccessor.retrieveRecord(i));
	    	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return recordList;
    }
	 

    @RequestMapping("/checkPassword")
    public Boolean checkPassword(@RequestParam(value = "email", defaultValue = "") String email,
    		@RequestParam(value = "password", defaultValue = "") String password) {
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
			if (staffPassword.equals(DatabaseAccessor.retrievePasswordHash(staffEmail))) {
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
			if (staffPassword.equals(DatabaseAccessor.retrievePasswordHash(staffEmail))) {
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
    		@RequestParam(value = "visitDate", defaultValue = "") Date visitDate,
   			@RequestParam(value = "patientEmail", required = true) String patientEmail) {
    	
    	try {
			if (doctorPassword.equals(DatabaseAccessor.retrievePasswordHash(doctorEmail)) &&
					(DatabaseAccessor.retrieveAccountInfo(doctorEmail) instanceof DoctorAccount)) {
				
				VisitRecord newVisit = new VisitRecord(-1);
				newVisit.setVisitDate(visitDate);
				newVisit.setPrescribingDoctor(doctorEmail);
				newVisit.setRelatedPatient(patientEmail);
				DatabaseAccessor.addRecord(newVisit);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
    
    @RequestMapping("/createPrescription")
    //Takes doctor email/password, id of related visit record, medicine name, quantity, dose, refillable
    public void createPrescription() {
    	
    }
    
    @RequestMapping("/createLabExam")
    //Takes doctor email/password, id of related visit record, exam item, date of exam
    public void createLabExam() {
    	
    }
    
    @RequestMapping("/createLabExamResult")
    //Takes staff email/password, id of related lab exam, result, upper bound, lower bound
    public void createLabExamResult() {
    	
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
    
    
    //@RequestMapping("/updateLabExam")
    //Takes staff or doctor email/password, record id, optional - exam item, date of exam
    
    //@RequestMapping("/updateLabExamResult")
    //Takes staff email/password, record id, optional - result, upper bound, lower bound
    
    //@RequestMapping("/deleteRecord")
    //Takes staff or doctor email/password, record id
    
    //@RequestMapping("/getUnverifiedPatients")
    //Takes staff email/password
    
    //@RequestMapping("/verifyPatient")
    //Takes staff email/password, email of patient to be verified
    
    //@RequestMapping("/generateReport")
    //Takes staff email/password, date, boolean annualReport (true = annual, false = yearly)
    
	@GetMapping("/viewRecords")
	public ModelAndView viewRecords (Model model) {
		List<ConfidentialRecord> recordList = getRecords("World", "all");
		model.addAttribute("records", recordList);
		return new ModelAndView("New");
	}

}
