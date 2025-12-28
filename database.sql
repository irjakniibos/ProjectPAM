CREATE DATABASE showroom_motor;
USE showroom_motor;

-- =====================================================
-- TABEL user - Data admin showroom
-- =====================================================
CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama_lengkap VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- TABEL token - JWT authentication tokens
-- =====================================================
CREATE TABLE token (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    token TEXT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- TABEL brand - Daftar merk motor
-- =====================================================
CREATE TABLE brand (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama_brand VARCHAR(50) NOT NULL UNIQUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- TABEL motor - Data motor showroom
-- =====================================================
CREATE TABLE motor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama_motor VARCHAR(100) NOT NULL,
    brand_id INT NOT NULL,
    tipe VARCHAR(50) NOT NULL,
    tahun INT NOT NULL,
    harga DECIMAL(15,2) NOT NULL,
    warna VARCHAR(30) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (brand_id) REFERENCES brand(id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY unique_motor (nama_motor, brand_id, warna, tahun)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- TABEL stok - Stok motor
-- =====================================================
CREATE TABLE stok (
    id INT AUTO_INCREMENT PRIMARY KEY,
    motor_id INT NOT NULL UNIQUE,
    jumlah_stok INT NOT NULL DEFAULT 0,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (motor_id) REFERENCES motor(id) ON DELETE CASCADE,
    CHECK (jumlah_stok >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- DATA AWAL - Insert admin user
-- =====================================================
INSERT INTO user (nama_lengkap, email, password) VALUES 
('irzayaumilsyahrar', 'mainadmin@gmail.com', '$2y$10$R.P8BpRgjAf.pEqt97bp.OneLIowsEvRlg5RSfCEEpnWjBneV7776');