<?php
require_once 'database.php';
require_once __DIR__ . '/auth.php';

// Proteger la ruta con autenticación y API Key
proteger();

header('Content-Type: application/json');

try {
    $conn = conexion();

    // Consulta para obtener los productos
    $stmt = $conn->prepare("SELECT id, nombre, precio, categoria, imagenProducto FROM productos");
    $stmt->execute();
    $productos = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode([
        'success' => true,
        'data' => $productos
    ]);
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'error' => 'Error en la base de datos: ' . $e->getMessage()
    ]);
}
