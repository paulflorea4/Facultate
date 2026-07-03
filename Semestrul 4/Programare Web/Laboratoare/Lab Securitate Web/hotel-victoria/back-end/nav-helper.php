<?php
require_once __DIR__ . '/config.php';
require_once __DIR__ . '/functions.php';

/**
 * Generate navigation HTML with authentication buttons
 * @return string HTML for navigation menu
 */
function getNavigation() {
    ob_start();
    ?>
    <nav id="menu">
        <ul>
            <li><a class="menu-home" href="home.php"><span class="menu-icon" aria-hidden="true"></span>Acasă</a></li>
            <li><a class="menu-rooms" href="rooms.php"><span class="menu-icon" aria-hidden="true"></span>Camere</a></li>
            <li><a class="menu-reservation" href="reservation.php"><span class="menu-icon" aria-hidden="true"></span>Rezervare</a></li>
            <li><a class="menu-contact" href="contact.php"><span class="menu-icon" aria-hidden="true"></span>Contact</a></li>
            <?php if (isAdmin()): ?>
                <li><a class="menu-dashboard" href="dashboard.php"><span class="menu-icon" aria-hidden="true"></span>Dashboard</a></li>
            <?php endif; ?>
            <li class="nav-auth">
                <?php if (isLoggedIn()): ?>
                    <div class="user-menu">
                        <span class="user-greeting">Bun venit, <?php echo htmlspecialchars($_SESSION['full_name']); ?></span>
                        <a href="../../back-end/logout.php" class="btn-auth btn-logout">Deconectare</a>
                    </div>
                <?php else: ?>
                    <a href="login.php" class="btn-auth btn-login">Autentificare</a>
                    <a href="register.php" class="btn-auth btn-register">Înregistrare</a>
                <?php endif; ?>
            </li>
        </ul>
    </nav>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <?php
    return ob_get_clean();
}
?>
