<?php
ini_set('display_errors', 1);
error_reporting(E_ALL);
header('Content-Type: application/json');

require_once 'database.php';

$data = json_decode(file_get_contents('php://input'), true);

if (!$data || empty($data['correo']) || empty($data['contrasena'])) {
    echo json_encode(['success' => false, 'message' => 'Faltan datos']);
    exit;
}

// Aquí va la consulta a la base de datos para verificar usuario
$db = conexion();
$stmt = $db->prepare('SELECT id, nombre, contrasena FROM usuarios WHERE correo = ? LIMIT 1');
$stmt->execute([$data['correo']]);
$user = $stmt->fetch(PDO::FETCH_ASSOC);

if (!$user || !password_verify($data['contrasena'], $user['contrasena'])) {
    echo json_encode(['success' => false, 'message' => 'Credenciales inválidas']);
    exit;
}

// Generar o recuperar API key
$stmtKey = $db->prepare('SELECT clave FROM api_keys WHERE id = ? AND activo = 1 LIMIT 1');
$stmtKey->execute([$user['id']]);
$apiRow = $stmtKey->fetch(PDO::FETCH_ASSOC);

if ($apiRow) {
    $apiKey = $apiRow['clave'];
} else {
    $apiKey = bin2hex(random_bytes(16));
    $insert = $db->prepare('INSERT INTO api_keys (id, clave, activo) VALUES (?, ?, 1)');
    $insert->execute([$user['id'], $apiKey]);
}

echo json_encode([
    'success' => true,
    'apiKey' => $apiKey,
    'nombre' => $user['nombre'],
    'id' => $user['id']
]);
