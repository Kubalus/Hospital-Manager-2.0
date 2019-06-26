USE hospitalManager;

DROP TRIGGER IF EXISTS newPatient;

DELIMITER &&

CREATE TRIGGER newPatient
BEFORE INSERT ON patients
FOR EACH ROW
	BEGIN
    CALL checkPESEL(new.Birth_date, new.PESEL);
    END &&
    
DELIMITER ;

DROP TRIGGER IF EXISTS newDoctor;

DELIMITER &&

CREATE TRIGGER newDoctor
BEFORE INSERT ON doctors
FOR EACH ROW
	BEGIN
    CALL checkPESEL(new.Birth_date, new.PESEL);
    END &&
    
DELIMITER ;