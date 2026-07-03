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
$captchaChallenge = createCaptchaChallenge();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $csrfToken = isset($_POST['csrf_token']) ? $_POST['csrf_token'] : '';
    if (!verifyCsrfToken($csrfToken)) {
        $error = 'Cerere invalidă (CSRF).';
    }
    // VULNERABILITY: Don't sanitize - allows SQL injection!
    $username = isset($_POST['login_identifier']) ? $_POST['login_identifier'] : '';
    $password = isset($_POST['login_secret']) ? $_POST['login_secret'] : '';
    $rememberMe = isset($_POST['remember_me']);
    $captchaAnswer = isset($_POST['captcha_answer']) ? $_POST['captcha_answer'] : '';
    $captchaToken = isset($_POST['captcha_token']) ? $_POST['captcha_token'] : '';
    
    // Validation
    if (empty($username) || empty($password)) {
        $error = 'Vă rugăm să completați toate câmpurile';
    // VULNERABILITY: Disable CAPTCHA check
    // } elseif (!validateLoginCaptcha($captchaAnswer, $captchaToken)) {
    //     $error = 'Codul CAPTCHA este incorect';
    } else {
        $result = loginUser($username, $password, $rememberMe);
        if ($result['success']) {
            clearLoginCaptchaChallenge();
            header("Location: home.php");
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
    <title>Autentificare - Hotel Victoria</title>
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
                <h2>Autentificare</h2>
                
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
                                        <input type="hidden" name="captcha_token" value="<?php echo htmlspecialchars($captchaChallenge['token'], ENT_QUOTES, 'UTF-8'); ?>">
                    <div class="form-group">
                       <label for="login_identifier">Nume de utilizator</label>
                       <input type="text" id="login_identifier" name="login_identifier" required autocomplete="new-password" autocapitalize="off" spellcheck="false"
                               placeholder="Introduceți numele de utilizator"
                           value="<?php echo isset($_POST['login_identifier']) ? htmlspecialchars($_POST['login_identifier']) : ''; ?>">
                    </div>
                    
                    <div class="form-group">
                       <label for="login_secret">Parolă</label>
                       <input type="password" id="login_secret" name="login_secret" required autocomplete="new-password"
                               placeholder="Introduceți parola">
                    </div>

                    <div class="form-group captcha-group">
                        <div class="captcha-image-container">
                            <img src="../../back-end/captcha-image.php?token=<?php echo urlencode($captchaChallenge['token']); ?>" alt="CAPTCHA" class="captcha-image" id="captchaImage">
                        </div>
                        <input type="text" id="captcha_answer" name="captcha_answer" required 
                               placeholder="Introduceți textul din imagine"
                               autocomplete="off" maxlength="6">
                    </div>

                    <div class="form-group form-group-inline">
                        <label>
                            <input type="checkbox" name="remember_me" value="1" <?php echo isset($_POST['remember_me']) ? 'checked' : ''; ?>>
                            Remember me
                        </label>
                    </div>
                    
                    <button type="submit" class="btn btn-primary">Autentificare</button>
                </form>
                
                <div class="auth-footer">
                    <p>Nu aveți cont? <a href="register.php">Înregistrați-vă aici</a></p>
                </div>
            </div>
        </div>
    </main>
    
    <footer>
        <p>© 2026 Hotel Victoria</p>
    </footer>
</body>
</html>
