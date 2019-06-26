USE hospitalManager;

DROP PROCEDURE IF EXISTS checkPESEL;
DELIMITER $$
CREATE PROCEDURE checkPESEL(IN birthdayDate DATE, IN PESEL VARCHAR(11))
	BEGIN
	DECLARE PESELDay TINYINT;
    DECLARE PESELMonth TINYINT;
    DECLARE PESELYear TINYINT;
    DECLARE PESELRandom INT;
    DECLARE PESELLength INT;
    DECLARE yearOver2000Flag TINYINT DEFAULT 2;
	DECLARE PESELDate DATE;
    
    
    SET PESELYear = CONVERT(SUBSTRING(PESEL, 1, 2), UNSIGNED INTEGER);
	SET PESELMonth = CONVERT(SUBSTRING(PESEL, 3, 2), UNSIGNED INTEGER);
    SET PESELDay = CONVERT(SUBSTRING(PESEL, 5, 2), UNSIGNED INTEGER);
    SET PESELLength = CHAR_LENGTH(PESEL);
    
    	-- Checking PESEL length 

    IF PESELLength <> 11
		THEN
		SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = 'PESEL with wrong Length';
    END IF;
    
    -- Checking correctness of Month in Pesel
    
    IF PESELMonth <= 12 
		THEN SET yearOver2000Flag = 0;
	ELSEIF PESELMonth >=20 AND PESELMonth <= 32
		THEN SET yearOver2000Flag = 1;
	ELSE
		SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = 'Wrong Month in PESEL';
	END IF;
    
	-- Checking correctness of Day in Pesel
    
    IF PESELDay < 1 OR PESELDay > 31
		THEN
		SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = 'Wrong Day in PESEL';
    END IF;
	

    
	-- Comparing date form PESEL with Date from data 

    IF yearOver2000Flag = 0
		THEN
		SET PESELDate = CAST(( (1900 + PESELYear) * 10000 + (PESELMonth * 100) 
					+ PESELDay) AS DATE);
                    
	ELSEIF yearOver2000Flag = 1 
		THEN
		SET PESELDate = CAST(( (2000 + PESELYear) * 10000 + ((PESELMonth-20) * 100) 
					+ PESELDay) AS DATE);
                    
    ELSE
		SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = 'Problem with Flag';
	END IF;
   
       
    IF PESELDate <> birthdayDate
    THEN
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Date from PESEL and from Data are not the same';
    END IF;
    
    
	-- Checking last 5 random number
    
    SET PESELRandom = CONVERT(SUBSTRING(PESEL, 7, 5), UNSIGNED INTEGER);
    
  END$$
DELIMITER ;

CALL checkPESEL( '2000-01-10' , '00211011111')