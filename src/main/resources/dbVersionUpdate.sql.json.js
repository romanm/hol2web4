{
	"dbVersionUpdateList" : [ 
{
	"dbVersionId" : 0, "sqls" : [
	"CREATE TABLE IF NOT EXISTS dbversion (dbversion_id INT PRIMARY KEY, dbversion_date TIMESTAMP DEFAULT NOW())"
	,"CREATE SEQUENCE if not exists dbid" 
]},{
	"dbVersionId" : 1, "sqls" : [
	"DROP SCHEMA IF EXISTS hol2"
	,"DROP SCHEMA IF EXISTS hol1"
	,"CREATE SCHEMA hol1"
	,"CREATE TABLE hol1.department (department_id SMALLINT AUTO_INCREMENT PRIMARY KEY, department_name VARCHAR NOT NULL, department_active BOOLEAN DEFAULT TRUE, department_profile_id TINYINT NOT NULL)"
	,"DELETE FROM hol1.department "
	,"INSERT INTO hol1.department VALUES (1,'Алергологiчне',1,1)"
	,"INSERT INTO hol1.department VALUES (2,'Гінекологічне',1,2)"
	,"INSERT INTO hol1.department VALUES (3,'Гастроентерологічне',1,1)"
	,"INSERT INTO hol1.department VALUES (4,'Гематологічне',1,1)"
	,"INSERT INTO hol1.department VALUES (5,'Детоксикації',1,1)"
	,"INSERT INTO hol1.department VALUES (6,'ЕГ реанімація доросла',0,1)"
	,"INSERT INTO hol1.department VALUES (7,'ЕГПВ та Н',0,1)"
	,"INSERT INTO hol1.department VALUES (8,'Ендокринологія',1,1)"
	,"INSERT INTO hol1.department VALUES (9,'Кардіохірургічне',1,2)"
	,"INSERT INTO hol1.department VALUES (10,'Кардіореанімація',1,2)"
	,"INSERT INTO hol1.department VALUES (11,'Неврологічне',1,1)"
	,"INSERT INTO hol1.department VALUES (12,'Нейрохірургічне',1,2)"
	,"INSERT INTO hol1.department VALUES (13,'Нефрологічне',1,1)"
	,"INSERT INTO hol1.department VALUES (14,'Опікове',1,2)"
	,"INSERT INTO hol1.department VALUES (15,'Ортопедичне',1,2)"
	,"INSERT INTO hol1.department VALUES (16,'Офтальмологічне',1,2)"
	,"INSERT INTO hol1.department VALUES (17,'Післяпологове',0,1)"
	,"INSERT INTO hol1.department VALUES (18,'Патологія вагітних',0,1)"
	,"INSERT INTO hol1.department VALUES (19,'Приймальне',1,3)"
	,"INSERT INTO hol1.department VALUES (20,'Проктологічне',1,2)"
	,"INSERT INTO hol1.department VALUES (21,'Пульмонологічне',1,1)"
	,"INSERT INTO hol1.department VALUES (22,'Реанімаційне',1,1)"
	,"INSERT INTO hol1.department VALUES (23,'Ревматологічне',1,1)"
	,"INSERT INTO hol1.department VALUES (24,'Судинної хірургії',1,2)"
	,"INSERT INTO hol1.department VALUES (25,'Торакальної хірургії',1,2)"
	,"INSERT INTO hol1.department VALUES (26,'Урологічне',1,2)"
	,"INSERT INTO hol1.department VALUES (27,'Хірургічне',1,2)"
	,"INSERT INTO hol1.department VALUES (28,'Щелепно-лицьової хірургії',1,2)"
	,"INSERT INTO hol1.department VALUES (29,'Поліклініка',1,4)"
	,"CREATE SCHEMA hol2"
	,"CREATE TABLE hol2.movedepartmentpatient (movedepartmentpatient_id INT DEFAULT (NEXT VALUE FOR dbid) PRIMARY KEY, movedepartmentpatient_date DATE NOT NULL, movedepartmentpatient_value TINYINT, department_id SMALLINT NOT NULL, movedepartmentpatient_updownit TINYINT NOT NULL)"
	,"ALTER TABLE hol2.movedepartmentpatient ADD CONSTRAINT department_id FOREIGN KEY (department_id) REFERENCES hol1.department(department_id)"
]},{
	"dbVersionId" : 6, "sqls" : [
"DROP TABLE IF EXISTS hol2.movedepartmentpatient"
,"CREATE TABLE hol2.movedepartmentpatient ( movedepartmentpatient_id INT PRIMARY KEY, movedepartmentpatient_date DATE NOT NULL, department_id SMALLINT NOT NULL, movedepartmentpatient_in TINYINT, movedepartmentpatient_out TINYINT, movedepartmentpatient_it TINYINT, movedepartmentpatient_bed TINYINT, movedepartmentpatient_patient1day TINYINT, movedepartmentpatient_patient2day TINYINT, movedepartmentpatient_dead TINYINT, movedepartmentpatient_indepartment TINYINT, movedepartmentpatient_outdepartment TINYINT, movedepartmentpatient_sity TINYINT, movedepartmentpatient_child TINYINT, movedepartmentpatient_lying TINYINT, movedepartmentpatient_insured TINYINT)"
,"ALTER TABLE hol2.movedepartmentpatient ADD CONSTRAINT department_id FOREIGN KEY (department_id) REFERENCES hol1.department(department_id)"
]}
	
]}
