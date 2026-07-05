<?php
require_once '../../back-end/config.php';
require_once '../../back-end/functions.php';
require_once '../../back-end/nav-helper.php';

requireLogin();



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
    $token = isset($_POST['csrf_token']) ? $_POST['csrf_token'] : '';
    if (!verifyCsrfToken($token)) {
        $fieldErrors['form'] = 'Cerere invalidă (CSRF)';
    } else {
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
    <style>
        .edit-reservation-section {
            margin-top: 40px;
            padding-top: 24px;
            border-top: 2px solid #e0e8f0;
        }

        .edit-reservation-section h2 {
            margin-bottom: 4px;
            color: #1f3a52;
            width: min(860px, 100%);
            margin-left: auto;
            margin-right: auto;
        }

        .edit-reservation-section > p {
            color: #57606a;
            margin-bottom: 16px;
            font-size: 0.93rem;
            width: min(860px, 100%);
            margin-left: auto;
            margin-right: auto;
        }

        .edit-status {
            padding: 10px 14px;
            border-radius: 6px;
            margin: 12px 0;
            font-weight: 600;
            display: none;
        }

        .edit-status.success {
            display: block;
            background-color: #d5f4e6;
            border: 2px solid #27ae60;
            color: #1e7e34;
        }

        .edit-status.error {
            display: block;
            background-color: #fadbd8;
            border: 2px solid #e74c3c;
            color: #a93226;
        }
    </style>
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

        <?php if (!isAdmin()): ?>
        <!-- ==================== CERINȚA 5: Editare cu AJAX vanilla ==================== -->
        <section class="edit-reservation-section">
            <h2>Cerința 5 — Editare rezervare cu AJAX vanilla</h2>
            <p>Selectați o rezervare din listă pentru a o edita. Salvarea se face prin <code>XMLHttpRequest</code>.</p>
            <form id="req5-form" onsubmit="return false;">
                <fieldset>
                    <legend>Selectare rezervare</legend>
                    <label for="req5-select">Rezervarea:</label>
                    <select id="req5-select" style="width:100%;max-width:100%">
                        <option value="">-- Se încarcă... --</option>
                    </select>
                </fieldset>
                <fieldset id="req5-fields" style="display:none">
                    <legend>Detalii rezervare</legend>
                    <label for="req5-room">Cameră <span class="required">*</span>:</label>
                    <select id="req5-room">
                        <option value="">-- Selectați camera --</option>
                        <?php foreach ($rooms as $room): ?>
                            <option value="<?php echo (int)$room['id']; ?>" data-capacity="<?php echo (int)($room['capacity'] ?? 0); ?>">
                                <?php echo htmlspecialchars(reservationRoomLabel($room), ENT_QUOTES, 'UTF-8'); ?>
                            </option>
                        <?php endforeach; ?>
                    </select><br>

                    <label for="req5-checkin">Check-in <span class="required">*</span>:</label>
                    <input type="date" id="req5-checkin"><br>

                    <label for="req5-checkout">Check-out <span class="required">*</span>:</label>
                    <input type="date" id="req5-checkout"><br>

                    <label for="req5-guests">Număr de persoane <span class="required">*</span>:</label>
                    <select id="req5-guests">
                        <option value="">-- Selectați --</option>
                        <option value="1">1 persoană</option>
                        <option value="2">2 persoane</option>
                        <option value="3">3 persoane</option>
                        <option value="4">4 persoane</option>
                        <option value="5">5+ persoane</option>
                    </select><br>

                    <label for="req5-requests">Solicitări speciale:</label>
                    <textarea id="req5-requests" rows="3" placeholder="Ex: cameră la etaj superior"></textarea><br>
                </fieldset>
                <div id="req5-status" class="edit-status"></div>
                <button id="req5-save" type="button" disabled>Salvează modificările</button>
            </form>
        </section>

        <!-- ==================== CERINȚA 6: Editare cu jQuery ==================== -->
        <section class="edit-reservation-section">
            <h2>Cerința 6 — Editare rezervare cu jQuery</h2>
            <p>Selectați o rezervare din listă pentru a o edita. Salvarea se face prin <code>$.ajax()</code>.</p>
            <form id="req6-form" onsubmit="return false;">
                <fieldset>
                    <legend>Selectare rezervare</legend>
                    <label for="req6-select">Rezervarea:</label>
                    <select id="req6-select" style="width:100%;max-width:100%">
                        <option value="">-- Se încarcă... --</option>
                    </select>
                </fieldset>
                <fieldset id="req6-fields" style="display:none">
                    <legend>Detalii rezervare</legend>
                    <label for="req6-room">Cameră <span class="required">*</span>:</label>
                    <select id="req6-room">
                        <option value="">-- Selectați camera --</option>
                        <?php foreach ($rooms as $room): ?>
                            <option value="<?php echo (int)$room['id']; ?>" data-capacity="<?php echo (int)($room['capacity'] ?? 0); ?>">
                                <?php echo htmlspecialchars(reservationRoomLabel($room), ENT_QUOTES, 'UTF-8'); ?>
                            </option>
                        <?php endforeach; ?>
                    </select><br>

                    <label for="req6-checkin">Check-in <span class="required">*</span>:</label>
                    <input type="date" id="req6-checkin"><br>

                    <label for="req6-checkout">Check-out <span class="required">*</span>:</label>
                    <input type="date" id="req6-checkout"><br>

                    <label for="req6-guests">Număr de persoane <span class="required">*</span>:</label>
                    <select id="req6-guests">
                        <option value="">-- Selectați --</option>
                        <option value="1">1 persoană</option>
                        <option value="2">2 persoane</option>
                        <option value="3">3 persoane</option>
                        <option value="4">4 persoane</option>
                        <option value="5">5+ persoane</option>
                    </select><br>

                    <label for="req6-requests">Solicitări speciale:</label>
                    <textarea id="req6-requests" rows="3" placeholder="Ex: cameră la etaj superior"></textarea><br>
                </fieldset>
                <div id="req6-status" class="edit-status"></div>
                <button id="req6-save" type="button" disabled>Salvează modificările</button>
            </form>
        </section>
        <?php endif; ?>

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
    <!-- ==================== CERINȚA 5: Vanilla AJAX Edit ==================== -->
    <script>
    (function() {
        var select = document.getElementById('req5-select');
        if (!select) return;

        var fieldsBlock = document.getElementById('req5-fields');
        var roomSelect  = document.getElementById('req5-room');
        var checkinInput = document.getElementById('req5-checkin');
        var checkoutInput = document.getElementById('req5-checkout');
        var guestsSelect = document.getElementById('req5-guests');
        var requestsArea = document.getElementById('req5-requests');
        var saveBtn = document.getElementById('req5-save');
        var statusDiv = document.getElementById('req5-status');

        var isDirty = false;
        var currentId = null;
        var originalValues = {};

        // Load user's reservations list
        function loadReservations() {
            var xhr = new XMLHttpRequest();
            xhr.open('GET', '../../back-end/api-reservations.php?action=get_user_reservations', true);
            xhr.onreadystatechange = function() {
                if (xhr.readyState !== 4 || xhr.status !== 200) return;
                var res = JSON.parse(xhr.responseText);
                var prev = select.value;
                select.innerHTML = '<option value="">-- Selectați rezervarea --</option>';
                for (var i = 0; i < res.data.length; i++) {
                    var r = res.data[i];
                    var opt = document.createElement('option');
                    opt.value = r.id;
                    opt.textContent = '#' + r.id + ' \u2013 Camera ' + (r.room_number || '?') + ' (' + r.check_in + ' \u2192 ' + r.check_out + ') \u2013 ' + r.status;
                    select.appendChild(opt);
                }
                if (prev) select.value = prev;
            };
            xhr.send();
        }

        // Load single reservation details
        function loadReservation(id) {
            var xhr = new XMLHttpRequest();
            xhr.open('GET', '../../back-end/api-reservations.php?action=get_reservation&id=' + id, true);
            xhr.onreadystatechange = function() {
                if (xhr.readyState !== 4 || xhr.status !== 200) return;
                var d = JSON.parse(xhr.responseText).data;
                roomSelect.value = d.room_id;
                checkinInput.value = d.check_in;
                checkoutInput.value = d.check_out;
                guestsSelect.value = d.guests;
                requestsArea.value = d.special_requests || '';
                originalValues = {
                    room_id: String(d.room_id),
                    check_in: d.check_in,
                    check_out: d.check_out,
                    guests: String(d.guests),
                    special_requests: d.special_requests || ''
                };
                fieldsBlock.style.display = '';
                isDirty = false;
                saveBtn.disabled = true;
                statusDiv.className = 'edit-status';
                statusDiv.textContent = '';
                currentId = id;
            };
            xhr.send();
        }

        function checkDirty() {
            if (!currentId) return;
            var changed =
                roomSelect.value !== originalValues.room_id ||
                checkinInput.value !== originalValues.check_in ||
                checkoutInput.value !== originalValues.check_out ||
                guestsSelect.value !== originalValues.guests ||
                requestsArea.value !== originalValues.special_requests;
            isDirty = changed;
            saveBtn.disabled = !changed;
        }

        select.addEventListener('change', function() {
            var newId = select.value;
            if (!newId) {
                fieldsBlock.style.display = 'none';
                currentId = null;
                isDirty = false;
                saveBtn.disabled = true;
                return;
            }
            if (isDirty) {
                if (!confirm('Aveți modificări nesalvate. Doriți să continuați fără a salva?')) {
                    select.value = currentId || '';
                    return;
                }
            }
            loadReservation(newId);
        });

        var fields = [roomSelect, checkinInput, checkoutInput, guestsSelect, requestsArea];
        for (var f = 0; f < fields.length; f++) {
            fields[f].addEventListener('input', checkDirty);
            fields[f].addEventListener('change', checkDirty);
        }

        saveBtn.addEventListener('click', function() {
            var payload = JSON.stringify({
                id: parseInt(currentId, 10),
                room_id: parseInt(roomSelect.value, 10),
                check_in: checkinInput.value,
                check_out: checkoutInput.value,
                guests: parseInt(guestsSelect.value, 10),
                special_requests: requestsArea.value
            });
            var xhr = new XMLHttpRequest();
            xhr.open('POST', '../../back-end/api-reservations.php?action=update_reservation', true);
            xhr.setRequestHeader('Content-Type', 'application/json');
            xhr.onreadystatechange = function() {
                if (xhr.readyState !== 4) return;
                var res = JSON.parse(xhr.responseText);
                if (xhr.status === 200 && res.success) {
                    statusDiv.textContent = res.message;
                    statusDiv.className = 'edit-status success';
                    isDirty = false;
                    saveBtn.disabled = true;
                    originalValues = {
                        room_id: roomSelect.value,
                        check_in: checkinInput.value,
                        check_out: checkoutInput.value,
                        guests: guestsSelect.value,
                        special_requests: requestsArea.value
                    };
                    loadReservations();
                } else {
                    statusDiv.textContent = res.error || 'Eroare la salvare';
                    statusDiv.className = 'edit-status error';
                }
            };
            xhr.send(payload);
        });

        loadReservations();
    })();
    </script>

    <!-- ==================== CERINȚA 6: jQuery Edit ==================== -->
    <script>
    (function() {
        var $select = $('#req6-select');
        if ($select.length === 0) return;

        var $fieldsBlock = $('#req6-fields');
        var $room     = $('#req6-room');
        var $checkin  = $('#req6-checkin');
        var $checkout = $('#req6-checkout');
        var $guests   = $('#req6-guests');
        var $requests = $('#req6-requests');
        var $saveBtn  = $('#req6-save');
        var $status   = $('#req6-status');

        var isDirty = false;
        var currentId = null;
        var originalValues = {};

        function loadReservations() {
            $.getJSON('../../back-end/api-reservations.php', { action: 'get_user_reservations' }, function(res) {
                var prev = $select.val();
                $select.html('<option value="">-- Selectați rezervarea --</option>');
                $.each(res.data, function(i, r) {
                    $select.append(
                        $('<option>').val(r.id).text('#' + r.id + ' \u2013 Camera ' + (r.room_number || '?') + ' (' + r.check_in + ' \u2192 ' + r.check_out + ') \u2013 ' + r.status)
                    );
                });
                if (prev) $select.val(prev);
            });
        }

        function loadReservation(id) {
            $.getJSON('../../back-end/api-reservations.php', { action: 'get_reservation', id: id }, function(res) {
                var d = res.data;
                $room.val(d.room_id);
                $checkin.val(d.check_in);
                $checkout.val(d.check_out);
                $guests.val(d.guests);
                $requests.val(d.special_requests || '');
                originalValues = {
                    room_id: String(d.room_id),
                    check_in: d.check_in,
                    check_out: d.check_out,
                    guests: String(d.guests),
                    special_requests: d.special_requests || ''
                };
                $fieldsBlock.show();
                isDirty = false;
                $saveBtn.prop('disabled', true);
                $status.attr('class', 'edit-status').text('');
                currentId = id;
            });
        }

        function checkDirty() {
            if (!currentId) return;
            var changed =
                $room.val() !== originalValues.room_id ||
                $checkin.val() !== originalValues.check_in ||
                $checkout.val() !== originalValues.check_out ||
                $guests.val() !== originalValues.guests ||
                $requests.val() !== originalValues.special_requests;
            isDirty = changed;
            $saveBtn.prop('disabled', !changed);
        }

        $select.on('change', function() {
            var newId = $select.val();
            if (!newId) {
                $fieldsBlock.hide();
                currentId = null;
                isDirty = false;
                $saveBtn.prop('disabled', true);
                return;
            }
            if (isDirty) {
                if (!confirm('Aveți modificări nesalvate. Doriți să continuați fără a salva?')) {
                    $select.val(currentId || '');
                    return;
                }
            }
            loadReservation(newId);
        });

        $room.add($checkin).add($checkout).add($guests).add($requests).on('input change', checkDirty);

        $saveBtn.on('click', function() {
            $.ajax({
                url: '../../back-end/api-reservations.php?action=update_reservation',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    id: parseInt(currentId, 10),
                    room_id: parseInt($room.val(), 10),
                    check_in: $checkin.val(),
                    check_out: $checkout.val(),
                    guests: parseInt($guests.val(), 10),
                    special_requests: $requests.val()
                }),
                success: function(res) {
                    $status.text(res.message).attr('class', 'edit-status success');
                    isDirty = false;
                    $saveBtn.prop('disabled', true);
                    originalValues = {
                        room_id: $room.val(),
                        check_in: $checkin.val(),
                        check_out: $checkout.val(),
                        guests: $guests.val(),
                        special_requests: $requests.val()
                    };
                    loadReservations();
                },
                error: function(xhr) {
                    var res = xhr.responseJSON || {};
                    $status.text(res.error || 'Eroare la salvare').attr('class', 'edit-status error');
                }
            });
        });

        loadReservations();
    })();
    </script>
</body>

</html>
