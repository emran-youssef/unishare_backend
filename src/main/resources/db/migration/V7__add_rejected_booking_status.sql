-- Add REJECTED as a valid booking status so owners can reject a PENDING
-- booking request distinctly from a CANCELLED booking.
ALTER TABLE bookings
    MODIFY status ENUM('PENDING','CONFIRMED','CANCELLED','COMPLETED','REJECTED') NOT NULL DEFAULT 'PENDING';
