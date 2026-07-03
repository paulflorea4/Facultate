<?php
require_once '../../back-end/config.php';
require_once '../../back-end/functions.php';
require_once '../../back-end/nav-helper.php';

requireLogin();

// CSRF protection for reservation form
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $csrfToken = isset($_POST['csrf_token']) ? $_POST['csrf_token'] : '';
    if (!verifyCsrfToken($csrfToken)) {
        $fieldErrors['form'] = 'Cerere invalidă (CSRF).';
    }
}

function reservationRoomLabel(array $room)
{
    $roomTypeLabels = [
        'single' => 'Single',
        'double' => 'Double',
        'twin' => 'Double Twin',
        'standard' => 'Double Standard',
        'apartament' => 'Apartament',
        'apartment' => 'Apartment',
        'deluxe' => 'Apartament Deluxe'
    ];

    $parts = [];
    if (!empty($room['room_number'])) {
        $parts[] = $room['room_number'];
    }

    $roomType = isset($room['room_type']) ? (string) $room['room_type'] : '';
    $parts[] = $roomTypeLabels[$roomType] ?? ucfirst($roomType ?: 'Cameră');

    if (!empty($room['capacity'])) {
        $parts[] = $room['capacity'] . ' pers.';
    }

    if (!empty($room['price_per_night'])) {
        $parts[] = $room['price_per_night'] . ' RON/noapte';
    }

    return implode(' - ', $parts);
}

$rooms = getRooms();
$fieldValues = [
    'room_id' => '',
    'check_in' => '',
    'check_out' => '',
    'guest_count' => '',
    'special_requests' => ''
];
$fieldErrors = [];
$successMessage = '';

if (isset($_SESSION['reservation_success_message'])) {
    $successMessage = $_SESSION['reservation_success_message'];
    unset($_SESSION['reservation_success_message']);
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $fieldValues['room_id'] = isset($_POST['room_id']) ? trim((string) $_POST['room_id']) : '';
    $fieldValues['check_in'] = isset($_POST['check_in']) ? trim((string) $_POST['check_in']) : '';
    $fieldValues['check_out'] = isset($_POST['check_out']) ? trim((string) $_POST['check_out']) : '';
    $fieldValues['guest_count'] = isset($_POST['guest_count']) ? trim((string) $_POST['guest_count']) : '';
    $fieldValues['special_requests'] = isset($_POST['special_requests']) ? trim((string) $_POST['special_requests']) : '';

    $roomId = filter_var($fieldValues['room_id'], FILTER_VALIDATE_INT, ['options' => ['min_range' => 1]]);
    $guestCount = filter_var($fieldValues['guest_count'], FILTER_VALIDATE_INT, ['options' => ['min_range' => 1, 'max_range' => 5]]);
    $checkIn = $fieldValues['check_in'];
    $checkOut = $fieldValues['check_out'];
    $today = new DateTime('today');
    $checkInDate = DateTime::createFromFormat('Y-m-d', $checkIn);
    $checkOutDate = DateTime::createFromFormat('Y-m-d', $checkOut);

    if (!$roomId) {
        $fieldErrors['room_id'] = 'Selectați camera.';
    }

    if (!$guestCount) {
        $fieldErrors['guest_count'] = 'Selectați numărul de persoane.';
    }

    if (!$checkInDate || $checkInDate->format('Y-m-d') !== $checkIn) {
        $fieldErrors['check_in'] = 'Data check-in nu este validă.';
    } elseif ($checkInDate < $today) {
        $fieldErrors['check_in'] = 'Data check-in nu poate fi în trecut.';
    }

    if (!$checkOutDate || $checkOutDate->format('Y-m-d') !== $checkOut) {
        $fieldErrors['check_out'] = 'Data check-out nu este validă.';
    } elseif ($checkInDate && $checkOutDate <= $checkInDate) {
        $fieldErrors['check_out'] = 'Data check-out trebuie să fie după check-in.';
    }

    if (!$fieldErrors) {
        // Server-side capacity validation before attempting to create reservation
        $selectedRoom = getRoomById($roomId);
        if ($selectedRoom && isset($selectedRoom['capacity'])) {
            $roomCapacity = (int) $selectedRoom['capacity'];
            if ($guestCount > $roomCapacity) {
                $fieldErrors['guest_count'] = 'Numărul de persoane depășește capacitatea camerei selectate.';
            }
        }

        if (!$fieldErrors) {
            $reservationResult = createReservation(
                $roomId,
                $checkIn,
                $checkOut,
                $guestCount,
                $fieldValues['special_requests'],
                isset($_SESSION['user_id']) ? $_SESSION['user_id'] : null
            );

            if ($reservationResult['success']) {
                $_SESSION['reservation_success_message'] = $reservationResult['message'];
                header('Location: reservation.php');
                exit();
            }

            $fieldErrors['form'] = $reservationResult['message'];
        }
    }
}

function reservationFieldClass($fieldName, array $fieldErrors)
{
    return isset($fieldErrors[$fieldName]) ? ' is-invalid' : '';
}

function reservationFieldValue(array $fieldValues, $fieldName)
{
    return isset($fieldValues[$fieldName]) ? $fieldValues[$fieldName] : '';
}
?>
<!DOCTYPE html>
<html lang="ro">

<head>
    <meta charset="UTF-8">
    <title>Rezervare</title>
    <link rel="stylesheet" href="../Styles/style-responsive.css">
</head>

<body>
    <header>
        <h1>Rezervare</h1>
    </header>
    <?php echo getNavigation(); ?>

    <main>
        <p class="reservation-intro">Completați formularul pentru rezervare.</p>

        <?php if (!empty($successMessage)): ?>
            <div class="form-success show"><?php echo htmlspecialchars($successMessage, ENT_QUOTES, 'UTF-8'); ?></div>
        <?php endif; ?>

        <?php if (!empty($fieldErrors['form'])): ?>
            <div class="form-error show"><?php echo htmlspecialchars($fieldErrors['form'], ENT_QUOTES, 'UTF-8'); ?></div>
        <?php endif; ?>

        <form id="reservationForm" method="post" action="reservation.php" novalidate>
            <?php echo csrfInputField(); ?>
            <fieldset>
                <legend>Detalii sejur</legend>
                <label for="room_id">Cameră <span class="required">*</span>:</label>
                <select id="room_id" name="room_id" class="<?php echo reservationFieldClass('room_id', $fieldErrors); ?>" required>
                    <option value="">-- Selectați camera --</option>
                    <?php if (!empty($rooms)): ?>
                        <?php foreach ($rooms as $room): ?>
                            <option value="<?php echo htmlspecialchars((string) $room['id'], ENT_QUOTES, 'UTF-8'); ?>" data-capacity="<?php echo htmlspecialchars((string) ($room['capacity'] ?? ''), ENT_QUOTES, 'UTF-8'); ?>" <?php echo reservationFieldValue($fieldValues, 'room_id') === (string) $room['id'] ? 'selected' : ''; ?>>
                                <?php echo htmlspecialchars(reservationRoomLabel($room), ENT_QUOTES, 'UTF-8'); ?>
                            </option>
                        <?php endforeach; ?>
                    <?php endif; ?>
                </select><br>
                <?php if (!empty($fieldErrors['room_id'])): ?>
                    <span class="error-message"><?php echo htmlspecialchars($fieldErrors['room_id'], ENT_QUOTES, 'UTF-8'); ?></span>
                <?php endif; ?>

                <label for="check_in">Check-in <span class="required">*</span>:</label>
                <input type="date" id="check_in" name="check_in" class="<?php echo reservationFieldClass('check_in', $fieldErrors); ?>" value="<?php echo htmlspecialchars(reservationFieldValue($fieldValues, 'check_in'), ENT_QUOTES, 'UTF-8'); ?>" required><br>
                <?php if (!empty($fieldErrors['check_in'])): ?>
                    <span class="error-message"><?php echo htmlspecialchars($fieldErrors['check_in'], ENT_QUOTES, 'UTF-8'); ?></span>
                <?php endif; ?>

                <label for="check_out">Check-out <span class="required">*</span>:</label>
                <input type="date" id="check_out" name="check_out" class="<?php echo reservationFieldClass('check_out', $fieldErrors); ?>" value="<?php echo htmlspecialchars(reservationFieldValue($fieldValues, 'check_out'), ENT_QUOTES, 'UTF-8'); ?>" required><br>
                <?php if (!empty($fieldErrors['check_out'])): ?>
                    <span class="error-message"><?php echo htmlspecialchars($fieldErrors['check_out'], ENT_QUOTES, 'UTF-8'); ?></span>
                <?php endif; ?>

                <label for="guest_count">Număr de persoane <span class="required">*</span>:</label>
                <select id="guest_count" name="guest_count" class="<?php echo reservationFieldClass('guest_count', $fieldErrors); ?>" required>
                    <option value="">-- Selectați --</option>
                    <option value="1" <?php echo reservationFieldValue($fieldValues, 'guest_count') === '1' ? 'selected' : ''; ?>>1 persoană</option>
                    <option value="2" <?php echo reservationFieldValue($fieldValues, 'guest_count') === '2' ? 'selected' : ''; ?>>2 persoane</option>
                    <option value="3" <?php echo reservationFieldValue($fieldValues, 'guest_count') === '3' ? 'selected' : ''; ?>>3 persoane</option>
                    <option value="4" <?php echo reservationFieldValue($fieldValues, 'guest_count') === '4' ? 'selected' : ''; ?>>4 persoane</option>
                    <option value="5" <?php echo reservationFieldValue($fieldValues, 'guest_count') === '5' ? 'selected' : ''; ?>>5+ persoane</option>
                </select><br>
                <?php if (!empty($fieldErrors['guest_count'])): ?>
                    <span class="error-message"><?php echo htmlspecialchars($fieldErrors['guest_count'], ENT_QUOTES, 'UTF-8'); ?></span>
                <?php endif; ?>

                <label for="special_requests">Solicitări speciale:</label>
                <textarea id="special_requests" name="special_requests" rows="4" placeholder="Ex: cameră la etaj superior, check-in după ora 18:00"><?php echo htmlspecialchars(reservationFieldValue($fieldValues, 'special_requests'), ENT_QUOTES, 'UTF-8'); ?></textarea><br>
            </fieldset>

            <button type="submit">Trimite rezervarea</button>
            <button type="reset">Șterge formularul</button>
        </form>

    </main>

    <footer>
        <p>© 2026 Hotel Victoria</p>
    </footer>
    <script>
        (function () {
            function toInt(v) {
                var n = parseInt(v, 10);
                return Number.isNaN(n) ? null : n;
            }

            var roomSelect = document.getElementById('room_id');
            var guestSelect = document.getElementById('guest_count');

            if (roomSelect && guestSelect) {
                function updateGuestOptions() {
                    var opt = roomSelect.selectedOptions && roomSelect.selectedOptions[0];
                    var cap = opt ? toInt(opt.getAttribute('data-capacity')) : null;

                    // Iterate guest options and disable those exceeding capacity
                    Array.prototype.forEach.call(guestSelect.options, function (o) {
                        if (!o.value) return; // skip placeholder
                        var val = toInt(o.value);
                        if (val === null) {
                            o.disabled = false;
                            return;
                        }

                        if (cap === null) {
                            o.disabled = false;
                        } else {
                            o.disabled = val > cap;
                        }
                    });

                    // If currently selected option is disabled, adjust selection
                    if (guestSelect.selectedOptions && guestSelect.selectedOptions[0] && guestSelect.selectedOptions[0].disabled) {
                        // pick the highest allowed value
                        var chosen = null;
                        for (var i = guestSelect.options.length - 1; i >= 0; i--) {
                            var oo = guestSelect.options[i];
                            if (!oo.value) continue;
                            if (!oo.disabled) { chosen = oo; break; }
                        }
                        if (chosen) {
                            guestSelect.value = chosen.value;
                        } else {
                            guestSelect.value = '';
                        }
                    }
                }

                roomSelect.addEventListener('change', updateGuestOptions);
                // initialize on load
                updateGuestOptions();
            }
        }());
    </script>
</body>

</html>
