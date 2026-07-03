<?php
require_once '../../back-end/config.php';
require_once '../../back-end/nav-helper.php';

// If already logged in, redirect to home
if (isLoggedIn()) {
    header("Location: home.php");
    exit();
}

$error = '';
$success = '';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $csrfToken = isset($_POST['csrf_token']) ? $_POST['csrf_token'] : '';
    if (!verifyCsrfToken($csrfToken)) {
        $error = 'Cerere invalidă (CSRF).';
    }
    $username = isset($_POST['register_identifier']) ? sanitize($_POST['register_identifier']) : '';
    $email = isset($_POST['register_email']) ? sanitize($_POST['register_email']) : '';
    $password = isset($_POST['register_secret']) ? $_POST['register_secret'] : '';
    $confirmPassword = isset($_POST['register_secret_confirm']) ? $_POST['register_secret_confirm'] : '';
    $fullName = isset($_POST['register_full_name']) ? sanitize($_POST['register_full_name']) : '';
    
    // Validation
    if (empty($username) || empty($email) || empty($password) || empty($confirmPassword) || empty($fullName)) {
        $error = 'Vă rugăm să completați toate câmpurile';
    } elseif (strlen($username) < 3) {
        $error = 'Numele de utilizator trebuie să aibă cel puțin 3 caractere';
    } elseif (!validateEmail($email)) {
        $error = 'Vă rugăm introduceți o adresă de email validă';
    } elseif (strlen($password) < 6) {
        $error = 'Parola trebuie să aibă cel puțin 6 caractere';
    } elseif ($password !== $confirmPassword) {
        $error = 'Parolele nu se potrivesc';
    } else {
        $result = registerUser($username, $email, $password, $fullName);
        if ($result['success']) {
            $success = 'Înregistrare reușită! Vă rugăm să vă autentificați.';
            // Clear form
            $username = $email = $password = $confirmPassword = $fullName = '';
            sleep(2);
            header("Location: login.php");
            exit();
        } else {
            $error = $result['message'];
        }
    }
}
?>
<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Înregistrare - Hotel Victoria</title>
    <link rel="stylesheet" href="../Styles/style-responsive.css">
    <link rel="stylesheet" href="../Styles/auth-forms.css">
</head>
<body>
    <header>
        <h1>Hotel Victoria</h1>
    </header>
    <?php echo getNavigation(); ?>
    
    <main>
        <div class="auth-form-container">
            <div class="auth-form-wrapper">
                <h2>Înregistrare</h2>
                
                <?php if (!empty($error)): ?>
                    <div class="alert alert-error" role="alert">
                        <span class="alert-icon">✕</span>
                        <p><?php echo $error; ?></p>
                    </div>
                <?php endif; ?>
                
                <?php if (!empty($success)): ?>
                    <div class="alert alert-success" role="alert">
                        <span class="alert-icon">✓</span>
                        <p><?php echo $success; ?></p>
                    </div>
                <?php endif; ?>
                
                <form method="POST" action="" class="auth-form" autocomplete="new-password">
                    <?php echo csrfInputField(); ?>
                    <div class="form-group">
                        <label for="register_full_name">Nume complet</label>
                        <input type="text" id="register_full_name" name="register_full_name" required autocomplete="new-password" autocapitalize="off" spellcheck="false"
                               placeholder="Introduceți numele complet"
                               value="<?php echo isset($fullName) ? $fullName : ''; ?>">
                    </div>
                    
                    <div class="form-group">
                        <label for="register_identifier">Nume de utilizator</label>
                        <input type="text" id="register_identifier" name="register_identifier" required autocomplete="new-password" autocapitalize="off" spellcheck="false"
                               placeholder="Alegeți un nume de utilizator (min. 3 caractere)"
                               value="<?php echo isset($username) ? $username : ''; ?>">
                    </div>
                    
                    <div class="form-group">
                        <label for="register_email">Email</label>
                        <input type="email" id="register_email" name="register_email" required autocomplete="new-password" autocapitalize="off" spellcheck="false"
                               placeholder="Introduceți adresa de email"
                               value="<?php echo isset($email) ? $email : ''; ?>">
                    </div>
                    
                    <div class="form-group">
                        <label for="register_secret">Parolă</label>
                        <input type="password" id="register_secret" name="register_secret" required autocomplete="new-password"
                               placeholder="Alegeți o parolă (min. 6 caractere)">
                        <small class="form-hint">Parola trebuie să aibă cel puțin 6 caractere</small>
                    </div>
                    
                    <div class="form-group">
                        <label for="register_secret_confirm">Confirmați parola</label>
                        <input type="password" id="register_secret_confirm" name="register_secret_confirm" required autocomplete="new-password"
                               placeholder="Confirmați parola">
                    </div>
                    
                    <button type="submit" class="btn btn-primary">Înregistrare</button>
                </form>
                
                <div class="auth-footer">
                    <p>Aveți deja cont? <a href="login.php">Autentificați-vă aici</a></p>
                </div>
            </div>
        </div>
    </main>
    
    <footer>
        <p>© 2026 Hotel Victoria</p>
    </footer>
</body>
</html>
