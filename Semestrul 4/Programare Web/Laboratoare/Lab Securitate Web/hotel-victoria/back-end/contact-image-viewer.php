<?php
require_once 'config.php';
require_once 'functions.php';

// Only admins can view contact message images
requireAdmin();

// VULNERABILITY: Path Traversal - Accept user-supplied path without proper validation
$filePath = isset($_GET['path']) ? $_GET['path'] : '';

if (empty($filePath)) {
    http_response_code(404);
    exit;
}

// VULNERABILITY: No proper path validation - Direct file access
// This allows reading ANY file on the system
$fullPath = __DIR__ . '/' . $filePath;

// Try to read and serve the file
if (file_exists($fullPath) && is_file($fullPath)) {
    header('Content-Type: application/octet-stream');
    header('Content-Length: ' . filesize($fullPath));
    header('Content-Disposition: attachment; filename="' . basename($fullPath) . '"');
    
    readfile($fullPath);
    exit;
} else {
    http_response_code(404);
    exit;
}
?>

