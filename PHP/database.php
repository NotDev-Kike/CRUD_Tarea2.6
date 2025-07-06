<?php
define('DB_HOST', 'localhost');
define('DB_USER', 'root');
define('DB_PASS', '1234');
define('DB_NAME', 'productos_api');

/**
 * Establece y devuelve una conexión PDO a la base de datos MySQL.
 *
 * @return PDO
 */
function conexion() {
    try {
        $pdo = new PDO(
            'mysql:host=' . DB_HOST . ';dbname=' . DB_NAME . ';charset=utf8',
            DB_USER,
            DB_PASS
        );

        $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        return $pdo;
    } catch (PDOException $e) {
        header('Content-Type: application/json');
        http_response_code(500);
        echo json_encode([
            'error' => 'Error de conexión a la base de datos',
            'message' => $e->getMessage()
        ]);
        exit;
    }
}
