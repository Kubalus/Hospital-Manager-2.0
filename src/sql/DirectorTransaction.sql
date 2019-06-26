use hospitalManager;

DROP PROCEDURE IF EXISTS newDirector;

DELIMITER $$

CREATE PROCEDURE newDirector(IN doctor VARCHAR(11))
	BEGIN
	DECLARE directorID INT;
    DECLARE doctorID INT;
    DECLARE counter INT;
    SELECT User_ID FROM doctors WHERE PESEL = doctor INTO doctorID;
    SELECT COUNT(ID) FROM users WHERE Access_lvl = 'director' INTO counter;
    IF counter = 0
		THEN
        UPDATE users 
			SET Access_lvl = 'director'
			WHERE users.ID = doctorID;
	ELSE
		SELECT ID FROM users WHERE Access_lvl = 'director' INTO directorID;
		START TRANSACTION;
		UPDATE users 
			SET Access_lvl = 'director'
			WHERE users.ID = doctorID;
		UPDATE users 
			SET Access_lvl = 'doctor'
			WHERE users.ID = directorID;
		COMMIT;
	END IF;
	END $$
    
DELIMITER ;