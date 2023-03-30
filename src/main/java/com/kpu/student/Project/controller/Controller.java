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
import com.kpu.student.Project.ConfidentialRecord;
import com.kpu.student.Project.DatabaseAccessor;
import com.kpu.student.Project.LabExam;
import com.kpu.student.Project.Prescription;
import com.kpu.student.Project.VisitRecord;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class Controller {
	
    @RequestMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
      return String.format("Hello %s!!!!!", name);
    }
    
    @RequestMapping("/getRecords")
    public List<ConfidentialRecord> getRecords(@RequestParam(value = "email", defaultValue = "") String requestingEmail) {
    	List<ConfidentialRecord> recordList = new ArrayList<ConfidentialRecord>();
    	ConfidentialRecord record1 = new VisitRecord(0, "steve@email.com", "steve-doctor@email.com");
    	((VisitRecord) record1).setVisitDate(new Date(0));
    	ConfidentialRecord record2 = new Prescription(1, (VisitRecord) record1);
    	((Prescription) record2).setDatePrescribed(new Date(0));
    	((Prescription) record2).setMedicineDose("10ml");
    	((Prescription) record2).setMedicineName("Cough Syrup");
    	((Prescription) record2).setMedicineQuantity("250ml bottle");
    	((Prescription) record2).setRefillable(true);
    	((VisitRecord) record1).addExam(new LabExam(6));
    	((VisitRecord) record1).addExam(new LabExam(7));
    	((VisitRecord) record1).addExam(new LabExam(8));
    	((VisitRecord) record1).addExam(new LabExam(9));
    	((VisitRecord) record1).addPrescription(new Prescription(15));
    	((VisitRecord) record1).addPrescription(new Prescription(16));
    	((VisitRecord) record1).addPrescription(new Prescription(17));
    	recordList.add(record1);
    	recordList.add(record2);
    	return recordList;
    }
	 
    //Sends an email/hashed password combo to be checked
    //Returns true if the combination is valid, false otherwise - Liam
    @RequestMapping("/checkPassword")
    public Boolean checkPassword(@RequestParam(value = "email", defaultValue = "") String email,
    		@RequestParam(value = "password", defaultValue = "") String password) {
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

	@GetMapping("/viewRecords")
	public ModelAndView viewRecords (Model model) {
		List<ConfidentialRecord> recordList = getRecords("World");
		model.addAttribute("records", recordList);
		return new ModelAndView("New");
	}

}
