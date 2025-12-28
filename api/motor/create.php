<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// Handle preflight
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

include_once '../../config/database.php';
include_once '../../utils/auth_middleware.php';

// Validate authentication
validateAuth();

// Hanya terima POST request
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode(array("success" => false, "message" => "Method not allowed"));
    exit();
}

// Get posted data
$data = json_decode(file_get_contents("php://input"));

// Validasi input
if (empty($data->nama_motor) || empty($data->brand_id) || empty($data->tipe) || 
    empty($data->tahun) || empty($data->harga) || empty($data->warna)) {
    http_response_code(400);
    echo json_encode(array(
        "success" => false,
        "message" => "Semua field wajib diisi"
    ));
    exit();
}

// Validasi tahun
if ($data->tahun < 2000) {
    http_response_code(400);
    echo json_encode(array(
        "success" => false,
        "message" => "Tahun minimal 2000"
    ));
    exit();
}

// Validasi harga
if ($data->harga <= 0) {
    http_response_code(400);
    echo json_encode(array(
        "success" => false,
        "message" => "Harga harus lebih dari 0"
    ));
    exit();
}

// Validasi stok awal
$stok_awal = isset($data->stok_awal) ? $data->stok_awal : 0;
if ($stok_awal < 0) {
    http_response_code(400);
    echo json_encode(array(
        "success" => false,
        "message" => "Stok awal tidak boleh negatif"
    ));
    exit();
}

// Database connection
$database = new Database();
$db = $database->getConnection();

try {
    // Cek duplikasi (nama_motor + brand_id + warna + tahun)
    $query = "SELECT id FROM motor 
              WHERE nama_motor = :nama_motor 
              AND brand_id = :brand_id 
              AND warna = :warna 
              AND tahun = :tahun 
              LIMIT 1";
    $stmt = $db->prepare($query);
    $stmt->bindParam(":nama_motor", $data->nama_motor);
    $stmt->bindParam(":brand_id", $data->brand_id);
    $stmt->bindParam(":warna", $data->warna);
    $stmt->bindParam(":tahun", $data->tahun);
    $stmt->execute();
    
    if ($stmt->rowCount() > 0) {
        http_response_code(400);
        echo json_encode(array(
            "success" => false,
            "message" => "Motor sudah ada!"
        ));
        exit();
    }
    
    // Insert motor baru
    $query = "INSERT INTO motor (nama_motor, brand_id, tipe, tahun, harga, warna) 
              VALUES (:nama_motor, :brand_id, :tipe, :tahun, :harga, :warna)";
    $stmt = $db->prepare($query);
    $stmt->bindParam(":nama_motor", $data->nama_motor);
    $stmt->bindParam(":brand_id", $data->brand_id);
    $stmt->bindParam(":tipe", $data->tipe);
    $stmt->bindParam(":tahun", $data->tahun);
    $stmt->bindParam(":harga", $data->harga);
    $stmt->bindParam(":warna", $data->warna);
    
    if ($stmt->execute()) {
        $motor_id = $db->lastInsertId();
        
        // Insert stok awal
        $query = "INSERT INTO stok (motor_id, jumlah_stok) VALUES (:motor_id, :jumlah_stok)";
        $stmt = $db->prepare($query);
        $stmt->bindParam(":motor_id", $motor_id);
        $stmt->bindParam(":jumlah_stok", $stok_awal);
        $stmt->execute();
        
        // DIPERBAIKI: Ambil data lengkap dari database termasuk created_at dan jumlah_stok
        $query = "SELECT m.id, m.nama_motor, m.brand_id, b.nama_brand, m.tipe, m.tahun, 
                         m.harga, m.warna, s.jumlah_stok, m.created_at
                  FROM motor m
                  INNER JOIN brand b ON m.brand_id = b.id
                  LEFT JOIN stok s ON m.id = s.motor_id
                  WHERE m.id = :id";
        $stmt = $db->prepare($query);
        $stmt->bindParam(":id", $motor_id);
        $stmt->execute();
        $motor_data = $stmt->fetch(PDO::FETCH_ASSOC);
        
        http_response_code(201);
        echo json_encode(array(
            "success" => true,
            "message" => "Motor berhasil ditambahkan",
            "data" => array(
                "id" => (int)$motor_data['id'],
                "nama_motor" => $motor_data['nama_motor'],
                "brand_id" => (int)$motor_data['brand_id'],
                "nama_brand" => $motor_data['nama_brand'],
                "tipe" => $motor_data['tipe'],
                "tahun" => (int)$motor_data['tahun'],
                "harga" => (double)$motor_data['harga'],
                "warna" => $motor_data['warna'],
                "jumlah_stok" => (int)$motor_data['jumlah_stok'],  // ✅ DITAMBAHKAN
                "created_at" => $motor_data['created_at']  // ✅ DITAMBAHKAN
            )
        ));
    } else {
        http_response_code(500);
        echo json_encode(array(
            "success" => false,
            "message" => "Gagal menambahkan motor"
        ));
    }
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(array(
        "success" => false,
        "message" => "Database error: " . $e->getMessage()
    ));
}