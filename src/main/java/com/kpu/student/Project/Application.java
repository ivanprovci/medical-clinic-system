package com.kpu.student.Project;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class Application {
    public static void main(String[] args) {
      SpringApplication.run(Application.class, args);
    }
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
      return String.format("Hello %s!", name);
    }
    
    @GetMapping("/checkPassword")
    //Sends an email/hashed password combo to be checked
    //Returns true if the combination is valid, false otherwise - Liam
    public boolean checkPassword(@RequestParam(value = "email", defaultValue = "") String email,
    		@RequestParam(value = "password", defaultValue = "") String password) {
    	
    	return (password.equals(DatabaseAccessor.retrievePasswordHash(email)));
    }
    
    @GetMapping("/getRecords")
    //Gets an array of records from the database
    //Going to add parameters later (type of record, email of person looking up records, etc)
    //For now, we just want to make sure we can pass them along properly to the JavaScript - Liam
    public ConfidentialRecord[] getRecords() {
    	ConfidentialRecord[] recordList = new ConfidentialRecord[2];
    	recordList[0] = new VisitRecord(0, new PatientAccount("patient-johndoe@gmail.com", "johndoepassword"), 
    			new DoctorAccount("doctorsmith@gmail.com", "drsmithpassword"));
    	recordList[1] = new Prescription(1, (VisitRecord) recordList[0]);
		return recordList;
    }
}

