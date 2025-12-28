<?php
/**
 * JWT Helper
 * Utility untuk generate dan validate JWT tokens
 */

require_once __DIR__ . '/../vendor/autoload.php';
use \Firebase\JWT\JWT;
use \Firebase\JWT\Key;

class JWTHelper {
    // Secret key untuk encode/decode JWT (GANTI dengan random string di production!)
    private static $secret_key = "showroom_motor_secret_key_2025_irza_yaumil_syahrar";
    private static $algorithm = "HS256";

    /**
     * Generate JWT token
     * @param int $user_id
     * @param string $email
     * @return string JWT token
     */
    public static function generateToken($user_id, $email) {
        $issued_at = time();
        $expiration_time = $issued_at + (60 * 60 * 24 * 365); // 1 tahun (sesuai SRS: tidak expired otomatis)
        
        $payload = array(
            "iat" => $issued_at,
            "exp" => $expiration_time,
            "user_id" => $user_id,
            "email" => $email
        );

        return JWT::encode($payload, self::$secret_key, self::$algorithm);
    }

    /**
     * Validate dan decode JWT token
     * @param string $token
     * @return object|null Decoded token data atau null jika invalid
     */
    public static function validateToken($token) {
        try {
            $decoded = JWT::decode($token, new Key(self::$secret_key, self::$algorithm));
            return $decoded;
        } catch (Exception $e) {
            return null;
        }
    }

    /**
     * Get user ID from token
     * @param string $token
     * @return int|null User ID atau null jika invalid
     */
    public static function getUserIdFromToken($token) {
        $decoded = self::validateToken($token);
        return $decoded ? $decoded->user_id : null;
    }
}
?>