<?php
require_once 'database.php';
require_once __DIR__ . '/auth.php';

// Proteger la ruta con autenticación y API Key
proteger();

header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD'] === 'PUT') {
    $data = json_decode(file_get_contents('php://input'), true);

    if (!isset($data['id'], $data['nombre'], $data['precio'], $data['categoria'], $data['imagen'])) {
        http_response_code(400);
        echo json_encode(['success' => false, 'error' => 'Faltan campos requeridos']);
        exit;
    }

    try {
        $conn = conexion();
        $stmt = $conn->prepare("UPDATE productos SET nombre = ?, precio = ?, categoria = ?, imagen = ? WHERE id = ?");
        $stmt->execute([$data['nombre'], $data['precio'], $data['categoria'], $data['imagen'], $data['id']]);

        echo json_encode(['success' => true]);
    } catch (PDOException $e) {
        http_response_code(500);
        echo json_encode(['success' => false, 'error' => $e->getMessage()]);
    }
} else {
    http_response_code(405);
    echo json_encode(['success' => false, 'error' => 'Método no permitido']);
}
