USE hospitalManager;

DROP PROCEDURE IF EXISTS addNewPatientData;

DELIMITER $$
CREATE PROCEDURE addNewPatientData(IN name VARCHAR(80), IN PESEL VARCHAR(11),
							   IN birthdayDate DATE, IN password CHAR(76))
	BEGIN
    DECLARE userID INT;
    START TRANSACTION;
		INSERT INTO users(Login, Password, Access_lvl) VALUES (CONCAT('P',PESEL), password, 'patient');
		SELECT ID FROM users WHERE Login = CONCAT('P',PESEL) INTO userID;
		INSERT INTO patients(PESEL, Full_Name, Birth_Date, User_ID) 
			VALUES (PESEL, name, birthdayDate, userID);
	COMMIT;
    
    END $$
    
DELIMiTER ;
                       
DROP PROCEDURE IF EXISTS addNewDoctor;

DELIMITER $$
CREATE PROCEDURE addNewDoctor(IN name VARCHAR(80), IN PESEL VARCHAR(11),
							   IN birthdayDate DATE, IN password CHAR(76))
	BEGIN
    DECLARE userID INT;
    START TRANSACTION;
		INSERT INTO users(Login, Password, Access_lvl) VALUES (CONCAT('D',PESEL), password, 'doctor');
		SELECT ID FROM users WHERE Login = CONCAT('D',PESEL) INTO userID;
		INSERT INTO doctors(PESEL, Full_Name, Birth_Date, User_ID) 
			VALUES (PESEL, name, birthdayDate, userID);
		COMMIT;
    END $$
    
DELIMITER ;

DROP PROCEDURE IF EXISTS addNewLog;

DELIMITER $$
CREATE PROCEDURE addNewLog(IN note TEXT, IN disease INT,IN author VARCHAR(30))
	BEGIN
    
    DECLARE time DATETIME;
    SET time = NOW();
    
    INSERT INTO diseaseshistory(Date, Disease, Author, Description) 
		VALUES (time, disease, author, note);
							   
    END $$
    
DELIMITER ;


DROP PROCEDURE IF EXISTS addNewPatientToHospital;

DELIMITER $$
CREATE PROCEDURE addNewPatientToHospital(IN patient VARCHAR(11), IN doctor VARCHAR(11),IN disease INT,
							   IN description TEXT)
	BEGIN
    DECLARE note TEXT;
    DECLARE patientName VARCHAR(80);
    DECLARE doctorName VARCHAR(80);
    DECLARE diseaseName VARCHAR(30);
    
    START TRANSACTION;
		SELECT Full_Name FROM patients WHERE patients.PESEL = patient INTO patientName;
		SELECT Full_Name FROM doctors WHERE doctors.PESEL = doctor INTO doctorName;
		SELECT Disease_Name FROM diseasestype WHERE diseasestype.ID = disease INTO diseaseName;
    
		INSERT INTO diseases(Patient, Leading_Doctor, Diagnosis_Doctor, Diseases_Type, State) 
			VALUES (patient, NULL, doctor, disease, 'PROCESSED');
	
		SET note = CONCAT('Pacjent ', patientName, ' został przyjęty na oddział. Diagnoza : ', Disease_Name,
						' wykonana przez : ',doctorName, ' dodatkowa notatka : ', description);
                      
		CALL addNewLog(note, disease, 'system');
	COMMIT;
                      
    END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS addNewDiseaseType;

DELIMITER $$
CREATE PROCEDURE addNewDiseaseType(IN name VARCHAR(30), IN cure VARCHAR(30))
	BEGIN
    DECLARE curing_ID INT;
    SELECT ID FROM specializations WHERE cure = specializations.Spec_Name INTO curing_ID;
    INSERT INTO diseasesType (Disease_Name, Curing_Specialization) VALUES (name, curing_ID);
    END $$
    
DELIMITER ;


DROP PROCEDURE IF EXISTS addNewSpecialization;

DELIMITER $$
CREATE PROCEDURE addNewSpecialization(IN name VARCHAR(30))
	BEGIN
    INSERT INTO specializations (Spec_Name) VALUES (name);
    END $$
    
DELIMITER ;

DROP PROCEDURE IF EXISTS addDoctorNewSpecialization;

DELIMITER $$
CREATE PROCEDURE addDoctorNewSpecialization(IN doctor VARCHAR(11), IN spec VARCHAR(11))
	BEGIN
    INSERT INTO doctorsSpecializations(Doctor, Specialization) VALUES (doctor, spec) ;
    END $$
    
DELIMITER ;

/*
DROP FUNCTION IF EXISTS generatePESEL;

DELIMITER $$
CREATE FUNCTION generatePESEL(birthDate DATE) RETURNS VARCHAR(11)
	NOT DETERMINISTIC
	BEGIN
		DECLARE birthYear TINYINT;
		DECLARE birthDay TINYINT;
        DECLARE birthMonth TINYINT;
        DECLARE rand CHAR(5);
        
        SET birthYear = YEAR(birthDate);
        SET birthDay = DAY(birthDate);
        SET rand = CAST(FLOOR(RAND()*99999) AS CHAR(5));
        
        IF birthYear >= 1900 AND birthYear < 2000
			THEN 
            SET birthMonth = MONTH(birthDate);
		ELSEIF birthYear >= 2000 AND birthYear < 2100
        	THEN 
            SET birthMonth = MONTH(birthDate) + 20;
		ELSE
			SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'Wrong Year';
		END IF;
	RETURN (CONCAT(SUBSTRING(CAST(birthYear AS CHAR(4)),1,2),CAST(birthMonth AS CHAR(2)),
			CAST(birthDay AS CHAR(2)),rand));
        
    END $$
    
DELIMITER ;


CALL addNewPatientData('Jakub Bryniarski','97011012050',
	'1997-01-10','1000:3937303131303132303530:9eb49c0b6e62ec4899776374e7014633e57e8963ed1ee88f');
    
    Select * from users;
    Select * from patients;