package com.kpu.student.Project.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kpu.student.Project.ConfidentialRecord;
import com.kpu.student.Project.DatabaseAccessor;
import com.kpu.student.Project.DoctorAccount;
import com.kpu.student.Project.PatientAccount;
import com.kpu.student.Project.Prescription;
import com.kpu.student.Project.RecordList;
import com.kpu.student.Project.VisitRecord;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class Controller {
	
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
      return String.format("Hello %s!!!!!", name);
    }
    
    @GetMapping("/getRecords")
    public List<ConfidentialRecord> hello2(@RequestParam(value = "name", defaultValue = "World") String name) {
    	List<ConfidentialRecord> recordList = new ArrayList<ConfidentialRecord>();
    	ConfidentialRecord record1 = new VisitRecord(0, "steve@email.com", "steve-doctor@email.com");
    	ConfidentialRecord record2 = new Prescription(1, (VisitRecord) record1);
    	recordList.add(record1);
    	recordList.add(record2);
    	return recordList;
    }
	 
    //Sends an email/hashed password combo to be checked
    //Returns true if the combination is valid, false otherwise - Liam
    @GetMapping("/checkPassword")
    public Boolean checkPassword(@RequestParam(value = "email", defaultValue = "") String email,
    		@RequestParam(value = "password", defaultValue = "") String password) {
    	Boolean val = (password.equals(DatabaseAccessor.retrievePasswordHash(email)));
    	System.out.print(val);
    	return val;
    }

}
