<?php

require_once 'database.php';

header('Content-Type: application/json');

$data = json_decode(file_get_contents("php://input"), true);

if (!isset($data['nombre'], $data['correo'], $data['contrasena'])) {
    http_response_code(400);
    echo json_encode(["success" => false, "error" => "Faltan datos"]);
    exit;
}

$nombre = $data['nombre'];
$correo = $data['correo'];
$contrasena = password_hash($data['contrasena'], PASSWORD_DEFAULT);

try {
    $conn = conexion();
    $stmt = $conn->prepare("INSERT INTO usuarios (nombre, correo, contrasena) VALUES (?, ?, ?)");
    $stmt->execute([$nombre, $correo, $contrasena]);

    echo json_encode(["success" => true]);
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(["success" => false, "error" => $e->getMessage()]);
}
?>
