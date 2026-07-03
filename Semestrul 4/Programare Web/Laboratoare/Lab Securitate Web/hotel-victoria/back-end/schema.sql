CREATE TABLE IF NOT EXISTS `users` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(100) NOT NULL UNIQUE,
  `email` VARCHAR(255) NOT NULL UNIQUE,
  `password` VARCHAR(255) NOT NULL,
  `full_name` VARCHAR(255),
  `role` ENUM('client', 'admin') DEFAULT 'client',
  `remember_selector` VARCHAR(64) NULL,
  `remember_token_hash` VARCHAR(255) NULL,
  `remember_expires_at` DATETIME NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `rooms` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `room_number` VARCHAR(64) DEFAULT NULL,
  `room_type` VARCHAR(64) NOT NULL,
  `capacity` INT NOT NULL DEFAULT 1,
  `price_per_night` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `description` TEXT DEFAULT NULL,
  `status` VARCHAR(32) NOT NULL DEFAULT 'available',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `reservations` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` INT UNSIGNED DEFAULT NULL,
  `room_id` INT UNSIGNED NOT NULL,
  `check_in` DATE NOT NULL,
  `check_out` DATE NOT NULL,
  `guests` INT NOT NULL DEFAULT 1,
  `special_requests` TEXT DEFAULT NULL,
  `status` VARCHAR(32) NOT NULL DEFAULT 'În așteptare',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_room_id` (`room_id`),
  INDEX `idx_user_id` (`user_id`),
  CONSTRAINT `fk_reservations_room` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_reservations_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `contact_messages` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` INT UNSIGNED NOT NULL,
  `full_name` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `subject` VARCHAR(255) NOT NULL,
  `message` TEXT NOT NULL,
  `attachment_image` LONGBLOB,
  `attachment_filename` VARCHAR(255),
  `attachment_mime_type` VARCHAR(100),
  `status` VARCHAR(32) DEFAULT 'new',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_status` (`status`),
  INDEX `idx_created_at` (`created_at`),
  CONSTRAINT `fk_contact_messages_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Sample room data
INSERT INTO `rooms` (`room_number`, `room_type`, `capacity`, `price_per_night`, `description`, `status`, `created_at`) VALUES
('101', 'twin', 2, 320.00, 'Cameră Standard Twin cu două paturi separate, Wi-Fi gratuit, TV, aer condiționat, minibar', 'available', NOW()),
('102', 'twin', 2, 320.00, 'Cameră Standard Twin cu două paturi separate, Wi-Fi gratuit, TV, aer condiționat, minibar', 'available', NOW()),
('103', 'twin', 2, 320.00, 'Cameră Standard Twin cu două paturi separate, Wi-Fi gratuit, TV, aer condiționat, minibar', 'available', NOW()),
('104', 'twin', 2, 320.00, 'Cameră Standard Twin cu două paturi separate, Wi-Fi gratuit, TV, aer condiționat, minibar', 'available', NOW()),
('201', 'double', 2, 300.00, 'Cameră Standard Double cu pat dublu, Wi-Fi gratuit, TV, aer condiționat, minibar', 'available', NOW()),
('202', 'double', 2, 300.00, 'Cameră Standard Double cu pat dublu, Wi-Fi gratuit, TV, aer condiționat, minibar', 'available', NOW()),
('203', 'double', 2, 300.00, 'Cameră Standard Double cu pat dublu, Wi-Fi gratuit, TV, aer condiționat, minibar', 'available', NOW()),
('204', 'double', 2, 300.00, 'Cameră Standard Double cu pat dublu, Wi-Fi gratuit, TV, aer condiționat, minibar', 'available', NOW()),
('301', 'apartament', 4, 450.00, 'Apartament spațios pentru 4 persoane, seif, TV, aer condiționat, minibar, frigider, Wi-Fi gratuit', 'available', NOW()),
('302', 'apartament', 4, 450.00, 'Apartament spațios pentru 4 persoane, seif, TV, aer condiționat, minibar, frigider, Wi-Fi gratuit', 'available', NOW()),
('303', 'apartament', 4, 450.00, 'Apartament spațios pentru 4 persoane, seif, TV, aer condiționat, minibar, frigider, Wi-Fi gratuit', 'available', NOW()),
('401', 'deluxe', 4, 650.00, 'Apartament Deluxe premium cu balcon, jacuzzi, pat dublu, seif, TV, aer condiționat, minibar, frigider, mic dejun inclus, Wi-Fi gratuit', 'available', NOW()),
('402', 'deluxe', 4, 650.00, 'Apartament Deluxe premium cu balcon, jacuzzi, pat dublu, seif, TV, aer condiționat, minibar, frigider, mic dejun inclus, Wi-Fi gratuit', 'available', NOW());
