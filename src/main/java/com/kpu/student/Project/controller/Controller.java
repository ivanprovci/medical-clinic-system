package com.kpu.student.Project.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.kpu.student.Project.ConfidentialRecord;
import com.kpu.student.Project.DatabaseAccessor;
import com.kpu.student.Project.Prescription;
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
    	String s = DatabaseAccessor.retrievePasswordHash(email);
    	Boolean val = (password.equals(s));
    	System.out.print("val = " + val);
    	System.out.print(" s = (" + s + ")");
    	return val;
    }

}