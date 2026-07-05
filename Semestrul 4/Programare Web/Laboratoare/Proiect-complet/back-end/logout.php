<?php
require_once __DIR__ . '/config.php';
require_once __DIR__ . '/functions.php';

// Logout user
logoutUser();

// Redirect to home page
header("Location: " . PAGES_URL . "home.php");
exit();
?>
