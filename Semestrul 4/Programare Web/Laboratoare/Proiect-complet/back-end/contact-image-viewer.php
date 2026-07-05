<?php
require_once 'config.php';
require_once 'functions.php';

// Only admins can view contact message images
requireAdmin();

$messageId = isset($_GET['id']) ? (int) $_GET['id'] : 0;

if ($messageId <= 0) {
    http_response_code(404);
    exit;
}

$message = getContactMessageById($messageId);

if (!$message || empty($message['attachment_image'])) {
    http_response_code(404);
    exit;
}

// Set proper headers for image streaming
header('Content-Type: ' . htmlspecialchars($message['attachment_mime_type']));
header('Content-Length: ' . strlen($message['attachment_image']));
header('Content-Disposition: inline; filename="' . htmlspecialchars($message['attachment_filename']) . '"');
header('Cache-Control: public, max-age=3600');

echo $message['attachment_image'];
exit;
?>
