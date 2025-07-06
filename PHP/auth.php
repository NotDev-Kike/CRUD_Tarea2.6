<?php
require_once __DIR__ . '/database.php';

define('REALM', 'API CRUD');

$usuarios_fallback = [
    'admin' => md5('admin:' . REALM . ':1234'),
    'user'  => md5('user:' . REALM . ':test')
];

function sendJsonError(int $code, string $msg): void {
    http_response_code($code);
    echo json_encode(['success' => false, 'error' => $msg]);
    exit;
}

function getHeaderInsensitive(array $headers, string $key) {
    foreach ($headers as $k => $v) {
        if (strcasecmp($k, $key) === 0) return $v;
    }
    return null;
}

function verificarApiKey(): void {
    $headers = function_exists('apache_request_headers') ? apache_request_headers() : getallheaders();
    $apiKey = getHeaderInsensitive($headers, 'X-API-KEY');

    if (empty($apiKey)) {
        sendJsonError(401, 'Falta API Key');
    }

    $db = conexion();
    $stmt = $db->prepare('SELECT 1 FROM api_keys WHERE clave = ? AND activo = 1 LIMIT 1');
    $stmt->execute([$apiKey]);

    if ($stmt->rowCount() === 0) {
        sendJsonError(403, 'API Key inválida');
    }
}

function verificarDigest(): void {
    global $usuarios_fallback;

    if (empty($_SERVER['PHP_AUTH_DIGEST'])) {
        header('HTTP/1.1 401 Unauthorized');
        header('WWW-Authenticate: Digest realm="' . REALM . '",qop="auth",nonce="' . uniqid() . '",opaque="' . md5(REALM) . '"');
        echo json_encode(['success' => false, 'error' => 'Autenticación requerida']);
        exit;
    }

    $data = parseDigest($_SERVER['PHP_AUTH_DIGEST']);
    if (!$data) {
        sendJsonError(400, 'Cabecera Digest inválida');
    }

    $db = conexion();
    $stmt = $db->prepare('SELECT ha1 FROM usuarios WHERE nombre = ? LIMIT 1');
    $stmt->execute([$data['username']]);
    $row = $stmt->fetch(PDO::FETCH_ASSOC);

    $A1 = $row['ha1'] ?? $usuarios_fallback[$data['username']] ?? null;
    if (!$A1) {
        sendJsonError(401, 'Usuario no válido o no tiene HA1');
    }

    $A2 = md5($_SERVER['REQUEST_METHOD'] . ':' . $data['uri']);
    $validResponse = md5($A1 . ':' . $data['nonce'] . ':' . $data['nc'] . ':' . $data['cnonce'] . ':' . $data['qop'] . ':' . $A2);

    if ($validResponse !== $data['response']) {
        sendJsonError(401, 'Credenciales incorrectas');
    }
}

function parseDigest($txt) {
    $needed = ['nonce'=>1,'nc'=>1,'cnonce'=>1,'qop'=>1,'username'=>1,'uri'=>1,'response'=>1];
    $data = [];
    preg_match_all('@(\w+)=("([^"]+)"|([^\s,]+))@', $txt, $matches, PREG_SET_ORDER);
    foreach ($matches as $m) {
        $key = $m[1];
        $val = $m[3] ?: $m[4];
        $data[$key] = $val;
        unset($needed[$key]);
    }
    return $needed ? false : $data;
}

function proteger(): void {
    verificarApiKey();

    if (empty($_SERVER['PHP_AUTH_DIGEST'])) {
        header('HTTP/1.1 401 Unauthorized');
        header('WWW-Authenticate: Digest realm="' . REALM . '",qop="auth",nonce="' . uniqid() . '",opaque="' . md5(REALM) . '"');
        echo json_encode(['success' => false, 'error' => 'Autenticación requerida']);
        exit;
    }

    verificarDigest();
}

function actualizarHA1Usuarios(): void {
    $db = conexion();
    $stmt = $db->query("SELECT id, nombre, contrasena FROM usuarios WHERE ha1 IS NULL OR ha1 = ''");
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $id = $row['id'];
        $nombre = $row['nombre'];

        $passwordPlano = '1234';

        $ha1 = md5($nombre . ':' . REALM . ':' . $passwordPlano);
        $update = $db->prepare("UPDATE usuarios SET ha1 = ? WHERE id = ?");
        $update->execute([$ha1, $id]);
    }
}