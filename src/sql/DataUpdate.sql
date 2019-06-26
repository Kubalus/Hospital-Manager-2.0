USE hospitalManager;

DROP TRIGGER IF EXISTS diseasesUpdated;
DELIMITER &&
CREATE TRIGGER diseasesUpdated
BEFORE UPDATE ON diseases
FOR EACH ROW
	BEGIN
    IF old.Leading_Doctor IS NULL
		THEN
        CALL addNewLog(CONCAT('Przydzielono lekarza prowadzącego : ', 
        (SELECT Full_Name FROM doctors WHERE PESEL = new.Leading_Doctor)), disease, 'system');
    ELSEIF old.Leading_Doctor <> new.Leading_Doctor
		THEN
        CALL addNewLog(CONCAT('Zmieniono lekarza prowadzącego z ', 
        (SELECT Full_Name FROM doctors WHERE PESEL = old.Leading_Doctor), ' na ',
        (SELECT Full_Name FROM doctors WHERE PESEL = new.Leading_Doctor)), disease, 'system');
	END IF;
    
	
    END &&
DELIMITER ;

DROP PROCEDURE IF EXISTS cureDisease;
                                        
DELIMITER $$
CREATE PROCEDURE cureDisease(IN disease INT)
	BEGIN
    
    DECLARE diseaseName VARCHAR(30);
    DECLARE patientPESEL VARCHAR(11);
    DECLARE patientName VARCHAR(80);
    DECLARE note TEXT;
    DECLARE counter INT;
    
    
    SELECT Disease_Name FROM diseasetype WHERE diseasetype.ID = disease INTO diseaseName;
    
    SELECT Patient FROM disease WHERE diseases.ID = disease INTO patientPESEL;
    SELECT Full_Name FROM patients WHERE patients.PESEL = patientPESEL INTO patientName;
    
    SET note = CONCAT('Wyleczono ', diseaseName);
    CALL addNewLog(note, disease, 'system');
    
    UPDATE diseases 
		SET diseases.State = 'CURED'
		WHERE diseases.ID = disease;
    
    SELECT COUNT(ID) FROM diseases WHERE State = 'PROCESSED' AND Patient = new.Patient INTO counter;
    
    IF counter = 0
		THEN
        SET note = CONCAT('Pacjent ', diseaseName, ' został wypisany ze szpitala');
        CALL addNewLog(note, disease, 'system');
	END IF;
    
    END $$
    
DELIMITER ;                                     
			
            
DROP PROCEDURE IF EXISTS changeLeadingDoctor;

DELIMITER $$
CREATE PROCEDURE changeLeadingDoctor(IN doctor VARCHAR(11), IN disease INT)
	BEGIN
    UPDATE diseases
		SET diseases.Leading_Doctor = doctor
		WHERE diseases.ID = disease;
    
    END $$
    
DELIMITER ;

DROP PROCEDURE IF EXISTS changePassword;

DELIMITER $$
CREATE PROCEDURE changePassword(IN user VARCHAR(30), IN oldPassword CHAR(76), IN newPassword CHAR(76))
	BEGIN
    DECLARE password CHAR(41);
    
   SELECT password FROM users WHERE Login = user INTO password;
    
    IF password = oldPassword
		THEN
        UPDATE users 
			SET users.Password = newPassword
			WHERE users.Login = user;
	ELSE 
		SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = 'Wrong Password';
	END IF;
    
    END $$
    
DELIMITER ;

