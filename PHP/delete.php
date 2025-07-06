<?php
require_once 'database.php';
require_once __DIR__ . '/auth.php';

// Proteger la ruta con autenticación y API Key
proteger();

header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD'] === 'DELETE') {
    $data = json_decode(file_get_contents('php://input'), true);

    if (!isset($data['id'])) {
        http_response_code(400);
        echo json_encode(['success' => false, 'error' => 'Falta el ID']);
        exit;
    }

    try {
        $conn = conexion();
        $stmt = $conn->prepare("DELETE FROM productos WHERE id = ?");
        $stmt->execute([$data['id']]);

        echo json_encode(['success' => true]);
    } catch (PDOException $e) {
        http_response_code(500);
        echo json_encode(['success' => false, 'error' => $e->getMessage()]);
    }
} else {
    http_response_code(405);
    echo json_encode(['success' => false, 'error' => 'Método no permitido']);
}
