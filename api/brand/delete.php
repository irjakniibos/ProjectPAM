<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: DELETE");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// Handle preflight
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

// Hanya terima DELETE
if ($_SERVER['REQUEST_METHOD'] !== 'DELETE') {
    http_response_code(405);
    echo json_encode(array("success" => false, "message" => "Method not allowed"));
    exit();
}

// DIPERBAIKI: Path ke config/database.php
require_once '../../config/database.php';

// Get ID from query parameter
$id = isset($_GET['id']) ? $_GET['id'] : null;

// Validasi input
if (empty($id)) {
    http_response_code(400);
    echo json_encode(array(
        "success" => false,
        "message" => "ID brand tidak boleh kosong"
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
    $stmt->bindParam(":id", $id);
    $stmt->execute();

    if ($stmt->rowCount() === 0) {
        http_response_code(404);
        echo json_encode(array(
            "success" => false,
            "message" => "Brand tidak ditemukan"
        ));
        exit();
    }

    // Cek apakah ada motor dengan brand ini
    $query = "SELECT COUNT(*) as total FROM motor WHERE brand_id = :brand_id";
    $stmt = $db->prepare($query);
    $stmt->bindParam(":brand_id", $id);
    $stmt->execute();
    $result = $stmt->fetch(PDO::FETCH_ASSOC);

    if ($result['total'] > 0) {
        http_response_code(400);
        echo json_encode(array(
            "success" => false,
            "message" => "Tidak dapat menghapus brand karena masih ada " . $result['total'] . " motor dengan brand ini"
        ));
        exit();
    }

    // Delete brand
    $query = "DELETE FROM brand WHERE id = :id";
    $stmt = $db->prepare($query);
    $stmt->bindParam(":id", $id);

    if ($stmt->execute()) {
        http_response_code(200);
        echo json_encode(array(
            "success" => true,
            "message" => "Brand berhasil dihapus"
        ));
    } else {
        http_response_code(500);
        echo json_encode(array(
            "success" => false,
            "message" => "Gagal menghapus brand"
        ));
    }
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(array(
        "success" => false,
        "message" => "Database error: " . $e->getMessage()
    ));
}