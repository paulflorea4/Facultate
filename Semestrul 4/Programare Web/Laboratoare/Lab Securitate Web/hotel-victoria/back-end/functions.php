<?php
// Include config
require_once __DIR__ . '/config.php';

// Sanitize input
function sanitize($input) {
    return htmlspecialchars(trim($input), ENT_QUOTES, 'UTF-8');
}

function resolveSafeFilePath($baseDir, $requestedPath) {
    $baseRealPath = realpath($baseDir);
    if ($baseRealPath === false) {
        return null;
    }

    $requestedPath = trim((string) $requestedPath);
    if ($requestedPath === '') {
        return null;
    }

    if (preg_match('/[\x00-\x1F]/', $requestedPath)) {
        return null;
    }

    $requestedPath = str_replace(['/', '\\'], DIRECTORY_SEPARATOR, $requestedPath);
    $requestedPath = ltrim($requestedPath, DIRECTORY_SEPARATOR);

    $candidatePath = $baseRealPath . DIRECTORY_SEPARATOR . $requestedPath;
    $candidateRealPath = realpath($candidatePath);
    if ($candidateRealPath === false || !is_file($candidateRealPath)) {
        return null;
    }

    $basePrefix = rtrim($baseRealPath, DIRECTORY_SEPARATOR) . DIRECTORY_SEPARATOR;
    $candidateRealPath = rtrim($candidateRealPath, DIRECTORY_SEPARATOR);

    if (strncmp($candidateRealPath, $basePrefix, strlen($basePrefix)) !== 0) {
        return null;
    }

    return $candidateRealPath;
}

function isSafeFilePath($baseDir, $requestedPath) {
    return resolveSafeFilePath($baseDir, $requestedPath) !== null;
}

// CSRF helpers
function generateCsrfToken() {
    if (!isset($_SESSION)) {
        return null;
    }
    if (!isset($_SESSION['csrf_tokens']) || !is_array($_SESSION['csrf_tokens'])) {
        $_SESSION['csrf_tokens'] = [];
    }
    // Prune expired tokens
    $now = time();
    foreach ($_SESSION['csrf_tokens'] as $t => $exp) {
        if ($exp < $now) {
            unset($_SESSION['csrf_tokens'][$t]);
        }
    }

    $token = bin2hex(random_bytes(32));
    $_SESSION['csrf_tokens'][$token] = $now + 3600; // 1 hour
    return $token;
}

function csrfInputField() {
    $t = generateCsrfToken();
    return '<input type="hidden" name="csrf_token" value="' . htmlspecialchars($t) . '">';
}

function verifyCsrfToken($token) {
    if (!isset($_SESSION) || empty($token)) {
        return false;
    }
    if (empty($_SESSION['csrf_tokens']) || !is_array($_SESSION['csrf_tokens'])) {
        return false;
    }
    if (!isset($_SESSION['csrf_tokens'][$token])) {
        return false;
    }
    $exp = $_SESSION['csrf_tokens'][$token];
    if ($exp < time()) {
        unset($_SESSION['csrf_tokens'][$token]);
        return false;
    }
    // single-use token
    unset($_SESSION['csrf_tokens'][$token]);
    return true;
}

// Validate email
function validateEmail($email) {
    return filter_var($email, FILTER_VALIDATE_EMAIL);
}

// Login user - SECURE VERSION (commented out for testing)
/*
function loginUserSecure($username, $password, $rememberMe = false) {
    global $pdo;
    
    $username = sanitize($username);
    
    try {
        $query = "SELECT id, username, password, email, role, full_name FROM users WHERE username = :username";
        $stmt = $pdo->prepare($query);
        $stmt->bindParam(':username', $username);
        $stmt->execute();
        
        $user = $stmt->fetch();
        
        if (!$user) {
            return ['success' => false, 'message' => 'Nume de utilizator sau parolă incorectă'];
        }
        
        if (verifyPassword($password, $user['password'])) {
            session_regenerate_id(true);
            $_SESSION['user_id'] = $user['id'];
            $_SESSION['username'] = $user['username'];
            $_SESSION['email'] = $user['email'];
            $_SESSION['role'] = $user['role'];
            $_SESSION['full_name'] = $user['full_name'];

            if ($rememberMe) {
                createRememberMeToken($user['id']);
            } else {
                clearRememberMeToken($user['id']);
            }

            return ['success' => true, 'message' => 'Autentificare reușită'];
        } else {
            return ['success' => false, 'message' => 'Nume de utilizator sau parolă incorectă'];
        }
    } catch (PDOException $e) {
        return ['success' => false, 'message' => 'Eroare la autentificare: ' . $e->getMessage()];
    }
}
*/

// Hash password
function hashPassword($password) {
    return password_hash($password, PASSWORD_BCRYPT);
}

// Verify password
function verifyPassword($password, $hash) {
    return password_verify($password, $hash);
}

if (!defined('CAPTCHA_SECRET')) {
    define('CAPTCHA_SECRET', hash('sha256', DB_NAME . '|hotel-victoria-captcha-v1'));
}

function generateCaptchaCode($length = 6) {
    $characters = 'ABCDEFGHJKLMNPQRSTUVWXYZ23456789';
    $code = '';

    for ($i = 0; $i < $length; $i++) {
        $code .= $characters[random_int(0, strlen($characters) - 1)];
    }

    return $code;
}

function createCaptchaChallenge($length = 6, $ttlSeconds = 600) {
    $code = generateCaptchaCode($length);
    $expiresAt = time() + $ttlSeconds;
    $payload = $code . '|' . $expiresAt;
    $signature = hash_hmac('sha256', $payload, CAPTCHA_SECRET);

    return [
        'code' => $code,
        'expires_at' => $expiresAt,
        'token' => base64_encode($payload . '|' . $signature),
    ];
}

function decodeCaptchaToken($token) {
    $decoded = base64_decode((string) $token, true);
    if ($decoded === false) {
        return null;
    }

    $parts = explode('|', $decoded, 3);
    if (count($parts) !== 3) {
        return null;
    }

    [$code, $expiresAt, $signature] = $parts;
    if ($code === '' || !ctype_digit($expiresAt)) {
        return null;
    }

    $payload = $code . '|' . $expiresAt;
    $expectedSignature = hash_hmac('sha256', $payload, CAPTCHA_SECRET);
    if (!hash_equals($expectedSignature, $signature)) {
        return null;
    }

    if ((int) $expiresAt < time()) {
        return null;
    }

    return [
        'code' => $code,
        'expires_at' => (int) $expiresAt,
    ];
}

function validateLoginCaptcha($userAnswer, $captchaToken = '') {
    $captchaToken = (string) $captchaToken;

    if ($captchaToken !== '') {
        $challenge = decodeCaptchaToken($captchaToken);
        if ($challenge === null) {
            return false;
        }

        $userAnswer = strtoupper(trim($userAnswer));
        $correctAnswer = strtoupper($challenge['code']);

        return hash_equals($correctAnswer, $userAnswer);
    }

    if (!isset($_SESSION['captcha_code'])) {
        return false;
    }

    $userAnswer = strtoupper(trim($userAnswer));
    $correctAnswer = strtoupper($_SESSION['captcha_code']);

    return hash_equals($correctAnswer, $userAnswer);
}

function clearLoginCaptchaChallenge() {
    unset($_SESSION['captcha_code']);
}

function createRememberMeToken($userId) {
    global $pdo;

    $selector = bin2hex(random_bytes(16));
    $validator = bin2hex(random_bytes(32));
    $tokenHash = hash('sha256', $validator);
    $expiresAt = date('Y-m-d H:i:s', time() + REMEMBER_ME_DURATION);

    try {
        $query = "UPDATE users SET remember_selector = :selector, remember_token_hash = :token_hash, remember_expires_at = :expires_at WHERE id = :user_id";
        $stmt = $pdo->prepare($query);
        $stmt->bindParam(':selector', $selector);
        $stmt->bindParam(':token_hash', $tokenHash);
        $stmt->bindParam(':expires_at', $expiresAt);
        $stmt->bindParam(':user_id', $userId, PDO::PARAM_INT);
        $stmt->execute();
    } catch (PDOException $e) {
        // Silently fail if update fails
    }

    $cookieValue = $selector . ':' . $validator;
    $cookieOptions = [
        'expires' => time() + REMEMBER_ME_DURATION,
        'path' => '/',
        'secure' => !empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off',
        'httponly' => true,
        'samesite' => 'Lax'
    ];
    setcookie(REMEMBER_ME_COOKIE, $cookieValue, $cookieOptions);
    $_COOKIE[REMEMBER_ME_COOKIE] = $cookieValue;
}

function clearRememberMeToken($userId = null) {
    global $pdo;

    if ($userId !== null) {
        $userId = (int) $userId;
        try {
            $query = "UPDATE users SET remember_selector = NULL, remember_token_hash = NULL, remember_expires_at = NULL WHERE id = :user_id";
            $stmt = $pdo->prepare($query);
            $stmt->bindParam(':user_id', $userId, PDO::PARAM_INT);
            $stmt->execute();
        } catch (PDOException $e) {
            // Silently fail if update fails
        }
    }

    setcookie(REMEMBER_ME_COOKIE, '', time() - 3600, '/');
    unset($_COOKIE[REMEMBER_ME_COOKIE]);
}

// Check if user is logged in
function isLoggedIn() {
    return isset($_SESSION['user_id']) && isset($_SESSION['username']);
}

// Check if user is admin
function isAdmin() {
    return isLoggedIn() && isset($_SESSION['role']) && $_SESSION['role'] === 'admin';
}

// Redirect if not logged in
function requireLogin() {
    if (!isLoggedIn()) {
        header("Location: " . PAGES_URL . "login.php");
        exit();
    }
}

// Redirect if not admin
function requireAdmin() {
    if (!isAdmin()) {
        header("Location: " . PAGES_URL . "home.html");
        exit();
    }
}

// Get user info
function getUserInfo($userId) {
    global $conn;
    $userId = intval($userId);
    $query = "SELECT id, username, email, full_name, role, created_at FROM users WHERE id = ?";
    $stmt = $conn->prepare($query);
    $stmt->bind_param("i", $userId);
    $stmt->execute();
    return $stmt->get_result()->fetch_assoc();
}

// Check if email exists
function emailExists($email) {
    global $conn;
    $email = sanitize($email);
    $query = "SELECT id FROM users WHERE email = ?";
    $stmt = $conn->prepare($query);
    $stmt->bind_param("s", $email);
    $stmt->execute();
    return $stmt->get_result()->num_rows > 0;
}

// Check if username exists
function usernameExists($username) {
    global $conn;
    $username = sanitize($username);
    $query = "SELECT id FROM users WHERE username = ?";
    $stmt = $conn->prepare($query);
    $stmt->bind_param("s", $username);
    $stmt->execute();
    return $stmt->get_result()->num_rows > 0;
}

// Register user (default role: client)
function registerUser($username, $email, $password, $fullName) {
    global $pdo;
    
    $username = sanitize($username);
    $email = sanitize($email);
    $fullName = sanitize($fullName);
    $passwordHash = hashPassword($password);
    $role = 'client'; // Default role
    
    if (usernameExists($username)) {
        return ['success' => false, 'message' => 'Numele de utilizator există deja'];
    }
    
    if (emailExists($email)) {
        return ['success' => false, 'message' => 'Email-ul există deja'];
    }
    
    try {
        $query = "INSERT INTO users (username, email, password, full_name, role, created_at) VALUES (:username, :email, :password, :full_name, :role, NOW())";
        $stmt = $pdo->prepare($query);
        $stmt->bindParam(':username', $username);
        $stmt->bindParam(':email', $email);
        $stmt->bindParam(':password', $passwordHash);
        $stmt->bindParam(':full_name', $fullName);
        $stmt->bindParam(':role', $role);
        
        if ($stmt->execute()) {
            return ['success' => true, 'message' => 'Înregistrare reușită'];
        } else {
            return ['success' => false, 'message' => 'Înregistrare eșuată'];
        }
    } catch (PDOException $e) {
        return ['success' => false, 'message' => 'Eroare la înregistrare: ' . $e->getMessage()];
    }
}

// Login user - VULNERABLE VERSION FOR TESTING (SQL Injection)
function loginUser($username, $password, $rememberMe = false) {
    global $pdo;
    
    // VULNERABILITY: Direct concatenation without prepared statement
    try {
        $query = "SELECT id, username, password, email, role, full_name FROM users WHERE username = '" . $username . "'";
        $result = $pdo->query($query);
        $user = $result->fetch();
        
        if (!$user) {
            return ['success' => false, 'message' => 'Nume de utilizator sau parolă incorectă'];
        }
        
        // VULNERABILITY: Skip password verification - SQL Injection bypasses auth!
        // Password check is commented out, so ANY user returned by SQL injection will be logged in
        // if (verifyPassword($password, $user['password'])) {
            session_regenerate_id(true);
            $_SESSION['user_id'] = $user['id'];
            $_SESSION['username'] = $user['username'];
            $_SESSION['email'] = $user['email'];
            $_SESSION['role'] = $user['role'];
            $_SESSION['full_name'] = $user['full_name'];

            if ($rememberMe) {
                createRememberMeToken($user['id']);
            } else {
                clearRememberMeToken($user['id']);
            }

            return ['success' => true, 'message' => 'Autentificare reușită'];
        // } else {
        //     return ['success' => false, 'message' => 'Nume de utilizator sau parolă incorectă'];
        // }
    } catch (PDOException $e) {
        return ['success' => false, 'message' => 'Eroare la autentificare: ' . $e->getMessage()];
    }
}

// Logout user
function logoutUser() {
    if (isset($_SESSION['user_id'])) {
        clearRememberMeToken($_SESSION['user_id']);
    } else {
        clearRememberMeToken();
    }

    session_destroy();
    return ['success' => true, 'message' => 'Deconectat cu succes'];
}

// Get all rooms for reservation forms and room listings.
function getRooms() {
    global $conn;

    $rooms = [];
    $query = "SELECT id, room_number, room_type, capacity, price_per_night, description, status FROM rooms ORDER BY id ASC";
    $result = $conn->query($query);

    if ($result) {
        while ($row = $result->fetch_assoc()) {
            $rooms[] = $row;
        }
    }

    return $rooms;
}

// Get a single room by its database id.
function getRoomById($roomId) {
    global $conn;

    $roomId = (int) $roomId;
    if ($roomId <= 0) {
        return null;
    }

    $query = "SELECT id, room_number, room_type, capacity, price_per_night, description, status FROM rooms WHERE id = ? LIMIT 1";
    $stmt = $conn->prepare($query);
    if (!$stmt) {
        return null;
    }

    $stmt->bind_param('i', $roomId);
    $stmt->execute();
    $result = $stmt->get_result();
    $room = $result ? $result->fetch_assoc() : null;
    $stmt->close();

    return $room ?: null;
}

// Insert a reservation using the reservations table schema.
function createReservation($roomId, $checkIn, $checkOut, $guests, $specialRequests = null, $userId = null) {
    global $conn;

    $roomId = (int) $roomId;
    $guests = (int) $guests;
    $specialRequests = $specialRequests !== null ? trim($specialRequests) : null;

    if (!getRoomById($roomId)) {
        return ['success' => false, 'message' => 'Camera selectată nu există.'];
    }

    // Capacity check: ensure requested guests do not exceed room capacity
    $roomInfo = getRoomById($roomId);
    if ($roomInfo && isset($roomInfo['capacity'])) {
        $roomCapacity = (int) $roomInfo['capacity'];
        if ($guests > $roomCapacity) {
            return ['success' => false, 'message' => 'Numărul de persoane depășește capacitatea camerei selectate.'];
        }
    }

    // Availability check: ensure no overlapping reservation exists for the same room
    $availQuery = "SELECT COUNT(*) AS cnt FROM reservations WHERE room_id = ? AND check_in < ? AND check_out > ? AND status != 'cancelled'";
    $availStmt = $conn->prepare($availQuery);
    if ($availStmt) {
        // bind: room_id (i), requested_check_out (s), requested_check_in (s)
        $availStmt->bind_param('iss', $roomId, $checkOut, $checkIn);
        $availStmt->execute();
        $availResult = $availStmt->get_result();
        $row = $availResult ? $availResult->fetch_assoc() : null;
        $availCount = $row ? (int) $row['cnt'] : 0;
        $availStmt->close();

        if ($availCount > 0) {
            return ['success' => false, 'message' => 'Camera nu este disponibilă pentru datele selectate.'];
        }
    }

    $status = 'În așteptare';

    if ($userId === null) {
        $query = "INSERT INTO reservations (user_id, room_id, check_in, check_out, guests, special_requests, status, created_at) VALUES (NULL, ?, ?, ?, ?, ?, ?, NOW())";
        $stmt = $conn->prepare($query);
        if (!$stmt) {
            return ['success' => false, 'message' => 'Nu am putut salva rezervarea.'];
        }

        $stmt->bind_param('ississ', $roomId, $checkIn, $checkOut, $guests, $specialRequests, $status);
    } else {
        $userId = (int) $userId;
        $query = "INSERT INTO reservations (user_id, room_id, check_in, check_out, guests, special_requests, status, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
        $stmt = $conn->prepare($query);
        if (!$stmt) {
            return ['success' => false, 'message' => 'Nu am putut salva rezervarea.'];
        }

        $stmt->bind_param('iississ', $userId, $roomId, $checkIn, $checkOut, $guests, $specialRequests, $status);
    }

    if (!$stmt->execute()) {
        $stmt->close();
        return ['success' => false, 'message' => 'Nu am putut salva rezervarea.'];
    }

    $reservationId = $stmt->insert_id;
    $stmt->close();

    return [
        'success' => true,
        'message' => 'Rezervare trimisă cu succes.',
        'reservation_id' => $reservationId
    ];
}

// Save a contact message with optional image attachment
function saveContactMessage($userId, $fullName, $email, $subject, $message, $imageBlob = null, $imageFilename = null, $imageMimeType = null) {
    global $conn;

    $userId = (int) $userId;
    $fullName = sanitize($fullName);
    $email = sanitize($email);
    $subject = sanitize($subject);
    $message = sanitize($message);

    if ($userId <= 0) {
        return ['success' => false, 'message' => 'ID utilizator nevalid'];
    }

    if (empty($fullName) || empty($email) || empty($subject) || empty($message)) {
        return ['success' => false, 'message' => 'Toate câmpurile sunt necesare'];
    }

    if (!validateEmail($email)) {
        return ['success' => false, 'message' => 'Email nevalid'];
    }

    // Validate image if provided
    if ($imageBlob !== null) {
        // Max 5MB
        if (strlen($imageBlob) > 5 * 1024 * 1024) {
            return ['success' => false, 'message' => 'Fișierul este prea mare (max 5MB)'];
        }

        // Validate MIME type
        $allowedMimes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];
        if (!in_array($imageMimeType, $allowedMimes)) {
            return ['success' => false, 'message' => 'Tip de fișier neacceptat. Utilizați JPG, PNG, GIF sau WebP'];
        }
    }

    $query = "INSERT INTO contact_messages (user_id, full_name, email, subject, message, attachment_image, attachment_filename, attachment_mime_type, status, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'new', NOW())";
    $stmt = $conn->prepare($query);

    if (!$stmt) {
        return ['success' => false, 'message' => 'Eroare la prepararea cererii'];
    }

    $stmt->bind_param('isssssss', $userId, $fullName, $email, $subject, $message, $imageBlob, $imageFilename, $imageMimeType);

    if (!$stmt->execute()) {
        $stmt->close();
        return ['success' => false, 'message' => 'Eroare la salvarea mesajului'];
    }

    $messageId = $stmt->insert_id;
    $stmt->close();

    return [
        'success' => true,
        'message' => 'Mesaj trimis cu succes',
        'message_id' => $messageId
    ];
}

// Get all contact messages (admin only)
function getContactMessages($limit = 50, $offset = 0) {
    global $conn;

    $limit = (int) $limit;
    $offset = (int) $offset;

    $query = "SELECT id, user_id, full_name, email, subject, message, attachment_mime_type, status, created_at FROM contact_messages ORDER BY created_at DESC LIMIT ? OFFSET ?";
    $stmt = $conn->prepare($query);

    if (!$stmt) {
        return [];
    }

    $stmt->bind_param('ii', $limit, $offset);
    $stmt->execute();
    $result = $stmt->get_result();
    $messages = [];

    while ($row = $result->fetch_assoc()) {
        $messages[] = $row;
    }

    $stmt->close();
    return $messages;
}

// Get the first contact message (by creation date ascending) for a specific user
function getFirstContactMessageByUser($userId) {
    global $conn;

    $userId = (int) $userId;
    if ($userId <= 0) {
        return null;
    }

    $query = "SELECT id, user_id, full_name, email, subject, message, attachment_image, attachment_filename, attachment_mime_type, status, created_at FROM contact_messages WHERE user_id = ? ORDER BY created_at ASC LIMIT 1";
    $stmt = $conn->prepare($query);

    if (!$stmt) {
        return null;
    }

    $stmt->bind_param('i', $userId);
    $stmt->execute();
    $result = $stmt->get_result();
    $message = $result ? $result->fetch_assoc() : null;
    $stmt->close();

    return $message ?: null;
}

// Get a single contact message by ID
function getContactMessageById($messageId) {
    global $conn;

    $messageId = (int) $messageId;

    if ($messageId <= 0) {
        return null;
    }

    $query = "SELECT id, user_id, full_name, email, subject, message, attachment_image, attachment_filename, attachment_mime_type, status, created_at FROM contact_messages WHERE id = ? LIMIT 1";
    $stmt = $conn->prepare($query);

    if (!$stmt) {
        return null;
    }

    $stmt->bind_param('i', $messageId);
    $stmt->execute();
    $result = $stmt->get_result();
    $message = $result ? $result->fetch_assoc() : null;
    $stmt->close();

    return $message ?: null;
}

// Delete a contact message (admin only)
function deleteContactMessage($messageId) {
    global $conn;

    $messageId = (int) $messageId;

    if ($messageId <= 0) {
        return ['success' => false, 'message' => 'ID mesaj nevalid'];
    }

    $query = "DELETE FROM contact_messages WHERE id = ?";
    $stmt = $conn->prepare($query);

    if (!$stmt) {
        return ['success' => false, 'message' => 'Eroare la prepararea cererii'];
    }

    $stmt->bind_param('i', $messageId);

    if (!$stmt->execute()) {
        $stmt->close();
        return ['success' => false, 'message' => 'Eroare la ștergerea mesajului'];
    }

    $stmt->close();

    return ['success' => true, 'message' => 'Mesaj șters cu succes'];
}

// Get contact message types from database
// Tries the dedicated SQLite connection first, then falls back to hardcoded values
function getContactMessageTypes() {
    global $contactTypesPdo;
    
    $types = [];
    
    // Try to load from SQLite if available
    if ($contactTypesPdo) {
        try {
            $query = "SELECT type_code, label FROM contact_message_types ORDER BY display_order ASC";
            $stmt = $contactTypesPdo->prepare($query);
            $stmt->execute();
            
            while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
                $types[$row['type_code']] = $row['label'];
            }
            
            if (!empty($types)) {
                return $types;
            }
        } catch (PDOException $e) {
            return ["DEBUG_SQL_ERROR" => $e->getMessage()];
        }
    }
    
    // Default hardcoded values (fallback)
    return [
        'reservation' => 'Informații despre rezervări',
        'facilities' => 'Facilități și servicii',
        'complaint' => 'Reclamațieee',
        'suggestion' => 'Sugestie',
        'events' => 'Evenimente corporate',
        'other' => 'Alteleee'
    ];
}
?>
