CREATE TABLE system_status (
	status_name VARCHAR(200) PRIMARY KEY,
	status_value VARCHAR(100)
);

INSERT INTO system_status VALUES ('registration_status', 'close');
INSERT INTO system_status VALUES ('update_instance_status', 'open');
INSERT INTO system_status VALUES ('result_status', 'pending');