<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: PUT");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// Handle preflight
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

// Hanya terima PUT
if ($_SERVER['REQUEST_METHOD'] !== 'PUT') {
    http_response_code(405);
    echo json_encode(array("success" => false, "message" => "Method not allowed"));
    exit();
}

// DIPERBAIKI: Path ke config/database.php
require_once '../../config/database.php';

// Get posted data
$data = json_decode(file_get_contents("php://input"));

// Validasi input
if (empty($data->id) || empty($data->nama_brand)) {
    http_response_code(400);
    echo json_encode(array(
        "success" => false,
        "message" => "ID dan nama brand tidak boleh kosong"
    ));
    exit();
}

// Database connection
$database = new Database();
$db = $database->getConnection();

try {
    // Cek apakah brand exists
    $query = "SELECT id FROM brand WHERE id = :id LIMIT 1";
    $stmt = $db->prepare($query);
    $stmt->bindParam(":id", $data->id);
    $stmt->execute();

    if ($stmt->rowCount() === 0) {
        http_response_code(404);
        echo json_encode(array(
            "success" => false,
            "message" => "Brand tidak ditemukan"
        ));
        exit();
    }

    // Cek duplikasi nama (kecuali untuk brand yang sedang diedit)
    $query = "SELECT id FROM brand WHERE LOWER(nama_brand) = LOWER(:nama_brand) AND id != :id LIMIT 1";
    $stmt = $db->prepare($query);
    $stmt->bindParam(":nama_brand", $data->nama_brand);
    $stmt->bindParam(":id", $data->id);
    $stmt->execute();

    if ($stmt->rowCount() > 0) {
        http_response_code(400);
        echo json_encode(array(
            "success" => false,
            "message" => "Brand dengan nama tersebut sudah ada!"
        ));
        exit();
    }

    // Update brand
    $query = "UPDATE brand SET nama_brand = :nama_brand WHERE id = :id";
    $stmt = $db->prepare($query);
    $stmt->bindParam(":nama_brand", $data->nama_brand);
    $stmt->bindParam(":id", $data->id);

    if ($stmt->execute()) {
        // Ambil data lengkap termasuk created_at dari database
        $query = "SELECT id, nama_brand, created_at FROM brand WHERE id = :id";
        $stmt = $db->prepare($query);
        $stmt->bindParam(":id", $data->id);
        $stmt->execute();
        
        $brand_data = $stmt->fetch(PDO::FETCH_ASSOC);
        
        http_response_code(200);
        echo json_encode(array(
            "success" => true,
            "message" => "Brand berhasil diupdate",
            "data" => array(
                "id" => (int)$brand_data['id'],
                "nama_brand" => $brand_data['nama_brand'],
                "created_at" => $brand_data['created_at']
            )
        ));
    } else {
        http_response_code(500);
        echo json_encode(array(
            "success" => false,
            "message" => "Gagal mengupdate brand"
        ));
    }
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(array(
        "success" => false,
        "message" => "Database error: " . $e->getMessage()
    ));
}