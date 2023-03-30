package com.kpu.student.Project.controller;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public List<ConfidentialRecord> getRecords(@RequestParam(value = "email", required = true) String requestingEmail) {
    	// Gets all the records that this account is allowed to see
    	List<ConfidentialRecord> recordList = new ArrayList<ConfidentialRecord>();
    	
		try {
			List<Integer> idList = DatabaseAccessor.getRecordsForAccount(requestingEmail);
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
    
	@GetMapping("/viewRecords")
	public ModelAndView viewRecords (Model model) {
		List<ConfidentialRecord> recordList = getRecords("World");
		model.addAttribute("records", recordList);
		return new ModelAndView("New");
	}

}
