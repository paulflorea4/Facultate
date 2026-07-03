<?php

$serverConfigFile = __DIR__ . '/server-config.php';
if (file_exists($serverConfigFile)) {
    require_once $serverConfigFile;
}

// Database Configuration
if (!defined('DB_HOST')) {
    define('DB_HOST', 'localhost');
}
if (!defined('DB_USER')) {
    define('DB_USER', 'root');
}
if (!defined('DB_PASS')) {
    define('DB_PASS', '');
}
if (!defined('DB_NAME')) {
    define('DB_NAME', 'hotel_victoria');
}

// SQLite configuration for contact message types
if (!defined('CONTACT_TYPES_DB_FILE')) {
    define('CONTACT_TYPES_DB_FILE', __DIR__ . '/contact-types.sqlite');
}

// Create connection
$conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Set charset
$conn->set_charset("utf8");

// Session handling
$sessionDir = rtrim(sys_get_temp_dir(), DIRECTORY_SEPARATOR) . DIRECTORY_SEPARATOR . 'hotel-victoria-sessions';
if (!is_dir($sessionDir)) {
    @mkdir($sessionDir, 0700, true);
}
if (is_dir($sessionDir) && is_writable($sessionDir)) {
    session_save_path($sessionDir);
}

session_name('hotel_victoria_session');
session_set_cookie_params([
    'lifetime' => 0,
    'path' => '/',
    'secure' => !empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off',
    'httponly' => true,
    'samesite' => 'Lax',
]);
session_start();

// Security headers
header('X-Frame-Options: DENY');
header('X-Content-Type-Options: nosniff');
header('Referrer-Policy: no-referrer-when-downgrade');
// Allow https resources and data: for images, permit OpenStreetMap in frames used on contact page
header("Content-Security-Policy: default-src 'self' https:; img-src 'self' data: https:; frame-src https://www.openstreetmap.org;");
// HSTS when served over HTTPS
if (!empty(
    
    
    $_SERVER['HTTPS']
    ) && $_SERVER['HTTPS'] !== 'off') {
    header('Strict-Transport-Security: max-age=63072000; includeSubDomains; preload');
}

// Remember-me cookie configuration
define('REMEMBER_ME_COOKIE', 'hotel_victoria_remember');
define('REMEMBER_ME_DURATION', 60 * 60 * 24 * 30);

function ensureRememberMeSchema($conn) {
    $columns = [
        'remember_selector' => "ALTER TABLE users ADD COLUMN remember_selector VARCHAR(64) NULL AFTER role",
        'remember_token_hash' => "ALTER TABLE users ADD COLUMN remember_token_hash VARCHAR(255) NULL AFTER remember_selector",
        'remember_expires_at' => "ALTER TABLE users ADD COLUMN remember_expires_at DATETIME NULL AFTER remember_token_hash"
    ];

    foreach ($columns as $columnName => $alterSql) {
        $check = $conn->prepare("SELECT COUNT(*) AS column_count FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = ? AND TABLE_NAME = 'users' AND COLUMN_NAME = ?");
        if (!$check) {
            continue;
        }

        $databaseName = DB_NAME;
        $check->bind_param('ss', $databaseName, $columnName);
        $check->execute();
        $result = $check->get_result()->fetch_assoc();
        $check->close();

        if (!empty($result['column_count'])) {
            continue;
        }

        $conn->query($alterSql);
    }
}

function attemptRememberMeLogin($conn) {
    if (isset($_SESSION['user_id'])) {
        return;
    }

    if (empty($_COOKIE[REMEMBER_ME_COOKIE])) {
        return;
    }

    $parts = explode(':', $_COOKIE[REMEMBER_ME_COOKIE], 2);
    if (count($parts) !== 2) {
        setcookie(REMEMBER_ME_COOKIE, '', time() - 3600, '/');
        unset($_COOKIE[REMEMBER_ME_COOKIE]);
        return;
    }

    [$selector, $validator] = $parts;
    $selector = trim($selector);
    $validator = trim($validator);

    if ($selector === '' || $validator === '') {
        setcookie(REMEMBER_ME_COOKIE, '', time() - 3600, '/');
        unset($_COOKIE[REMEMBER_ME_COOKIE]);
        return;
    }

    $query = "SELECT id, username, email, role, full_name, remember_token_hash, remember_expires_at FROM users WHERE remember_selector = ? LIMIT 1";
    $stmt = $conn->prepare($query);
    if (!$stmt) {
        return;
    }

    $stmt->bind_param('s', $selector);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows === 0) {
        setcookie(REMEMBER_ME_COOKIE, '', time() - 3600, '/');
        unset($_COOKIE[REMEMBER_ME_COOKIE]);
        return;
    }

    $user = $result->fetch_assoc();
    $stmt->close();

    if (empty($user['remember_expires_at']) || strtotime($user['remember_expires_at']) < time()) {
        $conn->query("UPDATE users SET remember_selector = NULL, remember_token_hash = NULL, remember_expires_at = NULL WHERE id = " . (int) $user['id']);
        setcookie(REMEMBER_ME_COOKIE, '', time() - 3600, '/');
        unset($_COOKIE[REMEMBER_ME_COOKIE]);
        return;
    }

    if (empty($user['remember_token_hash']) || !hash_equals($user['remember_token_hash'], hash('sha256', $validator))) {
        $conn->query("UPDATE users SET remember_selector = NULL, remember_token_hash = NULL, remember_expires_at = NULL WHERE id = " . (int) $user['id']);
        setcookie(REMEMBER_ME_COOKIE, '', time() - 3600, '/');
        unset($_COOKIE[REMEMBER_ME_COOKIE]);
        return;
    }

    session_regenerate_id(true);
    $_SESSION['user_id'] = $user['id'];
    $_SESSION['username'] = $user['username'];
    $_SESSION['email'] = $user['email'];
    $_SESSION['role'] = $user['role'];
    $_SESSION['full_name'] = $user['full_name'];

    $cookieOptions = [
        'expires' => time() + REMEMBER_ME_DURATION,
        'path' => '/',
        'secure' => !empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off',
        'httponly' => true,
        'samesite' => 'Lax'
    ];
    setcookie(REMEMBER_ME_COOKIE, $selector . ':' . $validator, $cookieOptions);
    $_COOKIE[REMEMBER_ME_COOKIE] = $selector . ':' . $validator;
}

ensureRememberMeSchema($conn);
attemptRememberMeLogin($conn);

// ============ PDO Configuration for Authentication ============
// Authentication uses MySQL via PDO
try {
    $pdo = new PDO(
        "mysql:host=" . DB_HOST . ";dbname=" . DB_NAME . ";charset=utf8mb4",
        DB_USER,
        DB_PASS,
        [
            PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
            PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
        ]
    );
} catch (PDOException $e) {
    die("PDO Connection failed: " . $e->getMessage());
}

// Separate SQLite connection for contact message types.
// This is independent from the authentication database.
$contactTypesPdo = null;
try {
    $contactTypesPdo = new PDO(
        'sqlite:' . CONTACT_TYPES_DB_FILE,
        null,
        null,
        [
            PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
            PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
        ]
    );
    $contactTypesPdo->exec('PRAGMA foreign_keys = ON');

} catch (PDOException $e) {
    error_log('Contact types SQLite connection failed: ' . $e->getMessage());
    $contactTypesPdo = null;
}

// Define base URLs
if (!defined('BASE_URL')) {
    define('BASE_URL', 'http://localhost/hotel-victoria/');
}
if (!defined('BACKEND_URL')) {
    define('BACKEND_URL', BASE_URL . 'back-end/');
}
if (!defined('FRONTEND_URL')) {
    define('FRONTEND_URL', BASE_URL . 'front-end/');
}
if (!defined('PAGES_URL')) {
    define('PAGES_URL', FRONTEND_URL . 'Pages/');
}
?>
