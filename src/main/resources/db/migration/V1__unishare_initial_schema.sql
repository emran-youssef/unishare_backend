-- ── 1. USERS ────────────────────────────────────────────────
CREATE TABLE users (
    id                 BIGINT          NOT NULL AUTO_INCREMENT,
    full_name          VARCHAR(100)    NOT NULL,
    email              VARCHAR(150)    NOT NULL,
    university_email   VARCHAR(150)    NOT NULL,
    password_hash      VARCHAR(255)    NOT NULL,
    role               ENUM('STUDENT','ADMIN') NOT NULL,
    profile_picture    VARCHAR(500)    NULL,
    phone              VARCHAR(20)     NULL,
    created_at         DATETIME        NOT NULL,
    updated_at         DATETIME        NOT NULL,

    CONSTRAINT pk_users          PRIMARY KEY (id),
    CONSTRAINT uq_users_email    UNIQUE (email),
    CONSTRAINT uq_users_uni_email UNIQUE (university_email)
);

-- ── 2. MEETUP_LOCATIONS ─────────────────────────────────────
-- Created before bookings because bookings has a FK to this table
CREATE TABLE meetup_locations (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    name        VARCHAR(200)    NOT NULL,
    description VARCHAR(500)    NULL,
    address     VARCHAR(300)    NULL,
    is_active   BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at  DATETIME        NOT NULL,
    updated_at  DATETIME        NOT NULL,

    CONSTRAINT pk_meetup_locations PRIMARY KEY (id)
);

-- ── 3. LISTINGS ─────────────────────────────────────────────
CREATE TABLE listings (
    id            BIGINT          NOT NULL AUTO_INCREMENT,
    owner_id      BIGINT          NOT NULL,
    title         VARCHAR(200)    NOT NULL,
    description   TEXT            NULL,
    price_per_day DECIMAL(10,2)   NOT NULL,
    category      ENUM('TEXTBOOKS','ELECTRONICS','FURNITURE','CLOTHING','OTHER') NOT NULL,
    `condition`   ENUM('NEW','LIKE_NEW','GOOD','FAIR') NOT NULL,
    status        ENUM('AVAILABLE','RENTED','INACTIVE') NOT NULL DEFAULT 'AVAILABLE',
    created_at    DATETIME        NOT NULL,
    updated_at    DATETIME        NOT NULL,

    CONSTRAINT pk_listings        PRIMARY KEY (id),
    CONSTRAINT fk_listings_owner  FOREIGN KEY (owner_id) REFERENCES users(id)
);

-- ── 4. LISTING_IMAGES ───────────────────────────────────────
CREATE TABLE listing_images (
    id            BIGINT          NOT NULL AUTO_INCREMENT,
    listing_id    BIGINT          NOT NULL,
    image_url     VARCHAR(500)    NOT NULL,
    display_order INT             NOT NULL DEFAULT 0,

    CONSTRAINT pk_listing_images         PRIMARY KEY (id),
    CONSTRAINT fk_listing_images_listing FOREIGN KEY (listing_id) REFERENCES listings(id)
);

-- ── 5. BOOKINGS ─────────────────────────────────────────────
CREATE TABLE bookings (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    listing_id          BIGINT          NOT NULL,
    renter_id           BIGINT          NOT NULL,
    start_date          DATE            NOT NULL,
    end_date            DATE            NOT NULL,
    total_price         DECIMAL(10,2)   NOT NULL,
    status              ENUM('PENDING','CONFIRMED','CANCELLED','COMPLETED') NOT NULL DEFAULT 'PENDING',
    meetup_location_id  BIGINT          NULL,
    created_at          DATETIME        NOT NULL,
    updated_at          DATETIME        NOT NULL,

    CONSTRAINT pk_bookings                  PRIMARY KEY (id),
    CONSTRAINT fk_bookings_listing          FOREIGN KEY (listing_id)         REFERENCES listings(id),
    CONSTRAINT fk_bookings_renter           FOREIGN KEY (renter_id)          REFERENCES users(id),
    CONSTRAINT fk_bookings_meetup_location  FOREIGN KEY (meetup_location_id) REFERENCES meetup_locations(id)
);


-- Critical index: drives the overlap detection query
-- BookingRepository checks: listing_id + date range + CONFIRMED status
CREATE INDEX idx_bookings_overlap
    ON bookings (listing_id, start_date, end_date, status);


-- ── 6. PAYMENTS ─────────────────────────────────────────────
CREATE TABLE payments (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    booking_id      BIGINT          NOT NULL,
    amount          DECIMAL(10,2)   NOT NULL,
    status          ENUM('PENDING','PAID','REFUNDED') NOT NULL DEFAULT 'PENDING',
    payment_method  VARCHAR(50)     NULL,
    transaction_ref VARCHAR(200)    NULL,
    paid_at         DATETIME        NULL,
    created_at      DATETIME        NOT NULL,
    updated_at      DATETIME        NOT NULL,

    CONSTRAINT pk_payments             PRIMARY KEY (id),
    CONSTRAINT uq_payments_booking     UNIQUE (booking_id),
    CONSTRAINT fk_payments_booking     FOREIGN KEY (booking_id) REFERENCES bookings(id)
);

-- ── 7. REVIEWS ──────────────────────────────────────────────
CREATE TABLE reviews (
    id           BIGINT      NOT NULL AUTO_INCREMENT,
    booking_id   BIGINT      NOT NULL,
    reviewer_id  BIGINT      NOT NULL,
    reviewee_id  BIGINT      NOT NULL,
    listing_id   BIGINT      NOT NULL,
    rating       TINYINT     NOT NULL,
    comment      TEXT        NULL,
    type         ENUM('OWNER_TO_RENTER','RENTER_TO_OWNER') ,
    created_at   DATETIME    NOT NULL,
    updated_at   DATETIME    NOT NULL,

    CONSTRAINT pk_reviews             PRIMARY KEY (id),
    CONSTRAINT fk_reviews_booking     FOREIGN KEY (booking_id)  REFERENCES bookings(id),
    CONSTRAINT fk_reviews_reviewer    FOREIGN KEY (reviewer_id) REFERENCES users(id),
    CONSTRAINT fk_reviews_reviewee    FOREIGN KEY (reviewee_id) REFERENCES users(id),
    CONSTRAINT fk_reviews_listing     FOREIGN KEY (listing_id)  REFERENCES listings(id),
    CONSTRAINT chk_reviews_rating     CHECK (rating BETWEEN 1 AND 5)
);

-- ── 8. CHAT_MESSAGES ────────────────────────────────────────
CREATE TABLE chat_messages (
    id           BIGINT      NOT NULL AUTO_INCREMENT,
    sender_id    BIGINT      NOT NULL,
    receiver_id  BIGINT      NOT NULL,
    listing_id   BIGINT      NOT NULL,
    content      TEXT        NOT NULL,
    is_read      BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at   DATETIME    NOT NULL,

    CONSTRAINT pk_chat_messages           PRIMARY KEY (id),
    CONSTRAINT fk_chat_messages_sender    FOREIGN KEY (sender_id)   REFERENCES users(id),
    CONSTRAINT fk_chat_messages_receiver  FOREIGN KEY (receiver_id) REFERENCES users(id),
    CONSTRAINT fk_chat_messages_listing   FOREIGN KEY (listing_id)  REFERENCES listings(id)
);

-- ── 9. SEED DATA — Meetup Locations ─────────────────────────
INSERT INTO meetup_locations (name, description, address, is_active, created_at, updated_at) VALUES
('Main Library Entrance',   'Front steps of the main campus library. Busy and well-lit.',          'Main Library, University Ave',       TRUE, NOW(), NOW()),
('Student Union Lobby',     'Ground floor lobby near the information desk.',                        'Student Union Building, Room 101',   TRUE, NOW(), NOW()),
('Science Building Atrium', 'Open atrium on the ground floor, between the elevators.',              '100 Science Drive, Ground Floor',    TRUE, NOW(), NOW()),
('Campus Bookstore Front',  'Outside the main entrance of the campus bookstore.',                   'University Bookstore, College Blvd', TRUE, NOW(), NOW()),
('Engineering Hall Lobby',  'Main lobby of the engineering building, near the reception desk.',     '200 Engineering Way, Lobby',         TRUE, NOW(), NOW());

