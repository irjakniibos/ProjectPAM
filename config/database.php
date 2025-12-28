<?php
// PENTING: Tidak boleh ada spasi atau karakter apapun sebelum <?php

class Database {
    private $host = "localhost";
    private $db_name = "showroom_motor";
    private $username = "root";
    private $password = "";
    public $conn;

    public function getConnection() {
        $this->conn = null;

        try {
            $this->conn = new PDO(
                "mysql:host=" . $this->host . ";dbname=" . $this->db_name . ";charset=utf8mb4",
                $this->username,
                $this->password
            );
            $this->conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            // DIHAPUS: $this->conn->exec("set names utf8mb4"); // Sudah di charset DSN
        } catch(PDOException $exception) {
            // JANGAN echo di sini, karena akan merusak JSON response
            // Lempar exception saja
            throw $exception;
        }

        return $this->conn;
    }
}