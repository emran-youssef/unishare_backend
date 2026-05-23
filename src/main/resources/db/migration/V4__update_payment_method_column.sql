ALTER TABLE payments
MODIFY COLUMN payment_method ENUM('CASH', 'ONLINE') NOT NULL;

ALTER TABLE payments
DROP COLUMN transaction_ref;