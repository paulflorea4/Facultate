<?php
ob_start();

require_once __DIR__ . '/config.php';
require_once __DIR__ . '/functions.php';

// Prevent caching
header('Content-Type: image/png');
header('Cache-Control: no-cache, no-store, must-revalidate');
header('Pragma: no-cache');
header('Expires: 0');

$captchaToken = $_GET['token'] ?? '';
$challenge = decodeCaptchaToken($captchaToken);

if ($challenge === null) {
    ob_end_clean();
    header('Content-Type: text/plain');
    echo 'Invalid CAPTCHA token';
    exit;
}

// Check if GD library is available
if (!extension_loaded('gd')) {
    ob_end_clean();
    header('Content-Type: text/plain');
    echo 'GD library not available';
    exit;
}

$code = $challenge['code'];

// Image dimensions
$width = 280;
$height = 100;

try {
    // Create image
    $image = imagecreatetruecolor($width, $height);
    if (!$image) {
        throw new Exception('Failed to create image');
    }

    // Define colors
    $whiteColor = imagecolorallocate($image, 255, 255, 255);
    $darkBlueColor = imagecolorallocate($image, 25, 60, 110);
    $lightGrayColor = imagecolorallocate($image, 220, 225, 235);

    // Fill background with white
    imagefilledrectangle($image, 0, 0, $width, $height, $whiteColor);

    // Draw subtle border
    imagerectangle($image, 1, 1, $width - 2, $height - 2, $lightGrayColor);

    // Add some subtle noise lines
    for ($i = 0; $i < 4; $i++) {
        imageline(
            $image,
            random_int(0, $width),
            random_int(0, $height),
            random_int(0, $width),
            random_int(0, $height),
            imagecolorallocate($image, 235, 240, 245)
        );
    }

    // Draw characters
    $chars = str_split($code);
    $charSpacing = $width / 7;

    foreach ($chars as $index => $char) {
        $x = ($index * $charSpacing) + 18;
        $y = 40 + random_int(-5, 5);

        imagestring($image, 5, $x, $y, $char, $darkBlueColor);
    }

    ob_end_clean();
    imagepng($image);
    imagedestroy($image);
} catch (Exception $e) {
    ob_end_clean();
    header('Content-Type: text/plain');
    echo 'Error: ' . $e->getMessage();
    exit;
}
?>

