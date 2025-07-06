<?php
require_once 'database.php';
require_once __DIR__ . '/auth.php';

proteger();

header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD'] === 'POST') {

    $data = json_decode(file_get_contents('php://input'), true);


    if (!isset($data['nombre'], $data['precio'], $data['categoria'], $data['imagen'])) {
        http_response_code(400);
        echo json_encode(['success' => false, 'error' => 'Faltan campos requeridos']);
        exit;
    }

    if (!is_string($data['nombre']) || !is_numeric($data['precio']) || !is_string($data['categoria']) || !is_string($data['imagen'])) {
        http_response_code(400);
        echo json_encode(['success' => false, 'error' => 'Tipo de dato incorrecto en uno o más campos']);
        exit;
    }

    try {
        $conn = conexion();

        $stmt = $conn->prepare("INSERT INTO productos (nombre, precio, categoria, imagen) VALUES (?, ?, ?, ?)");
        $stmt->execute([
            $data['nombre'],
            $data['precio'],
            $data['categoria'],
            $data['imagen']
        ]);

        echo json_encode(['success' => true, 'id' => $conn->lastInsertId()]);
    } catch (PDOException $e) {
        http_response_code(500);
        echo json_encode(['success' => false, 'error' => 'Error del servidor: ' . $e->getMessage()]);
    }
} else {
    http_response_code(405);
    echo json_encode(['success' => false, 'error' => 'Método no permitido']);
}
