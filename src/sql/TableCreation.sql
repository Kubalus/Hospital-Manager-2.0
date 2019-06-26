
DROP SCHEMA IF EXISTS hospitalManager;
CREATE SCHEMA hospitalManager;
USE hospitalManager;

DROP TABLE IF EXISTS users;

CREATE TABLE users(
	ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
	Login VARCHAR(30) NOT NULL UNIQUE,
    Password CHAR(76) NOT NULL,
    Access_lvl ENUM('patient','doctor','director'),
    PRIMARY KEY (ID)
    );

DROP TABLE IF EXISTS doctors;
CREATE TABLE doctors(
	PESEL VARCHAR(11) NOT NULL,
    Full_Name VARCHAR(80) NOT NULL,
    Birth_date DATE NOT NULL,
    User_ID INT UNSIGNED NOT NULL UNIQUE,
    PRIMARY KEY(PESEL),
    FOREIGN KEY (User_ID) REFERENCES users(ID)
    );
    
DROP TABLE IF EXISTS patients;

CREATE TABLE patients(
	PESEL VARCHAR(11) NOT NULL,
    Full_Name VARCHAR(80) NOT NULL,
    Birth_date DATE NOT NULL,
	User_ID INT UNSIGNED NOT NULL UNIQUE,
    PRIMARY KEY(PESEL),
    FOREIGN KEY (User_ID) REFERENCES users(ID)
    );
    
DROP TABLE IF EXISTS specializations;
    
CREATE TABLE specializations(
	ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
    Spec_Name VARCHAR(30) NOT NULL,
    PRIMARY KEY (ID)
    );
    
DROP TABLE IF EXISTS diseasesType;
    
CREATE TABLE diseasesType(
	ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
    Disease_Name VARCHAR(30) NOT NULL,
    Curing_Specialization INT UNSIGNED NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (Curing_Specialization) REFERENCES specializations(ID)
    );
    
DROP TABLE IF EXISTS diseases;
CREATE TABLE diseases(
	ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
	Patient VARCHAR(11) NOT NULL,
    Leading_Doctor VARCHAR(11),
    Diagnosis_doctor VARCHAR(11) NOT NULL,
    Diseases_Type INT UNSIGNED NOT NULL,
    State ENUM ('CURED', 'PROCESSED'),
    PRIMARY KEY (ID),
    FOREIGN KEY (Patient) REFERENCES patients(PESEL),
    FOREIGN KEY (Leading_Doctor) REFERENCES doctors(PESEL),
    FOREIGN KEY (Diagnosis_Doctor) REFERENCES doctors(PESEL),
    FOREIGN KEY (Diseases_Type) REFERENCES diseasesType(ID)
);

DROP TABLE IF EXISTS doctorsSpecializations;

CREATE TABLE doctorsSpecializations(
	Doctor VARCHAR(11) NOT NULL,
    Specialization INT UNSIGNED NOT NULL,
    FOREIGN KEY (Doctor) REFERENCES doctors(PESEL),
    FOREIGN KEY (Specialization) REFERENCES specializations(ID)
    );
    
DROP TABLE IF EXISTS diseasesHistory;

CREATE TABLE diseasesHistory(
	ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
	Date DATETIME NOT NULL,
    Disease INT UNSIGNED NOT NULL,
    Author VARCHAR(30) NOT NULL,
    Description TEXT,
	PRIMARY KEY (ID),
    FOREIGN KEY (Disease) REFERENCES diseases(ID)
);
