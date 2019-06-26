SELECT * FROM hospitalmanager.diseases;

CALL addNewPatientData('Jakub Bryniarski','97011012050',
	'1997-01-10','1000:3937303131303132303530:9eb49c0b6e62ec4899776374e7014633e57e8963ed1ee88f');
    
CALL addNewDoctor('DR Adam Nowak ','97011012050',
	'1997-01-10','1000:3937303131303132303530:9eb49c0b6e62ec4899776374e7014633e57e8963ed1ee88f');
    
CALL addNewSpecialization('Kardiologia');
CALL addNewSpecialization('Onkologia');
CALL addNewSpecialization('Endokrynologia');
CALL addNewSpecialization('Chirurgia');
CALL addNewSpecialization('Okulistyka');
CALL addNewSpecialization('Kardiologia');
CALL addNewSpecialization('Ortopedia');
SELECT * FROM specializations;

CALL addNewDiseaseType('Zawal Serca','Kardiologia');
CALL addNewDiseaseType('Migotanie Przedsionka','Kardiologia');
CALL addNewDiseaseType('Rak Trzustki','Onkologia');
CALL addNewDiseaseType('Rak Watroby','Onkologia');
CALL addNewDiseaseType('Niedoczynnosc Tarczycy','Endokrynologia');
CALL addNewDiseaseType('Nadczynnosc Tarczycy','Endokrynologia');
CALL addNewDiseaseType('Zlamanie Otwarte','Chirurgia');
CALL addNewDiseaseType('Zlamanie Zamkniete','Chirurgia');
CALL addNewDiseaseType('Odklejanie Siatkowki','Okulistyka');
CALL addNewDiseaseType('Jaskra','Okulistyka');
CALL addNewDiseaseType('Zerwanie Sciegna','Ortopedia');
CALL addNewDiseaseType('Plyn w stawie','Ortopedia');
SELECT * FROM diseases;

SELECT * FROM hospitalmanager.diseases;

SELECT patients.Full_Name as patient, Disease_Name, diseases.ID, patients.PESEL as pesel,
 doctors.Full_Name as doctor FROM diseases 
JOIN patients ON diseases.Patient = patients.PESEL
JOIN diseasestype ON diseases.Diseases_Type = diseasestype.ID
JOIN doctors ON diseases.diagnosis_doctor = doctors.PESEL 
 WHERE State = 'PROCESSED' AND Leading_Doctor = '97011012050'; 


SELECT * FROM diseasestype;

SELECT Author, Date,Description FROM diseaseshistory WHERE diseaseshistory.Disease = 1;