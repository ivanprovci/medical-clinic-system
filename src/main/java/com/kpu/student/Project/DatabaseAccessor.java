package com.kpu.student.Project;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DatabaseAccessor {

	public static String retrievePasswordHash(String email) throws SQLException {
		// Gets the password hash associated with a certain email
		// Returns an empty string if there is no account associated with the email -Liam
		String pwHash = "";
		Connection c = DatabaseAccessor.connect();
		System.out.println("calling retrievePasswordHash(" + email + ")");
		PreparedStatement getPW = c.prepareStatement("SELECT passwordHash FROM Account WHERE email = ?");
		getPW.setString(1, email);
		ResultSet results = getPW.executeQuery();		
		if (results.isBeforeFirst()) {
			results.next();
			// System.out.println("retrieved password: " +
			// results.getString("passwordHash"));
			pwHash = results.getString("passwordHash");
		}
		c.close();

		System.out.println("retrievePasswordHash() executed successfully");
		return pwHash;
	}

	public static Account retrieveAccountInfo(String email) throws SQLException {
		// Returns an Account object with all associated info
		// If the requested account is a patient account, it is returned as a PatientAccount
		// Likewise for doctor and staff accounts - Liam
		Account retrievedAccount = new Account("", "");
		Connection c = DatabaseAccessor.connect();

		// Execute MySQL statement to see if the provided email shows up in StaffAccount table
		// If it does, we know this account is a staff account
		PreparedStatement checkIfStaff = c.prepareStatement("SELECT * FROM StaffAccount WHERE email = ?");
		checkIfStaff.setString(1, email);
		ResultSet staffResults = checkIfStaff.executeQuery();

		// Same as above, but for DoctorAccount
		PreparedStatement checkIfDoctor = c.prepareStatement("SELECT * FROM DoctorAccount WHERE email = ?");
		checkIfDoctor.setString(1, email);
		ResultSet doctorResults = checkIfDoctor.executeQuery();

		// Same as above, but for PatientAccount
		PreparedStatement checkIfPatient = c.prepareStatement("SELECT * FROM PatientAccount WHERE email = ?");
		checkIfPatient.setString(1, email);
		ResultSet patientResults = checkIfPatient.executeQuery();

		if (staffResults.isBeforeFirst()) {
			// Check to see if provided email is in StaffAccount table - in other words, is this a staff account?
			// If it is, we'll return a StaffAccount object
			retrievedAccount = new StaffAccount(email, DatabaseAccessor.retrievePasswordHash(email));

		} else if (doctorResults.isBeforeFirst()) {
			// Same as above, but for DoctorAccount
			retrievedAccount = new DoctorAccount(email, DatabaseAccessor.retrievePasswordHash(email));
			doctorResults.next();

			((DoctorAccount) retrievedAccount).setProfile(doctorResults.getString("profile"));

		} else if (patientResults.isBeforeFirst()) {
			// Same as above, but for PatientAccount
			retrievedAccount = new PatientAccount(email, DatabaseAccessor.retrievePasswordHash(email));
			patientResults.next();

			((PatientAccount) retrievedAccount).setAddress(patientResults.getString("address"));
			((PatientAccount) retrievedAccount).setHealthNo(patientResults.getString("healthNo"));

			if (patientResults.getString("verifyingStaffMember") != null) {
				// If the patient has a staff member that verified them
				// Set verified to true
				((PatientAccount) retrievedAccount).setVerified(true);
				// Get the email of the staff member who verified them
				((PatientAccount) retrievedAccount).setVerifiedBy(patientResults.getString("verifyingStaffMember"));
			} else {
				// If the patient is unverified
				// Set verified to false
				((PatientAccount) retrievedAccount).setVerified(false);
				// Nobody verified them, so set verifiedBy to null
				((PatientAccount) retrievedAccount).setVerifiedBy(null);
			}

		}

		// Get information common to all account types - Liam
		PreparedStatement generalAccountInfo = c.prepareStatement("SELECT * FROM Account WHERE email = ?");
		generalAccountInfo.setString(1, email);
		ResultSet accountResults = generalAccountInfo.executeQuery();

		if (accountResults.isBeforeFirst()) {
			// Ensure the account actually exists first, just in case
			accountResults.next();
			retrievedAccount.setFirstName(accountResults.getString("firstName"));
			retrievedAccount.setLastName(accountResults.getString("lastName"));
			retrievedAccount.setPhoneNo(accountResults.getString("phoneNo"));
		} else {

		}

		// Close connection
		c.close();
		
		return retrievedAccount;
	}

	public static ConfidentialRecord retrieveRecord(int recordID) throws SQLException {
		ConfidentialRecord retrievedRecord = new ConfidentialRecord(0, "", "");
		Connection c = DatabaseAccessor.connect();

		PreparedStatement checkIfVisit = c.prepareStatement("SELECT * FROM VisitRecord WHERE recordID = ?");
		checkIfVisit.setInt(1, recordID);
		ResultSet visitResults = checkIfVisit.executeQuery();

		PreparedStatement checkIfPrescription = c.prepareStatement("SELECT * FROM Prescription WHERE recordID = ?");
		checkIfPrescription.setInt(1, recordID);
		ResultSet prescriptionResults = checkIfPrescription.executeQuery();

		PreparedStatement checkIfExam = c.prepareStatement("SELECT * FROM LabExam WHERE recordID = ?");
		checkIfExam.setInt(1, recordID);
		ResultSet examResults = checkIfExam.executeQuery();

		PreparedStatement checkIfExamData = c.prepareStatement("SELECT * FROM LabExamResult WHERE recordID = ?");
		checkIfExamData.setInt(1, recordID);
		ResultSet examDataResults = checkIfExamData.executeQuery();

		PreparedStatement genericRecordData = c.prepareStatement("SELECT * FROM ConfidentialRecord WHERE recordID = ?");
		genericRecordData.setInt(1, recordID);
		ResultSet recordResults = genericRecordData.executeQuery();

		if (recordResults.isBeforeFirst()) {

			if (visitResults.isBeforeFirst()) {
				retrievedRecord = new VisitRecord(recordID);
				visitResults.next();
				((VisitRecord) retrievedRecord).setVisitDate(visitResults.getDate("date"));

			} else if (prescriptionResults.isBeforeFirst()) {
				retrievedRecord = new Prescription(recordID);
				prescriptionResults.next();

				int relatedVisit = prescriptionResults.getInt("relatedVisitRecord");
				((Prescription) retrievedRecord).setDatePrescribed(
						((VisitRecord) DatabaseAccessor.retrieveRecord(relatedVisit)).getVisitDate());

				((Prescription) retrievedRecord).setMedicineDose(prescriptionResults.getString("medDose"));
				((Prescription) retrievedRecord).setMedicineName(prescriptionResults.getString("medName"));
				((Prescription) retrievedRecord).setMedicineQuantity(prescriptionResults.getString("medQuantity"));
				((Prescription) retrievedRecord).setRefillable(prescriptionResults.getBoolean("refillable"));

			} else if (examResults.isBeforeFirst()) {
				retrievedRecord = new LabExam(recordID);
				examResults.next();

				((LabExam) retrievedRecord).setExamItem(prescriptionResults.getString("examItem"));
				((LabExam) retrievedRecord).setExamDate(prescriptionResults.getDate("date"));

			} else if (examDataResults.isBeforeFirst()) {
				retrievedRecord = new LabExamResult(recordID);
				examDataResults.next();

				int upper = examDataResults.getInt("upperBound");
				int lower = examDataResults.getInt("lowerBound");
				int result = examDataResults.getInt("result");

				((LabExamResult) retrievedRecord).setRelatedExam(examDataResults.getInt("relatedLabExam"));
				((LabExamResult) retrievedRecord).setNormalUpperBound(upper);
				((LabExamResult) retrievedRecord).setNormalLowerBound(lower);
				((LabExamResult) retrievedRecord).setResult(result);
				((LabExamResult) retrievedRecord).setNormalResult((lower <= result) && (result <= upper));
			}
			recordResults.next();
			retrievedRecord.setPrescribingDoctor(recordResults.getString("relatedDoctor"));
			retrievedRecord.setRelatedPatient(recordResults.getString("relatedPatient"));

		} else {

		}

		// Close connection
		c.close();
		
		return retrievedRecord;
	}

	public static void addAccount(Account newAccount) throws SQLException {

		Connection c = DatabaseAccessor.connect();

		// Add row in Account table
		PreparedStatement addToAccount, addToPatient, addToDoctor, addToStaff;
		addToAccount = c.prepareStatement(
				"INSERT INTO Account (email, passwordHash, firstName, lastName, phoneNo) " + "VALUES (?, ?, ?, ?, ?)");
		addToAccount.setString(1, newAccount.getEmail());
		addToAccount.setString(2, newAccount.getPassword());
		addToAccount.setString(3, newAccount.getFirstName());
		addToAccount.setString(4, newAccount.getLastName());
		addToAccount.setString(5, newAccount.getPhoneNo());
		addToAccount.executeUpdate();

		if (newAccount instanceof PatientAccount) {
			// If patient, add row in PatientAccount table
			addToPatient = c
					.prepareStatement("INSERT INTO PatientAccount (email, verifyingStaffMember, address, healthNo) "
							+ "VALUES (?, ?, ?, ?)");
			addToPatient.setString(1, ((PatientAccount) newAccount).getEmail());
			if (((PatientAccount) newAccount).isVerified()) {
				addToPatient.setString(2, ((PatientAccount) newAccount).getVerifiedBy());
			} else {
				addToPatient.setNull(2, Types.VARCHAR);
			}
			addToPatient.setString(3, ((PatientAccount) newAccount).getAddress());
			addToPatient.setString(4, ((PatientAccount) newAccount).getHealthNo());
			addToPatient.executeUpdate();

		} else if (newAccount instanceof DoctorAccount) {

			addToDoctor = c.prepareStatement("INSERT INTO DoctorAccount (email, profile) " + "VALUES (?, ?)");
			addToDoctor.setString(1, ((DoctorAccount) newAccount).getEmail());
			addToDoctor.setString(2, ((DoctorAccount) newAccount).getProfile());
			addToDoctor.executeUpdate();

		} else if (newAccount instanceof StaffAccount) {

			addToStaff = c.prepareStatement("INSERT INTO StaffAccount (email) " + "VALUES (?)");
			addToStaff.setString(1, newAccount.getEmail());
			addToStaff.executeUpdate();

		}

		// Close connection
		c.close();

	}

	public static void addRecord(ConfidentialRecord newRecord) throws SQLException {

		Connection c = DatabaseAccessor.connect();
		Timestamp t = new Timestamp(Instant.now().toEpochMilli());
		PreparedStatement addToRecord, addToVisit, addToPrescription, addToExam, addToExamResult;
		int id = 0;

		// Add row in ConfidentialRecord table
		// Don't pass a record ID so the database can automatically generate one
		addToRecord = c
				.prepareStatement("INSERT INTO ConfidentialRecord (relatedPatient, relatedDoctor, createdTimeStamp) "
						+ "VALUES (?, ?, ?)");
		addToRecord.setString(1, newRecord.getPrescribingDoctor());
		addToRecord.setString(2, newRecord.getRelatedPatient());
		addToRecord.setTimestamp(3, t);
		addToRecord.executeUpdate();

		// Get the automatically generated ID
		PreparedStatement getGenID = c
				.prepareStatement("SELECT recordID FROM ConfidentialRecord WHERE createdTimeStamp = ?");
		getGenID.setTimestamp(1, t);
		ResultSet genIDResults = getGenID.executeQuery();
		if (genIDResults.isBeforeFirst()) {
			genIDResults.next();
			id = genIDResults.getInt("recordID");
			newRecord.setRecordID(id);
		}

		if (newRecord instanceof VisitRecord) {
			// If visit record, add row in VisitRecord table

			addToVisit = c.prepareStatement("INSERT INTO VisitRecord (recordID, date) " + "VALUES (?, ?)");
			addToVisit.setInt(1, id);
			addToVisit.setDate(2, (Date) ((VisitRecord) newRecord).getVisitDate());
			addToVisit.executeUpdate();

		} else if (newRecord instanceof Prescription) {

			addToPrescription = c.prepareStatement(
					"INSERT INTO Prescription (recordID, relatedVisitRecord, medName, medQuantity, medDose, refillable) "
							+ "VALUES (?, ?, ?, ?, ?, ?)");
			addToPrescription.setInt(1, id);
			addToPrescription.setInt(2, ((Prescription) newRecord).getPrescribedDuring());
			addToPrescription.setString(3, ((Prescription) newRecord).getMedicineName());
			addToPrescription.setString(4, ((Prescription) newRecord).getMedicineQuantity());
			addToPrescription.setString(5, ((Prescription) newRecord).getMedicineDose());
			addToPrescription.setBoolean(6, ((Prescription) newRecord).isRefillable());
			addToPrescription.executeUpdate();

		} else if (newRecord instanceof LabExam) {
			addToExam = c.prepareStatement(
					"INSERT INTO LabExam (recordID, relatedVisitRecord, date, examItem) "
							+ "VALUES (?, ?, ?, ?, ?, ?)");
			addToExam.setInt(1, id);
			addToExam.setInt(2, ((LabExam) newRecord).getPrescribedDuring());
			addToExam.setDate(3, (Date) ((LabExam) newRecord).getExamDate());
			addToExam.setString(4, ((LabExam) newRecord).getExamItem());
			addToExam.executeUpdate();

		} else if (newRecord instanceof LabExamResult) {
			addToExamResult = c.prepareStatement(
					"INSERT INTO LabExamResult (recordID, relatedLabExam, result, upperBound, lowerBound) "
							+ "VALUES (?, ?, ?, ?, ?)");
			addToExamResult.setInt(1, id);
			addToExamResult.setInt(2, ((LabExamResult) newRecord).getRelatedExam());
			addToExamResult.setInt(3, ((LabExamResult) newRecord).getResult());
			addToExamResult.setInt(4, ((LabExamResult) newRecord).getNormalUpperBound());
			addToExamResult.setInt(5, ((LabExamResult) newRecord).getNormalLowerBound());
			addToExamResult.executeUpdate();
		}

		// Close connection
		c.close();

	}
	
	public static void updateAccount(Account account) throws SQLException {

		Connection c = DatabaseAccessor.connect();

		// Update row in Account table
		PreparedStatement updateAccount, updatePatient, updateDoctor;
		updateAccount = c.prepareStatement(
				"UPDATE Account SET password = ?, firstName = ?, lastName = ?, phoneNo = ? WHERE email = ?");
		updateAccount.setString(1, account.getPassword());
		updateAccount.setString(2, account.getFirstName());
		updateAccount.setString(3, account.getLastName());
		updateAccount.setString(4, account.getPhoneNo());
		updateAccount.setString(5, account.getEmail());
		updateAccount.executeUpdate();

		if (account instanceof PatientAccount) {
			// If patient, update row in PatientAccount table
			updatePatient = c
					.prepareStatement("UPDATE PatientAccount SET verifyingStaffMember = ?, address = ?, healthNo = ? WHERE email = ?)");
			
			if (((PatientAccount) account).isVerified()) {
				updatePatient.setString(1, ((PatientAccount) account).getVerifiedBy());
			} else {
				updatePatient.setNull(1, Types.VARCHAR);
			}
			updatePatient.setString(2, ((PatientAccount) account).getAddress());
			updatePatient.setString(3, ((PatientAccount) account).getHealthNo());
			updatePatient.setString(4, ((PatientAccount) account).getEmail());
			updatePatient.executeUpdate();

		} else if (account instanceof DoctorAccount) {
			// If doctor, update row in DoctorAccount table

			updateDoctor = c.prepareStatement("UPDATE DoctorAccount SET profile = ? WHERE email = ?");
			updateDoctor.setString(1, ((DoctorAccount) account).getProfile());
			updateDoctor.setString(2, ((DoctorAccount) account).getEmail());
			updateDoctor.executeUpdate();

		} else if (account instanceof StaffAccount) {
			// No staff-specific updates necessary

		}

		// Close connection
		c.close();

	}
	
	public static void updateRecord(ConfidentialRecord record) throws SQLException {

		Connection c = DatabaseAccessor.connect();
		PreparedStatement updateRecord, updateVisit, updatePrescription, updateExam, updateExamResult;

		// Update row in ConfidentialRecord table
		updateRecord = c
				.prepareStatement("UPDATE ConfidentialRecord SET relatedDoctor = ?, relatedPatient = ? WHERE recordID = ?");
		updateRecord.setString(1, record.getPrescribingDoctor());
		updateRecord.setString(2, record.getRelatedPatient());
		updateRecord.setInt(3, record.getRecordID());
		updateRecord.executeUpdate();

		if (record instanceof VisitRecord) {
			// If visit record, update row in VisitRecord table

			updateVisit = c.prepareStatement("UPDATE VisitRecord SET date = ? WHERE recordID = ?");
			updateVisit.setDate(1, (Date) ((VisitRecord) record).getVisitDate());
			updateVisit.setInt(2, record.getRecordID());
			updateVisit.executeUpdate();

		} else if (record instanceof Prescription) {

			updatePrescription = c.prepareStatement(
					"UPDATE Prescription SET relatedVisitRecord = ?, medName = ?, medQuantity = ?, medDose = ?, refillable = ?) WHERE recordID = ?");
			
			updatePrescription.setInt(1, ((Prescription) record).getPrescribedDuring());
			updatePrescription.setString(2, ((Prescription) record).getMedicineName());
			updatePrescription.setString(3, ((Prescription) record).getMedicineQuantity());
			updatePrescription.setString(4, ((Prescription) record).getMedicineDose());
			updatePrescription.setBoolean(5, ((Prescription) record).isRefillable());
			updatePrescription.setInt(6, record.getRecordID());
			updatePrescription.executeUpdate();

		} else if (record instanceof LabExam) {
			updateExam = c.prepareStatement(
					"UPDATE LabExam SET relatedVisitRecord = ?, date = ?, examItem = ? WHERE recordID = ?");			
			updateExam.setInt(1, ((LabExam) record).getPrescribedDuring());
			updateExam.setDate(2, (Date) ((LabExam) record).getExamDate());
			updateExam.setString(3, ((LabExam) record).getExamItem());
			updateExam.setInt(4, record.getRecordID());
			updateExam.executeUpdate();

		} else if (record instanceof LabExamResult) {
			updateExamResult = c.prepareStatement(
					"UPDATE LabExamResult SET relatedLabExam = ?, result = ?, upperBound = ?, lowerBound = ? WHERE recordID = ?");

			updateExamResult.setInt(1, ((LabExamResult) record).getRelatedExam());
			updateExamResult.setInt(2, ((LabExamResult) record).getResult());
			updateExamResult.setInt(3, ((LabExamResult) record).getNormalUpperBound());
			updateExamResult.setInt(4, ((LabExamResult) record).getNormalLowerBound());
			updateExamResult.setInt(5, record.getRecordID());
			updateExamResult.executeUpdate();
		}

		// Close connection
		c.close();

	}
	
	public static void deleteAccount(Account a) throws SQLException {
		Connection c = DatabaseAccessor.connect();
		
		// Delete the row in Account table
		// ON DELETE CASCADE will do the rest of the work for us
		PreparedStatement deleteAccount = c.prepareStatement("DELETE FROM Account WHERE email = ?");
		deleteAccount.setString(1, a.getEmail());
		deleteAccount.executeUpdate();
		c.close();
	}
	
	public static void deleteRecord(ConfidentialRecord r) throws SQLException {
		Connection c = DatabaseAccessor.connect();
		
		// Delete the row in ConfidentialRecord table
		// ON DELETE CASCADE will do the rest of the work for us
		PreparedStatement deleteRecord = c.prepareStatement("DELETE FROM ConfidentialRecord WHERE recordID = ?");
		deleteRecord.setInt(1, r.getRecordID());
		deleteRecord.executeUpdate();
		c.close();
	}
	
	public static List<Integer> getRecordsForAccount(String email, char recordType) throws SQLException {
		System.out.println("getRecordsForAccount(String): retrieving account info for account " + email);
		return DatabaseAccessor.getRecordsForAccount(DatabaseAccessor.retrieveAccountInfo(email), recordType);
	}
	
	public static List<Integer> getRecordsForAccount(Account account, char recordType) throws SQLException {
		List<Integer> records = new LinkedList<Integer>();
		Connection c = DatabaseAccessor.connect();
		PreparedStatement getRecords;
		ResultSet results;
		String table = "";
		
		switch (recordType) {
		case 'a':
			table = "ConfidentialRecord";
			break;
		case 'v':
			table = "VisitRecord";
			break;
		case 'p':
			table = "Prescription";
			break;
		case 'e':
			table = "LabExam";
			break;
		case 'r':
			table = "LabExamResult";
			break;
		default:
			table = "";		
		}
		System.out.println("Table to search: " + table);
		
		if (account instanceof StaffAccount) {
			getRecords = c.prepareStatement("SELECT recordID FROM " + table);
			System.out.println("Staff account - get all records");
			System.out.print("Executing query... ");
			results = getRecords.executeQuery();
			System.out.println("Query executed successfully");
			
		} else if (account instanceof PatientAccount) {
			getRecords = c.prepareStatement("SELECT recordID FROM " + table + " WHERE relatedPatient = ?");
			getRecords.setString(1, account.getEmail());
			System.out.println("Patient account - get records where relatedPatient = " + account.getEmail());
			System.out.print("Executing query... ");
			results = getRecords.executeQuery();
			System.out.println("Query executed successfully");
			
		} else if (account instanceof DoctorAccount) {
			getRecords = c.prepareStatement("SELECT recordID FROM " + table + " WHERE relatedDoctor = ?");
			getRecords.setString(1, account.getEmail());
			System.out.println("Doctor account - get records where relatedDoctor = " + account.getEmail());
			System.out.print("Executing query... ");
			results = getRecords.executeQuery();
			System.out.println("Query executed successfully");
			
		} else {
			return null;
		}
		while (results.next()) {
			records.add(results.getInt("recordID"));
			System.out.println("Add recordID to return list: " + results.getInt("recordID"));
		}
		
		return records;
	}
	
	public static List<String> getUnverifiedPatients() throws SQLException {
		List<String> patients = new LinkedList<String>();
		
		System.out.println("Getting list of unverified patients");
		Connection c = DatabaseAccessor.connect();
		PreparedStatement getPatients = c.prepareStatement("SELECT email FROM PatientAccount WHERE verifyingStaffMember IS NULL");
		ResultSet results = getPatients.executeQuery();
		
		while (results.next()) {
			patients.add(results.getString("email"));
			System.out.println("Add unverified patient to return list: " + results.getString("email"));
		}
		
		return patients;
	}
	
	// 1st Report: all visit records for one doctor
	public static List<VisitRecord> staffReport_visitsByDoctor(String doctorEmail, boolean isAnnualReport) throws SQLException {
		Connection c = DatabaseAccessor.connect();
		PreparedStatement getRecords;
		List<VisitRecord> records = new LinkedList<VisitRecord>();
		
		System.out.println("Getting recent visit records for doctor " + doctorEmail);
		
		if (isAnnualReport) {
			getRecords = c.prepareStatement("SELECT recordID FROM VisitRecord "
					+ "WHERE date BETWEEN CURDATE() - INTERVAL 1 YEAR AND CURDATE() AND "
					+ "relatedDoctor = ? NATURAL JOIN ConfidentialRecord");
			getRecords.setString(1, doctorEmail);
			System.out.println("Getting last month's records");
		}
		else {
			getRecords = c.prepareStatement("SELECT recordID FROM VisitRecord "
					+ "WHERE date BETWEEN CURDATE() - INTERVAL 1 MONTH AND CURDATE() AND "
					+ "relatedDoctor = ? NATURAL JOIN ConfidentialRecord");
			getRecords.setString(1, doctorEmail); 
			System.out.println("Getting last year's records");
		}
		ResultSet results = getRecords.executeQuery();
		while (results.next()) {
			records.add((VisitRecord) DatabaseAccessor.retrieveRecord(results.getInt("recordID")));
			System.out.println("Add visit record to return list: " + results.getInt("recordID"));
		}
		
		return records;
	}
	
	// 2nd Report: all doctors seen by one patient and how many times 
	public static HashMap<String, Integer> staffReport_visitsByPatient(String patientEmail, boolean isAnnualReport) throws SQLException {
		HashMap<String, Integer> doctorVisits = new HashMap<String, Integer>();
		Connection c = DatabaseAccessor.connect();
		PreparedStatement getRecords;
		
		System.out.println("Getting recent visit records for patient " + patientEmail);
		
		if (isAnnualReport) {
			getRecords = c.prepareStatement("SELECT relatedDoctor, COUNT(*) FROM VisitRecord "
					+ "WHERE date BETWEEN CURDATE() - INTERVAL 1 YEAR AND CURDATE() AND "
					+ "relatedPatient = ? GROUP BY relatedDoctor NATURAL JOIN ConfidentialRecord");
			getRecords.setString(1, patientEmail);
			System.out.println("Getting last month's records");
		}
		else {
			getRecords = c.prepareStatement("SELECT relatedDoctor, COUNT(*) AS Visits FROM VisitRecord "
					+ "WHERE date BETWEEN CURDATE() - INTERVAL 1 MONTH AND CURDATE() AND "
					+ "relatedPatient = ? GROUP BY relatedDoctor NATURAL JOIN ConfidentialRecord");
			getRecords.setString(1, patientEmail); 
			System.out.println("Getting last year's records");
		}
		
		ResultSet results = getRecords.executeQuery();
		while (results.next()) {
			doctorVisits.put(results.getString("relatedDoctor"), results.getInt("Visits"));
			System.out.println("Add visit record to return list: " + results.getInt("recordID"));
		}
		
		return doctorVisits;
	}
	
	// 3rd Report: 3 most commonly prescribed medications
	public static HashMap<String, Integer> staffReport_commonMeds(boolean isAnnualReport) throws SQLException {
		HashMap<String, Integer> medications = new HashMap<String, Integer>();
		Connection c = DatabaseAccessor.connect();
		PreparedStatement getMeds;
		
		if (isAnnualReport) {
			getMeds = c.prepareStatement("SELECT medName, COUNT(*) AS numPrescribed FROM Prescription "
					+ "WHERE date BETWEEN CURDATE() - INTERVAL 1 YEAR AND CURDATE()"
					+ "GROUP BY medName NATURAL JOIN ConfidentialRecord"
					+ "INNER JOIN VisitRecord ON VisitRecord.recordID=Prescription.relatedVisitRecord"
					+ "ORDER BY numPrescribed LIMIT 3");
			System.out.println("Getting last month's records");
		}
		else {
			getMeds = c.prepareStatement("SELECT medName, COUNT(*) FROM Prescription "
					+ "WHERE date BETWEEN CURDATE() - INTERVAL 1 MONTH AND CURDATE()"
					+ "GROUP BY medName NATURAL JOIN ConfidentialRecord"
					+ "INNER JOIN VisitRecord ON VisitRecord.recordID=Prescription.relatedVisitRecord"
					+ "ORDER BY numPrescribed LIMIT 3");
			System.out.println("Getting last year's records");
		}
		ResultSet results = getMeds.executeQuery();
		while (results.next()) {
			medications.put(results.getString("medName"), results.getInt("numPrescribed"));
			System.out.println("Add prescription to return list: " + results.getInt("medName"));
		}
		
		return medications;
	}

	public static Connection connect() throws SQLException {

		// Create connection with database - Liam
		return DriverManager.getConnection("jdbc:mysql://localhost/INFO2413DB?" + 
											"user=accessor&password=DB_Accessor");
	}
}
