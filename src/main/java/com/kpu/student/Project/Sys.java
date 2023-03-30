package com.kpu.student.Project;

import java.sql.SQLException;

public class Sys {

	//created because of professor's feedbacks
	private void compareLabResult() {}
	private void sendAutoAlertEmail() {}
	
	  public class Account {}
	  
	  public class ConfidentialRecord {}
	  
	  public static void main(String[] args) {
		  PatientAccount acc = new PatientAccount("steve@email.com", "stevepassword");
		  try {
			DatabaseAccessor.addAccount(acc);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
}
