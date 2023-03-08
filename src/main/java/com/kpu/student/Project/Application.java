package com.kpu.student.Project;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {
    public static void main(String[] args) {
      SpringApplication.run(Application.class, args);
    }
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
      return String.format("Hello %s!", name);
    }
    
    @GetMapping("/checkPassword")
    public boolean checkPassword(@RequestParam(value = "email", defaultValue = "") String email,
    		@RequestParam(value = "password", defaultValue = "") String password) {
    	
    	return (password == DatabaseAccessor.retrievePasswordHash(email));
    }
    
    @GetMapping("/getRecords")
    public ConfidentialRecord[] getRecords() {
    	ConfidentialRecord[] recordList = new ConfidentialRecord[2];
    	recordList[0] = new Prescription(0, null, null);
    	recordList[1] = new VisitRecord(0, null, null);
		return recordList;
    }
}

