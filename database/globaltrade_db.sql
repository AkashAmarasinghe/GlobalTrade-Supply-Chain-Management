
CREATE DATABASE IF NOT EXISTS globaltrade CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE globaltrade;

CREATE TABLE IF NOT EXISTS roles (
 id BIGINT AUTO_INCREMENT PRIMARY KEY, role_name VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS users (
 id BIGINT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(50) NOT NULL UNIQUE, password_hash VARCHAR(255) NOT NULL, full_name VARCHAR(100) NOT NULL, email VARCHAR(120), active BOOLEAN NOT NULL DEFAULT TRUE, role_id BIGINT NOT NULL, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT fk_users_role FOREIGN KEY(role_id) REFERENCES roles(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS vendors (
 id BIGINT AUTO_INCREMENT PRIMARY KEY, vendor_code VARCHAR(30) NOT NULL UNIQUE, vendor_name VARCHAR(120) NOT NULL, contact_person VARCHAR(100), email VARCHAR(120), phone VARCHAR(30), country VARCHAR(80), status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE', performance_score DECIMAL(5,2) NOT NULL DEFAULT 0.00, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS inventory_items (
 id BIGINT AUTO_INCREMENT PRIMARY KEY, sku VARCHAR(50) NOT NULL UNIQUE, item_name VARCHAR(120) NOT NULL, description VARCHAR(255), quantity_on_hand INT NOT NULL DEFAULT 0, reorder_level INT NOT NULL DEFAULT 10, unit VARCHAR(30) DEFAULT 'units', vendor_id BIGINT NULL, last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, CONSTRAINT fk_inventory_vendor FOREIGN KEY(vendor_id) REFERENCES vendors(id) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS shipments (
 id BIGINT AUTO_INCREMENT PRIMARY KEY, shipment_number VARCHAR(50) NOT NULL UNIQUE, vendor_id BIGINT NULL, carrier_name VARCHAR(120), tracking_reference VARCHAR(100), origin_country VARCHAR(80) NOT NULL, destination_country VARCHAR(80) NOT NULL, departure_date DATETIME NULL, expected_arrival_date DATETIME NULL, actual_arrival_date DATETIME NULL, status VARCHAR(30) NOT NULL DEFAULT 'CREATED', created_by BIGINT NULL, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, CONSTRAINT fk_shipment_vendor FOREIGN KEY(vendor_id) REFERENCES vendors(id) ON DELETE SET NULL, CONSTRAINT fk_shipment_user FOREIGN KEY(created_by) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS shipment_items (
 id BIGINT AUTO_INCREMENT PRIMARY KEY, shipment_id BIGINT NOT NULL, inventory_item_id BIGINT NOT NULL, quantity INT NOT NULL, CONSTRAINT fk_si_shipment FOREIGN KEY(shipment_id) REFERENCES shipments(id) ON DELETE CASCADE, CONSTRAINT fk_si_inventory FOREIGN KEY(inventory_item_id) REFERENCES inventory_items(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS customs_documents (
 id BIGINT AUTO_INCREMENT PRIMARY KEY, shipment_id BIGINT NOT NULL, document_type VARCHAR(80) NOT NULL, document_number VARCHAR(100), status VARCHAR(30) NOT NULL DEFAULT 'PENDING', due_date DATETIME NULL, submitted_at DATETIME NULL, approved_at DATETIME NULL, notes VARCHAR(500), created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, CONSTRAINT fk_customs_shipment FOREIGN KEY(shipment_id) REFERENCES shipments(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS vendor_performance (
 id BIGINT AUTO_INCREMENT PRIMARY KEY, vendor_id BIGINT NOT NULL, evaluation_date DATE NOT NULL, delivery_score DECIMAL(5,2) DEFAULT 0, quality_score DECIMAL(5,2) DEFAULT 0, compliance_score DECIMAL(5,2) DEFAULT 0, overall_score DECIMAL(5,2) DEFAULT 0, remarks VARCHAR(500), created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, CONSTRAINT fk_perf_vendor FOREIGN KEY(vendor_id) REFERENCES vendors(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS audit_logs (
 id BIGINT AUTO_INCREMENT PRIMARY KEY, actor VARCHAR(100) NOT NULL DEFAULT 'SYSTEM', action VARCHAR(150) NOT NULL, entity_type VARCHAR(100), entity_id BIGINT NULL, details VARCHAR(1000), execution_time_ms BIGINT DEFAULT 0, success BOOLEAN NOT NULL DEFAULT TRUE, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS system_alerts (
 id BIGINT AUTO_INCREMENT PRIMARY KEY, severity VARCHAR(20) NOT NULL DEFAULT 'INFO', category VARCHAR(50) NOT NULL, title VARCHAR(150) NOT NULL, message VARCHAR(1000) NOT NULL, reference_type VARCHAR(80), reference_id BIGINT NULL, resolved BOOLEAN NOT NULL DEFAULT FALSE, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, resolved_at DATETIME NULL
) ENGINE=InnoDB;

INSERT INTO roles(role_name) VALUES ('ADMIN'),('LOGISTICS_COORDINATOR'),('WAREHOUSE_MANAGER'),('CUSTOMS_AGENT'),('VENDOR'),('CUSTOMER') ON DUPLICATE KEY UPDATE role_name=VALUES(role_name);

-- User accounts are not seeded in public source control.
-- Run tools/create_local_users.py to generate private SQL with your own passwords.
-- Import the resulting local_users.sql after importing this file.

INSERT INTO vendors(vendor_code,vendor_name,contact_person,email,phone,country,status,performance_score) VALUES
('VND-0018','Nippon Components','Akira Sato','operations@nippon.example','+81-000-1000','Japan','ACTIVE',91.00),
('VND-0023','Meridian Supply','Melvin Tan','supply@meridian.example','+65-000-2000','Singapore','ACTIVE',87.00)
ON DUPLICATE KEY UPDATE vendor_name=VALUES(vendor_name), performance_score=VALUES(performance_score);

INSERT INTO inventory_items(sku,item_name,description,quantity_on_hand,reorder_level,unit,vendor_id)
SELECT 'SNS-A17','Industrial Sensor A17','Industrial monitoring sensor',84,25,'units',v.id FROM vendors v WHERE v.vendor_code='VND-0018'
ON DUPLICATE KEY UPDATE item_name=VALUES(item_name), quantity_on_hand=VALUES(quantity_on_hand), reorder_level=VALUES(reorder_level);
INSERT INTO inventory_items(sku,item_name,description,quantity_on_hand,reorder_level,unit,vendor_id)
SELECT 'CTL-X2','Control Module X2','Supply chain control module',41,20,'units',v.id FROM vendors v WHERE v.vendor_code='VND-0023'
ON DUPLICATE KEY UPDATE item_name=VALUES(item_name), quantity_on_hand=VALUES(quantity_on_hand), reorder_level=VALUES(reorder_level);

INSERT INTO shipments
(shipment_number,vendor_id,carrier_name,tracking_reference,origin_country,destination_country,departure_date,expected_arrival_date,actual_arrival_date,status,created_by)
VALUES
('GT-SHP-1001',(SELECT id FROM vendors WHERE vendor_code='VND-0018'),'Oceanic Air Cargo','TRK-JP-1001','Japan','Sri Lanka',NOW()-INTERVAL 1 DAY,NOW()+INTERVAL 2 DAY,NULL,'IN_TRANSIT',(SELECT id FROM users WHERE username='admin'))
ON DUPLICATE KEY UPDATE vendor_id=VALUES(vendor_id), carrier_name=VALUES(carrier_name), tracking_reference=VALUES(tracking_reference), origin_country=VALUES(origin_country), destination_country=VALUES(destination_country), departure_date=VALUES(departure_date), expected_arrival_date=VALUES(expected_arrival_date), actual_arrival_date=VALUES(actual_arrival_date), status=VALUES(status), created_by=VALUES(created_by);

INSERT INTO shipments
(shipment_number,vendor_id,carrier_name,tracking_reference,origin_country,destination_country,departure_date,expected_arrival_date,actual_arrival_date,status,created_by)
VALUES
('GT-SHP-1002',(SELECT id FROM vendors WHERE vendor_code='VND-0023'),'Meridian Logistics','TRK-SG-1002','Singapore','Sri Lanka',NOW()-INTERVAL 4 DAY,NOW()-INTERVAL 1 DAY,NULL,'DELAYED',(SELECT id FROM users WHERE username='admin'))
ON DUPLICATE KEY UPDATE vendor_id=VALUES(vendor_id), carrier_name=VALUES(carrier_name), tracking_reference=VALUES(tracking_reference), origin_country=VALUES(origin_country), destination_country=VALUES(destination_country), departure_date=VALUES(departure_date), expected_arrival_date=VALUES(expected_arrival_date), actual_arrival_date=VALUES(actual_arrival_date), status=VALUES(status), created_by=VALUES(created_by);

INSERT INTO shipments
(shipment_number,vendor_id,carrier_name,tracking_reference,origin_country,destination_country,departure_date,expected_arrival_date,actual_arrival_date,status,created_by)
VALUES
('GT-SHP-1003',(SELECT id FROM vendors WHERE vendor_code='VND-0018'),'Pacific Freight','TRK-JP-1003','Japan','Sri Lanka',NOW()-INTERVAL 2 DAY,NOW()+INTERVAL 1 DAY,NULL,'CUSTOMS_HOLD',(SELECT id FROM users WHERE username='admin'))
ON DUPLICATE KEY UPDATE vendor_id=VALUES(vendor_id), carrier_name=VALUES(carrier_name), tracking_reference=VALUES(tracking_reference), origin_country=VALUES(origin_country), destination_country=VALUES(destination_country), departure_date=VALUES(departure_date), expected_arrival_date=VALUES(expected_arrival_date), actual_arrival_date=VALUES(actual_arrival_date), status=VALUES(status), created_by=VALUES(created_by);

INSERT INTO shipments
(shipment_number,vendor_id,carrier_name,tracking_reference,origin_country,destination_country,departure_date,expected_arrival_date,actual_arrival_date,status,created_by)
VALUES
('GT-SHP-1004',(SELECT id FROM vendors WHERE vendor_code='VND-0023'),'Lanka Express','TRK-SG-1004','Singapore','Sri Lanka',NOW()-INTERVAL 7 DAY,NOW()-INTERVAL 4 DAY,NOW()-INTERVAL 3 DAY,'DELIVERED',(SELECT id FROM users WHERE username='admin'))
ON DUPLICATE KEY UPDATE vendor_id=VALUES(vendor_id), carrier_name=VALUES(carrier_name), tracking_reference=VALUES(tracking_reference), origin_country=VALUES(origin_country), destination_country=VALUES(destination_country), departure_date=VALUES(departure_date), expected_arrival_date=VALUES(expected_arrival_date), actual_arrival_date=VALUES(actual_arrival_date), status=VALUES(status), created_by=VALUES(created_by);

INSERT INTO shipment_items(shipment_id,inventory_item_id,quantity)
SELECT s.id,i.id,8 FROM shipments s JOIN inventory_items i ON i.sku='SNS-A17'
WHERE s.shipment_number='GT-SHP-1001'
  AND NOT EXISTS (SELECT 1 FROM shipment_items x WHERE x.shipment_id=s.id AND x.inventory_item_id=i.id);

INSERT INTO shipment_items(shipment_id,inventory_item_id,quantity)
SELECT s.id,i.id,5 FROM shipments s JOIN inventory_items i ON i.sku='CTL-X2'
WHERE s.shipment_number='GT-SHP-1002'
  AND NOT EXISTS (SELECT 1 FROM shipment_items x WHERE x.shipment_id=s.id AND x.inventory_item_id=i.id);

INSERT INTO shipment_items(shipment_id,inventory_item_id,quantity)
SELECT s.id,i.id,12 FROM shipments s JOIN inventory_items i ON i.sku='SNS-A17'
WHERE s.shipment_number='GT-SHP-1003'
  AND NOT EXISTS (SELECT 1 FROM shipment_items x WHERE x.shipment_id=s.id AND x.inventory_item_id=i.id);

INSERT INTO shipment_items(shipment_id,inventory_item_id,quantity)
SELECT s.id,i.id,6 FROM shipments s JOIN inventory_items i ON i.sku='CTL-X2'
WHERE s.shipment_number='GT-SHP-1004'
  AND NOT EXISTS (SELECT 1 FROM shipment_items x WHERE x.shipment_id=s.id AND x.inventory_item_id=i.id);

INSERT INTO customs_documents(shipment_id,document_type,document_number,status,due_date,notes)
SELECT s.id,'Import Declaration','CUS-GT-1001','PENDING',NOW()+INTERVAL 24 HOUR,'Awaiting customs review in Colombo.'
FROM shipments s WHERE s.shipment_number='GT-SHP-1003'
AND NOT EXISTS (SELECT 1 FROM customs_documents c WHERE c.document_number='CUS-GT-1001');

INSERT INTO customs_documents(shipment_id,document_type,document_number,status,due_date,submitted_at,notes)
SELECT s.id,'Commercial Invoice','CUS-GT-1002','SUBMITTED',NOW()+INTERVAL 72 HOUR,NOW()-INTERVAL 3 HOUR,'Submitted electronically for verification.'
FROM shipments s WHERE s.shipment_number='GT-SHP-1001'
AND NOT EXISTS (SELECT 1 FROM customs_documents c WHERE c.document_number='CUS-GT-1002');

INSERT INTO customs_documents(shipment_id,document_type,document_number,status,due_date,submitted_at,approved_at,notes)
SELECT s.id,'Certificate of Origin','CUS-GT-1003','APPROVED',NOW()-INTERVAL 5 DAY,NOW()-INTERVAL 6 DAY,NOW()-INTERVAL 5 DAY,'Approved sample compliance record.'
FROM shipments s WHERE s.shipment_number='GT-SHP-1004'
AND NOT EXISTS (SELECT 1 FROM customs_documents c WHERE c.document_number='CUS-GT-1003');

UPDATE customs_documents SET due_date=NOW()+INTERVAL 24 HOUR WHERE document_number='CUS-GT-1001' AND status<>'APPROVED';
UPDATE customs_documents SET due_date=NOW()+INTERVAL 72 HOUR WHERE document_number='CUS-GT-1002' AND status<>'APPROVED';

INSERT INTO vendor_performance(vendor_id,evaluation_date,delivery_score,quality_score,compliance_score,overall_score,remarks)
SELECT v.id,'2026-08-10',90.00,93.00,90.00,91.00,'Consistent delivery and quality performance.'
FROM vendors v WHERE v.vendor_code='VND-0018'
AND NOT EXISTS (SELECT 1 FROM vendor_performance p WHERE p.vendor_id=v.id AND p.evaluation_date='2026-08-10');

INSERT INTO vendor_performance(vendor_id,evaluation_date,delivery_score,quality_score,compliance_score,overall_score,remarks)
SELECT v.id,'2026-08-20',92.00,91.00,90.00,91.00,'Stable performance across the latest review.'
FROM vendors v WHERE v.vendor_code='VND-0018'
AND NOT EXISTS (SELECT 1 FROM vendor_performance p WHERE p.vendor_id=v.id AND p.evaluation_date='2026-08-20');

INSERT INTO vendor_performance(vendor_id,evaluation_date,delivery_score,quality_score,compliance_score,overall_score,remarks)
SELECT v.id,'2026-08-11',85.00,88.00,88.00,87.00,'Good supplier performance with minor delivery variance.'
FROM vendors v WHERE v.vendor_code='VND-0023'
AND NOT EXISTS (SELECT 1 FROM vendor_performance p WHERE p.vendor_id=v.id AND p.evaluation_date='2026-08-11');

INSERT INTO vendor_performance(vendor_id,evaluation_date,delivery_score,quality_score,compliance_score,overall_score,remarks)
SELECT v.id,'2026-08-21',88.00,86.00,87.00,87.00,'Recent assessment remains within the healthy range.'
FROM vendors v WHERE v.vendor_code='VND-0023'
AND NOT EXISTS (SELECT 1 FROM vendor_performance p WHERE p.vendor_id=v.id AND p.evaluation_date='2026-08-21');

INSERT INTO system_alerts(severity,category,title,message,reference_type,reference_id,resolved,created_at)
SELECT 'HIGH','SHIPMENT','Shipment delay detected','GT-SHP-1002 passed its expected arrival time.','Shipment',s.id,FALSE,NOW()-INTERVAL 35 MINUTE
FROM shipments s WHERE s.shipment_number='GT-SHP-1002'
AND NOT EXISTS (SELECT 1 FROM system_alerts a WHERE a.category='SHIPMENT' AND a.title='Shipment delay detected' AND a.reference_type='Shipment' AND a.reference_id=s.id AND a.resolved=FALSE);

INSERT INTO system_alerts(severity,category,title,message,reference_type,reference_id,resolved,created_at)
SELECT 'HIGH','CUSTOMS','Customs deadline approaching','Import Declaration for GT-SHP-1003 is due within 24 hours.','CustomsDocument',c.id,FALSE,NOW()-INTERVAL 15 MINUTE
FROM customs_documents c WHERE c.document_number='CUS-GT-1001'
AND NOT EXISTS (SELECT 1 FROM system_alerts a WHERE a.category='CUSTOMS' AND a.title='Customs deadline approaching' AND a.reference_type='CustomsDocument' AND a.reference_id=c.id AND a.resolved=FALSE);

INSERT INTO system_alerts(severity,category,title,message,reference_type,reference_id,resolved,created_at)
SELECT 'INFO','ROUTE','Route optimization cycle completed','Scheduled route optimization assessment completed.','RouteOptimization',NULL,FALSE,NOW()-INTERVAL 10 MINUTE
WHERE NOT EXISTS (SELECT 1 FROM system_alerts a WHERE a.category='ROUTE' AND a.title='Route optimization cycle completed' AND a.resolved=FALSE);

INSERT INTO system_alerts(severity,category,title,message,reference_type,reference_id,resolved,created_at,resolved_at)
SELECT 'MEDIUM','INVENTORY','Inventory level restored','Sample historical alert resolved after replenishment.','InventoryItem',i.id,TRUE,NOW()-INTERVAL 1 DAY,NOW()-INTERVAL 20 HOUR
FROM inventory_items i WHERE i.sku='CTL-X2'
AND NOT EXISTS (SELECT 1 FROM system_alerts a WHERE a.category='INVENTORY' AND a.title='Inventory level restored' AND a.reference_type='InventoryItem' AND a.reference_id=i.id);
