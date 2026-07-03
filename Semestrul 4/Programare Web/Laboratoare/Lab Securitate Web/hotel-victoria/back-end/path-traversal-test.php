<?php
require_once __DIR__ . '/config.php';
require_once __DIR__ . '/functions.php';

requireAdmin();

header('Content-Type: text/plain; charset=UTF-8');

$baseDir = __DIR__;
$tests = [
    'config.php',
    '../back-end/config.php',
    '../../../../windows/win.ini',
];

if (isset($_GET['path'])) {
    $tests = [(string) $_GET['path']];
}

foreach ($tests as $testPath) {
    $resolvedPath = resolveSafeFilePath($baseDir, $testPath);
    if ($resolvedPath === null) {
        echo $testPath . ' => BLOCKED' . PHP_EOL;
        continue;
    }

    echo $testPath . ' => ALLOWED (' . $resolvedPath . ')' . PHP_EOL;
}
