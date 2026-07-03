<?php
require_once '../../back-end/config.php';
require_once '../../back-end/functions.php';
require_once '../../back-end/nav-helper.php';

requireLogin();

$successMessage = '';
$errorMessage = '';
$isAdmin = isAdmin();

// CSRF protection: verify token on any POST - DISABLED FOR TESTING
/*
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $csrfToken = isset($_POST['csrf_token']) ? $_POST['csrf_token'] : '';
    if (!verifyCsrfToken($csrfToken)) {
        $errorMessage = 'Cerere invalidă (CSRF).';
    }
}
*/

// Handle delete action for admins
if (empty($errorMessage) && $_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['action']) && $_POST['action'] === 'delete_message' && $isAdmin) {
    $messageId = isset($_POST['message_id']) ? (int) $_POST['message_id'] : 0;
    $result = deleteContactMessage($messageId);
    if ($result['success']) {
        $successMessage = $result['message'];
    } else {
        $errorMessage = $result['message'];
    }
}

// Handle contact form submission for clients
if (empty($errorMessage) && $_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['action']) && $_POST['action'] === 'send_message' && !$isAdmin) {
    $fullName = isset($_POST['contact_full_name']) ? trim($_POST['contact_full_name']) : '';
    $email = isset($_POST['contact_email']) ? trim($_POST['contact_email']) : '';
    $subject = isset($_POST['contact_subject']) ? trim($_POST['contact_subject']) : '';
    $message = isset($_POST['contact_message']) ? trim($_POST['contact_message']) : '';

    // Validate required fields
    if (empty($fullName) || empty($email) || empty($subject) || empty($message)) {
        $errorMessage = 'Toate câmpurile sunt necesare';
    } else {
        // Handle file upload - VULNERABILITY: No validation for testing
        $imageBlob = null;
        $imageFilename = null;
        $imageMimeType = null;

        if (isset($_FILES['contact_attachment']) && $_FILES['contact_attachment']['error'] !== UPLOAD_ERR_NO_FILE) {
            $file = $_FILES['contact_attachment'];
            // VULNERABILITY: Unrestricted File Upload - Accept ANY file without validation
            $imageBlob = file_get_contents($file['tmp_name']);
            $imageFilename = basename($file['name']); // No sanitization
            $imageMimeType = $file['type']; // Use browser-provided MIME type (can be spoofed)
        }
        // If no validation errors, save the message
        if (empty($errorMessage)) {
            $result = saveContactMessage(
                $_SESSION['user_id'],
                $fullName,
                $email,
                $subject,
                $message,
                $imageBlob,
                $imageFilename,
                $imageMimeType
            );

            if ($result['success']) {
                $successMessage = $result['message'];
                // Clear form
                $fullName = '';
                $email = '';
                $subject = '';
                $message = '';
            } else {
                $errorMessage = $result['message'];
            }
        }
    }
}

// Prefill subject, message, name, email and show previous attachment preview (allow editing)
if (!$isAdmin) {
    // Ensure all form variables exist
    if (!isset($fullName)) $fullName = '';
    if (!isset($email)) $email = '';
    if (!isset($subject)) $subject = '';
    if (!isset($message)) $message = '';

    // Default name/email to session values when empty
    if (empty($fullName) && isset($_SESSION['full_name'])) {
        $fullName = $_SESSION['full_name'];
    }
    if (empty($email) && isset($_SESSION['email'])) {
        $email = $_SESSION['email'];
    }

    // Only prefill when there's no submitted subject/message (i.e., on initial GET or after successful clear)
    if (empty($subject) && empty($message)) {
        $prefill = getFirstContactMessageByUser($_SESSION['user_id']);
        if ($prefill) {
            // Only prefill subject when it matches one of the allowed option values
            $allowedSubjects = ['reservation', 'facilities', 'complaint', 'suggestion', 'events', 'other'];
            if (isset($prefill['subject']) && in_array($prefill['subject'], $allowedSubjects, true)) {
                $subject = $prefill['subject'];
            }

            if (isset($prefill['message']) && $prefill['message'] !== '') {
                // Stored messages were sanitized before saving; decode entities so textarea shows original content
                $message = html_entity_decode($prefill['message'], ENT_QUOTES, 'UTF-8');
            }
            // Prefill name/email if available and not already set
            if (isset($prefill['full_name']) && $prefill['full_name'] !== '' && empty($fullName)) {
                $fullName = $prefill['full_name'];
            }
            if (isset($prefill['email']) && $prefill['email'] !== '' && empty($email)) {
                $email = $prefill['email'];
            }

            // Do NOT prefill or preview previous attachment: user requested no automatic attachment prefill
        }
    }
}

?>

<!DOCTYPE html>
<html lang="ro">

<head>
    <meta charset="UTF-8">
    <title>Contact</title>
    <link rel="stylesheet" href="../Styles/style-responsive.css">
    <style>
        .contact-image-thumbnail {
            max-width: 80px;
            max-height: 80px;
            cursor: pointer;
            border-radius: 4px;
            border: 1px solid #ddd;
        }

        .contact-image-thumbnail:hover {
            border-color: #666;
            opacity: 0.8;
        }

        .message-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        .message-table thead {
            background-color: #f5f5f5;
        }

        .message-table th,
        .message-table td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        .message-table th {
            font-weight: 600;
            background-color: #f5f5f5;
        }

        .message-table tbody tr:hover {
            background-color: #f9f9f9;
        }

        .message-actions {
            display: flex;
            gap: 10px;
        }

        .subject-option-list {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 12px;
            margin-top: 12px;
        }

        .subject-option-card {
            display: block;
            border: 1px solid #cfd8e3;
            border-radius: 8px;
            padding: 12px 14px;
            background: #fff;
            cursor: pointer;
            transition: border-color 0.15s ease, box-shadow 0.15s ease, transform 0.15s ease;
        }

        .subject-option-card:hover {
            border-color: #7aa7d9;
            box-shadow: 0 4px 14px rgba(0, 0, 0, 0.06);
            transform: translateY(-1px);
        }

        .subject-option-card input {
            margin-right: 10px;
        }

        .subject-option-card.is-selected {
            border-color: #2b7de9;
            background: #f4f9ff;
        }

        .subject-option-title {
            font-weight: 600;
        }

        .subject-option-description {
            margin-top: 4px;
            color: #57606a;
            font-size: 0.92rem;
        }

        .btn-delete {
            background-color: #dc3545;
            color: white;
            padding: 6px 12px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 12px;
        }

        .btn-delete:hover {
            background-color: #c82333;
        }

        .alert {
            padding: 12px 16px;
            margin-bottom: 20px;
            border-radius: 4px;
            border-left: 4px solid;
        }

        .alert-success {
            background-color: #d4edda;
            border-left-color: #28a745;
            color: #155724;
        }

        .alert-error {
            background-color: #f8d7da;
            border-left-color: #dc3545;
            color: #721c24;
        }

        .image-preview-modal {
            display: none;
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
        }

        .image-preview-content {
            background-color: white;
            margin: 5% auto;
            padding: 20px;
            border-radius: 8px;
            max-width: 600px;
            max-height: 80vh;
            overflow: auto;
        }

        .image-preview-content img {
            max-width: 100%;
            height: auto;
        }

        .close-modal {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
            cursor: pointer;
        }

        .close-modal:hover {
            color: #000;
        }
    </style>
</head>

<body>
    <header>
        <h1>Contact</h1>
    </header>
    <?php echo getNavigation(); ?>
    <main>
        <?php if (!$isAdmin): ?>
            <h2>Date de contact</h2>
            <p>Pentru rezervări rapide, solicitări corporate sau informații generale, ne puteți contacta prin canalele de mai jos.</p>

            <section class="stats-widgets contact-widgets" aria-label="Informații de contact hotel">
                <div class="stat-widget">
                    <p class="stat-label">Email recepție</p>
                    <p class="stat-value">contact@hotelvictoria.ro</p>
                </div>
                <div class="stat-widget">
                    <p class="stat-label">Telefon recepție</p>
                    <p class="stat-value">0700 000 000</p>
                </div>
                <div class="stat-widget">
                    <p class="stat-label">Telefon rezervări</p>
                    <p class="stat-value">0700 111 222</p>
                </div>
                <div class="stat-widget">
                    <p class="stat-label">Adresă</p>
                    <p class="stat-value">Str. Memorandumului 12, Cluj-Napoca</p>
                </div>
                <div class="stat-widget">
                    <p class="stat-label">Program recepție</p>
                    <p class="stat-value">Non-stop (24/7)</p>
                </div>
                <div class="stat-widget">
                    <p class="stat-label">Email evenimente</p>
                    <p class="stat-value">events@hotelvictoria.ro</p>
                </div>
            </section>

            <section class="map-widget" aria-label="Locația hotelului pe hartă">
                <h3>Ne găsiți aici</h3>
                <p>Hotel Victoria, Cluj-Napoca</p>
                <iframe class="map-frame"
                    src="https://www.openstreetmap.org/export/embed.html?bbox=23.565%2C46.745%2C23.615%2C46.785&layer=mapnik&marker=46.765%2C23.59"
                    title="Harta locației Hotel Victoria" loading="lazy" referrerpolicy="no-referrer-when-downgrade">
                </iframe>
            </section>

            <section class="contact-form-section">
                <h3>Trimiteți-ne un mesaj</h3>

                <?php if (!empty($successMessage)): ?>
                    <div class="alert alert-success">
                        <?php echo htmlspecialchars($successMessage); ?>
                    </div>
                <?php endif; ?>

                <?php if (!empty($errorMessage)): ?>
                    <div class="alert alert-error">
                        <?php echo htmlspecialchars($errorMessage); ?>
                    </div>
                <?php endif; ?>

                <form method="POST" enctype="multipart/form-data" novalidate>
                    <input type="hidden" name="action" value="send_message">
                    <?php echo csrfInputField(); ?>

                    <fieldset>
                        <legend>Informații de contact</legend>

                        <label for="contact_full_name">Nume complet <span class="required">*</span>:</label>
                        <input type="text" id="contact_full_name" name="contact_full_name"
                            value="<?php echo htmlspecialchars($fullName); ?>" required><br>

                        <label for="contact_email">Email <span class="required">*</span>:</label>
                        <input type="email" id="contact_email" name="contact_email"
                            value="<?php echo htmlspecialchars($email); ?>" required><br>

                        <label for="contact_subject">Subiectul mesajului <span class="required">*</span>:</label>
                        <div class="subject-option-list" role="radiogroup" aria-label="Subiectul mesajului">
                            <?php
                            $messageTypes = getContactMessageTypes();
                            foreach ($messageTypes as $code => $label):
                                $isSelected = ($subject === $code);
                                ?>
                                <label class="subject-option-card <?php echo $isSelected ? 'is-selected' : ''; ?>">
                                    <input type="radio" name="contact_subject" value="<?php echo htmlspecialchars($code); ?>" <?php echo $isSelected ? 'checked' : ''; ?> required>
                                    <span class="subject-option-title"><?php echo htmlspecialchars($label); ?></span>
                                </label>
                            <?php endforeach; ?>
                        </div><br>
                    </fieldset>

                    <fieldset>
                        <legend>Mesaj și atașament</legend>

                        <label for="contact_message">Mesajul dumneavoastră <span class="required">*</span>:</label>
                        <textarea id="contact_message" name="contact_message" rows="6"
                            placeholder="Descrieți în detaliu mesajul dumneavoastră..." required><?php echo htmlspecialchars($message); ?></textarea><br>

                        <label for="contact_attachment">Atașează o imagine (opțional):</label>
                        <input type="file" id="contact_attachment" name="contact_attachment"
                            accept="image/jpeg,image/png,image/gif,image/webp"><br>
                    </fieldset>

                    <button type="submit">Trimite mesajul</button>
                    <button type="reset">Șterge formularul</button>
                </form>
            </section>

        <?php else: ?>
            <!-- Admin view: Messages table -->
            <h2>Mesaje de contact</h2>

            <?php if (!empty($successMessage)): ?>
                <div class="alert alert-success">
                    <?php echo htmlspecialchars($successMessage); ?>
                </div>
            <?php endif; ?>

            <?php if (!empty($errorMessage)): ?>
                <div class="alert alert-error">
                    <?php echo htmlspecialchars($errorMessage); ?>
                </div>
            <?php endif; ?>

            <div class="table-container">
                <table class="message-table">
                    <thead>
                        <tr>
                            <th>Nume</th>
                            <th>Email</th>
                            <th>Subiect</th>
                            <th>Mesaj</th>
                            <th>Imagine</th>
                            <th>Status</th>
                            <th>Data</th>
                            <th>Acțiuni</th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php
                        $messages = getContactMessages(50, 0);
                        if (count($messages) > 0):
                            foreach ($messages as $msg):
                                $hasImage = !empty($msg['attachment_mime_type']);
                                $imageHtml = '';
                                if ($hasImage) {
                                    $imageHtml = '<img src="../../back-end/contact-image-viewer.php?id=' . $msg['id'] . '" 
                                        alt="attachment" class="contact-image-thumbnail" 
                                        onclick="openImagePreview(' . $msg['id'] . ')">';
                                } else {
                                    $imageHtml = '-';
                                }

                                $createdAt = new DateTime($msg['created_at']);
                                $formattedDate = $createdAt->format('d.m.Y H:i');

                                echo '<tr>';
                                // VULNERABILITY: XSS - No htmlspecialchars() on user input
                                echo '<td>' . $msg['full_name'] . '</td>';
                                echo '<td>' . $msg['email'] . '</td>';
                                echo '<td>' . $msg['subject'] . '</td>';
                                // Truncate message for table display
                                $preview = isset($msg['message']) ? $msg['message'] : '';
                                $preview = trim($preview);
                                if (strlen($preview) > 150) {
                                    $short = substr($preview, 0, 150) . '...';
                                } else {
                                    $short = $preview;
                                }
                                echo '<td>' . nl2br($short) . '</td>';
                                echo '<td>' . $imageHtml . '</td>';
                                echo '<td>' . $msg['status'] . '</td>';
                                echo '<td>' . $formattedDate . '</td>';
                                echo '<td>';
                                echo '<form method="POST" style="display: inline;" onsubmit="return confirm(\'Ești sigur că vrei să ștergi acest mesaj?\');">';
                                echo '<input type="hidden" name="action" value="delete_message">';
                                echo '<input type="hidden" name="message_id" value="' . $msg['id'] . '">';
                                echo csrfInputField();
                                echo '<button type="submit" class="btn-delete">Șterge</button>';
                                echo '</form>';
                                echo '</td>';
                                echo '</tr>';
                            endforeach;
                        else:
                            echo '<tr><td colspan="8" style="text-align: center;">Nu sunt mesaje</td></tr>';
                        endif;
                        ?>
                    </tbody>
                </table>
            </div>
        <?php endif; ?>
    </main>

    <!-- Image Preview Modal -->
    <div id="imagePreviewModal" class="image-preview-modal">
        <div class="image-preview-content">
            <span class="close-modal" onclick="closeImagePreview()">&times;</span>
            <img id="previewImage" src="" alt="Previzualizare">
        </div>
    </div>

    <footer>
        <p>© 2026 Hotel Victoria</p>
    </footer>

    <script>
        function openImagePreview(messageId) {
            const modal = document.getElementById('imagePreviewModal');
            const img = document.getElementById('previewImage');
            img.src = '../../back-end/contact-image-viewer.php?id=' + messageId;
            modal.style.display = 'block';
        }

        function closeImagePreview() {
            const modal = document.getElementById('imagePreviewModal');
            modal.style.display = 'none';
        }

        // Close modal when clicking outside the content
        window.onclick = function(event) {
            const modal = document.getElementById('imagePreviewModal');
            if (event.target === modal) {
                modal.style.display = 'none';
            }
        }
    </script>
</body>

</html>
