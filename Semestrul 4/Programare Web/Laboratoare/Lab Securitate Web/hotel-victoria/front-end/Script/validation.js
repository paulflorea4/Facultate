// Validation Helper Functions

function asJQuery(element) {
    if (!element) {
        return $();
    }
    return element.jquery ? element : $(element);
}

// Validates email address format.
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

// Validates Romanian phone number format.
function isValidPhone(phone) {
    const phoneRegex = /^(\+40|0)[0-9]{9,10}$/;
    return phoneRegex.test(phone.replace(/\s/g, ''));
}

// Marks a form field as invalid.
function markAsInvalid(element) {
    const $element = asJQuery(element);
    $element.addClass('is-invalid').removeClass('is-valid');
}

// Marks a form field as valid.
function markAsValid(element) {
    const $element = asJQuery(element);
    $element.removeClass('is-invalid').addClass('is-valid');
}

// Clears validation styles and previous inline errors.
function clearValidationStyles(form) {
    const $form = asJQuery(form);
    $form.find('input, textarea, select, .form-group').removeClass('is-invalid is-valid');
    $form.find('.error-message').remove();
}

// Determines the error anchor element for displaying error messages
function getErrorAnchor(element) {
    const $element = asJQuery(element).first();
    if (!$element.length) {
        return $();
    }

    if ($element.hasClass('form-group')) {
        return $element;
    }

    const $label = $element.closest('label');
    if ($label.length && $label[0] !== $element[0]) {
        return $label;
    }

    return $element;
}

// Displays an inline error message near a field.
function showErrorMessage(element, message) {
    const $anchor = getErrorAnchor(element);
    if (!$anchor.length) {
        return;
    }

    const $existingError = $anchor.next('.error-message').first();
    if ($existingError.length) {
        $existingError.remove();
    }

    $('<span>', {
        class: 'error-message',
        text: message
    }).insertAfter($anchor);
}

// Removes the inline error message for a field.
function clearErrorMessage(element) {
    const $anchor = getErrorAnchor(element);
    if (!$anchor.length) {
        return;
    }

    $anchor.next('.error-message').first().remove();
}

// Returns dependency data or empty default object if not available
function getDependencyData() {
    return window.HotelFormDependencies || {
        roomTypesByGuestCount: {},
        hotelAreasByLocation: {},
        roomLabels: {},
        roomFacilitiesByRoomType: {}
    };
}

// Formats a Date object to YYYY-MM-DD string format
function formatDateForInput(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}

// Updates birthdate input min/max constraints based on age value
function updateBirthdateConstraints() {
    const $ageInput = $('#age');
    const $birthdateInput = $('#birthdate');

    if (!$ageInput.length || !$birthdateInput.length) {
        return;
    }

    const ageValue = parseInt($ageInput.val(), 10);
    const today = new Date();

    if (!Number.isNaN(ageValue) && ageValue > 0) {
        const maxBirthdate = new Date(today.getFullYear() - ageValue, today.getMonth(), today.getDate());
        const minBirthdate = new Date(today.getFullYear() - 120, today.getMonth(), today.getDate());
        $birthdateInput.attr('max', formatDateForInput(maxBirthdate));
        $birthdateInput.attr('min', formatDateForInput(minBirthdate));
    } else {
        $birthdateInput.removeAttr('max min');
    }
}

// Calculates age from birthdate and returns as string or empty string if invalid
function calculateAgeFromBirthdate(birthdateValue) {
    if (!birthdateValue) {
        return '';
    }

    const birthDate = new Date(birthdateValue);
    if (Number.isNaN(birthDate.getTime())) {
        return '';
    }

    const today = new Date();
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDifference = today.getMonth() - birthDate.getMonth();

    if (monthDifference < 0 || (monthDifference === 0 && today.getDate() < birthDate.getDate())) {
        age -= 1;
    }

    return age > 0 ? String(age) : '';
}

// Syncs age field from birthdate input value
function syncAgeFromBirthdate() {
    const $ageInput = $('#age');
    const $birthdateInput = $('#birthdate');

    if (!$ageInput.length || !$birthdateInput.length) {
        return;
    }

    const calculatedAge = calculateAgeFromBirthdate($birthdateInput.val());
    if (calculatedAge) {
        $ageInput.val(calculatedAge);
        markAsValid($ageInput);
        clearErrorMessage($ageInput);
        updateBirthdateConstraints();
    }
}

// Updates available room types based on guest count and syncs facilities
function updateRoomTypeOptions() {
    const data = getDependencyData();
    const $guestCountInput = $('#guest_count');
    const $roomTypeInputs = $('input[name="room_type"]');

    if (!$guestCountInput.length || !$roomTypeInputs.length) {
        return;
    }

    const allowedRoomTypes = data.roomTypesByGuestCount[$guestCountInput.val()] || Object.keys(data.roomLabels);

    $roomTypeInputs.each(function () {
        const $input = $(this);
        const $label = $input.closest('label');
        const isDisabled = !allowedRoomTypes.includes($input.val());

        $input.prop('disabled', isDisabled);
        if ($label.length) {
            $label.css({
                opacity: isDisabled ? '0.45' : '1',
                cursor: isDisabled ? 'not-allowed' : 'pointer'
            });
        }

        if (isDisabled && $input.is(':checked')) {
            $input.prop('checked', false);
        }
    });

    const $checkedRoomType = $roomTypeInputs.filter(':checked:not(:disabled)').first();
    if (!$checkedRoomType.length) {
        const $firstAvailable = $roomTypeInputs.filter(':not(:disabled)').first();
        if ($firstAvailable.length) {
            $firstAvailable.prop('checked', true);
        }
    }

    syncRequestedFacilitiesWithRoomType();
}

// Auto-checks facility checkboxes matching the selected room type
function syncRequestedFacilitiesWithRoomType() {
    const data = getDependencyData();
    const $selectedRoomType = $('input[name="room_type"]:checked').first();
    const $facilityInputs = $('input[name="requested_facilities"]');

    if (!$selectedRoomType.length || !$facilityInputs.length) {
        return;
    }

    const roomFacilitiesMap = data.roomFacilitiesByRoomType || {};
    const selectedFacilities = roomFacilitiesMap[$selectedRoomType.val()] || [];

    $facilityInputs.each(function () {
        const $input = $(this);
        $input.prop('checked', selectedFacilities.includes($input.val()));
    });
}


// Initializes all field dependencies and attaches event listeners
function initializeFieldDependencies() {
    updateBirthdateConstraints();
    updateRoomTypeOptions();

    const $ageInput = $('#age');
    const $birthdateInput = $('#birthdate');
    const $guestCountInput = $('#guest_count');
    const $roomTypeInputs = $('input[name="room_type"]');

    $ageInput.on('input', function () {
        updateBirthdateConstraints();
    });

    $birthdateInput.on('input change', function () {
        syncAgeFromBirthdate();
    });

    $guestCountInput.on('change', function () {
        updateRoomTypeOptions();
    });

    $roomTypeInputs.on('change', function () {
        syncRequestedFacilitiesWithRoomType();
    });
}

const ROBOT_CHALLENGE_LIBRARY = [
    { id: 'spa', label: 'Spa', code: 'SPA', colorA: '#dcecff', colorB: '#a9cfff' },
    { id: 'pool', label: 'Piscina', code: 'POOL', colorA: '#d6f6ff', colorB: '#8ad7ff' },
    { id: 'breakfast', label: 'Mic dejun', code: 'FOOD', colorA: '#fff1d9', colorB: '#ffd28f' },
    { id: 'parking', label: 'Parcare', code: 'PARK', colorA: '#e8ecf3', colorB: '#bcc8da' },
    { id: 'wifi', label: 'WiFi', code: 'WIFI', colorA: '#e6fce7', colorB: '#97e8a2' },
    { id: 'gym', label: 'Fitness', code: 'GYM', colorA: '#f3e8ff', colorB: '#d2b3ff' },
    { id: 'sauna', label: 'Sauna', code: 'HEAT', colorA: '#ffe8d7', colorB: '#ffbe8e' },
    { id: 'conference', label: 'Conferinta', code: 'MEET', colorA: '#f0f5ff', colorB: '#c8d7ff' },
    { id: 'transfer', label: 'Transfer', code: 'RIDE', colorA: '#e9fff8', colorB: '#adf0d5' },
    { id: 'view', label: 'Panorama', code: 'VIEW', colorA: '#ebf8ff', colorB: '#bce7ff' },
    { id: 'laundry', label: 'Spalatorie', code: 'CLEAN', colorA: '#eef3ff', colorB: '#bfceff' },
    { id: 'pets', label: 'Pet Friendly', code: 'PETS', colorA: '#fff0f0', colorB: '#ffc3c3' }
];

const robotChallengeState = {
    resolver: null,
    challenge: null
};

// Randomly shuffles array elements
function shuffleArray(values) {
    const result = values.slice();
    for (let i = result.length - 1; i > 0; i -= 1) {
        const randomIndex = Math.floor(Math.random() * (i + 1));
        const temp = result[i];
        result[i] = result[randomIndex];
        result[randomIndex] = temp;
    }
    return result;
}

// Creates randomized robot challenge data with target and distractor tiles
function createRobotChallengeData() {
    const target = ROBOT_CHALLENGE_LIBRARY[Math.floor(Math.random() * ROBOT_CHALLENGE_LIBRARY.length)];
    const requiredCount = 2 + Math.floor(Math.random() * 2);
    const distractors = shuffleArray(ROBOT_CHALLENGE_LIBRARY.filter(item => item.id !== target.id)).slice(0, 9 - requiredCount);
    const tiles = [];

    for (let i = 0; i < requiredCount; i += 1) {
        tiles.push(target);
    }

    distractors.forEach(function (item) {
        tiles.push(item);
    });

    return {
        targetId: target.id,
        targetLabel: target.label,
        requiredCount,
        tiles: shuffleArray(tiles)
    };
}

// Creates or retrieves the robot challenge modal overlay element
function getRobotChallengeModal() {
    const $existingOverlay = $('#robotChallengeOverlay');
    if ($existingOverlay.length) {
        return $existingOverlay;
    }

    const $overlay = $('<div>', {
        id: 'robotChallengeOverlay',
        class: 'robot-challenge-overlay'
    }).html([
        '<div class="robot-challenge-dialog" role="dialog" aria-modal="true" aria-labelledby="robotChallengeTitle">',
        '  <h3 id="robotChallengeTitle">Verificare robot</h3>',
        '  <p class="robot-challenge-text" data-robot-instruction></p>',
        '  <div class="robot-challenge-grid" data-robot-grid></div>',
        '  <p class="robot-challenge-status" data-robot-status></p>',
        '  <div class="robot-challenge-actions">',
        '    <button type="button" class="robot-refresh" data-robot-refresh>Schimba imaginile</button>',
        '    <button type="button" class="robot-check" data-robot-check>Verifica</button>',
        '  </div>',
        '</div>'
    ].join(''));

    $('body').append($overlay);

    $overlay.on('click', '[data-robot-grid] .robot-challenge-tile', function () {
        $(this).toggleClass('is-selected');
    });

    $overlay.on('click', '[data-robot-refresh]', function () {
        renderRobotChallenge($overlay);
    });

    $overlay.on('click', '[data-robot-check]', function () {
        if (!robotChallengeState.challenge) {
            return;
        }

        const selectedIds = $overlay.find('.robot-challenge-tile.is-selected').map(function () {
            return $(this).attr('data-id');
        }).get();

        const $status = $overlay.find('[data-robot-status]');
        const expected = robotChallengeState.challenge.requiredCount;
        const correctSelections = selectedIds.filter(id => id === robotChallengeState.challenge.targetId).length;
        const wrongSelections = selectedIds.some(id => id !== robotChallengeState.challenge.targetId);

        if (!wrongSelections && correctSelections === expected && selectedIds.length === expected) {
            $status.text('Perfect. Verificare reusita.').removeClass('is-error').addClass('is-success');
            closeRobotChallenge($overlay, true);
            return;
        }

        $status.text('Nu este corect. Selecteaza exact toate imaginile cerute.').removeClass('is-success').addClass('is-error');
    });

    return $overlay;
}

// Renders the challenge tiles and instructions in the robot challenge modal
function renderRobotChallenge(overlay) {
    const $overlay = asJQuery(overlay);
    const challenge = createRobotChallengeData();
    robotChallengeState.challenge = challenge;

    const $instruction = $overlay.find('[data-robot-instruction]');
    const $grid = $overlay.find('[data-robot-grid]');
    const $status = $overlay.find('[data-robot-status]');

    $instruction.text(`Selecteaza toate imaginile cu: ${challenge.targetLabel}`);
    $status.text(`Trebuie sa gasesti ${challenge.requiredCount} imagini.`).removeClass('is-error is-success');

    $grid.empty();

    challenge.tiles.forEach(function (tile) {
        const $tileButton = $('<button>', {
            type: 'button',
            class: 'robot-challenge-tile',
            'data-id': tile.id
        }).html(`<span class="robot-challenge-image" style="background: linear-gradient(135deg, ${tile.colorA}, ${tile.colorB});">${tile.code}</span>`);

        $grid.append($tileButton);
    });
}

// Closes the robot challenge modal and resolves the promise with pass/fail result
function closeRobotChallenge(overlay, passed) {
    asJQuery(overlay).removeClass('show');
    if (robotChallengeState.resolver) {
        const resolver = robotChallengeState.resolver;
        robotChallengeState.resolver = null;
        resolver(passed);
    }
}

// Displays robot challenge modal and returns promise that resolves when challenge completes
function runRobotChallenge() {
    const $overlay = getRobotChallengeModal();
    $overlay.addClass('show');
    renderRobotChallenge($overlay);

    return new Promise(function (resolve) {
        robotChallengeState.resolver = resolve;
    });
}

// Displays error message at the top of the form
function showFormError(form, message) {
    const $form = asJQuery(form);
    let $errorDiv = $form.children('.form-error').first();

    if (!$errorDiv.length) {
        $errorDiv = $('<div>', { class: 'form-error' });
        $form.prepend($errorDiv);
    }

    $errorDiv.text(message).addClass('show');

    $form.children('.form-success').removeClass('show');
}

// Removes both success and error messages from the form
function clearFormFeedback(form) {
    const $form = asJQuery(form);
    $form.children('.form-success, .form-error').removeClass('show');
}

// ====== CONTACT FORM VALIDATION ======
// Validates all contact form fields and runs robot challenge verification
async function validateContactForm(event) {
    event.preventDefault();
    
    clearValidationStyles(event.target);
    let isValid = true;
    
    const form = event.target;
    clearFormFeedback(form);
    
    // Get form elements
    const contactFullName = $('#contact_full_name')[0];
    const email = $('#contact_email')[0];
    const contactPhone = $('#contact_phone')[0];
    const contactSubject = $('#contact_subject')[0];
    const contactCategory = $('input[name="contact_category"]:checked')[0];
    const contactMessage = $('#contact_message')[0];
    const contactFollowupDate = $('#contact_followup_date')[0];
    const policy = $('input[name="privacy_policy"]')[0];
    
    // Validate Name
    if (!contactFullName.value.trim()) {
        markAsInvalid(contactFullName);
        showErrorMessage(contactFullName, 'Numele este obligatoriu');
        isValid = false;
    } else if (contactFullName.value.trim().length < 3) {
        markAsInvalid(contactFullName);
        showErrorMessage(contactFullName, 'Numele trebuie să aibă cel puțin 3 caractere');
        isValid = false;
    } else {
        markAsValid(contactFullName);
        clearErrorMessage(contactFullName);
    }
    
    // Validate Email
    if (!email.value.trim()) {
        markAsInvalid(email);
        showErrorMessage(email, 'Email-ul este obligatoriu');
        isValid = false;
    } else if (!isValidEmail(email.value)) {
        markAsInvalid(email);
        showErrorMessage(email, 'Email-ul nu este valid');
        isValid = false;
    } else {
        markAsValid(email);
        clearErrorMessage(email);
    }
    
    // Validate Phone
    if (!contactPhone.value.trim()) {
        markAsInvalid(contactPhone);
        showErrorMessage(contactPhone, 'Telefonul este obligatoriu');
        isValid = false;
    } else if (!isValidPhone(contactPhone.value)) {
        markAsInvalid(contactPhone);
        showErrorMessage(contactPhone, 'Telefonul nu este valid');
        isValid = false;
    } else {
        markAsValid(contactPhone);
        clearErrorMessage(contactPhone);
    }
    
    // Validate Subject
    if (!contactSubject.value) {
        markAsInvalid(contactSubject);
        showErrorMessage(contactSubject, 'Selectați subiectul mesajului');
        isValid = false;
    } else {
        markAsValid(contactSubject);
        clearErrorMessage(contactSubject);
    }
    
    // Validate Category
    if (!contactCategory) {
        const categoryGroup = $('input[name="contact_category"]').first().closest('.form-group')[0];
        markAsInvalid(categoryGroup);
        showErrorMessage(categoryGroup, 'Selectați o categorie');
        isValid = false;
    } else {
        const categoryGroup = $('input[name="contact_category"]').first().closest('.form-group')[0];
        markAsValid(categoryGroup);
        clearErrorMessage(categoryGroup);
    }
    
    // Validate Message
    if (!contactMessage.value.trim()) {
        markAsInvalid(contactMessage);
        showErrorMessage(contactMessage, 'Mesajul este obligatoriu');
        isValid = false;
    } else if (contactMessage.value.trim().length < 10) {
        markAsInvalid(contactMessage);
        showErrorMessage(contactMessage, 'Mesajul trebuie să aibă cel puțin 10 caractere');
        isValid = false;
    } else {
        markAsValid(contactMessage);
        clearErrorMessage(contactMessage);
    }
    
    // Validate Response Date
    if (!contactFollowupDate.value) {
        markAsInvalid(contactFollowupDate);
        showErrorMessage(contactFollowupDate, 'Selectați o dată de contact');
        isValid = false;
    } else {
        const responseDate = new Date(contactFollowupDate.value);
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        if (responseDate < today) {
            markAsInvalid(contactFollowupDate);
            showErrorMessage(contactFollowupDate, 'Data trebuie să fie în viitor');
            isValid = false;
        } else {
            markAsValid(contactFollowupDate);
            clearErrorMessage(contactFollowupDate);
        }
    }
    
    // Validate Privacy Policy
    if (!policy.checked) {
        markAsInvalid(policy);
        showErrorMessage(policy, 'Trebuie să acceptați politica de confidențialitate');
        isValid = false;
    } else {
        markAsValid(policy);
        clearErrorMessage(policy);
    }
    
    if (isValid) {
        const robotPassed = await runRobotChallenge();
        if (!robotPassed) {
            showFormError(form, 'Verificarea robot nu a fost finalizata corect. Incearca din nou.');
            return false;
        }

        showFormSuccess(event.target, 'Mesaj trimis cu succes! Vă vom contacta în curând.');
        setTimeout(() => {
            event.target.reset();
            clearValidationStyles(event.target);
        }, 2000);
    }
    
    return false;
}

// ====== HOTEL ADMIN FORM VALIDATION ======
// Validates all admin/reservation management form fields and runs robot challenge verification
async function validateAdminForm(event) {
    event.preventDefault();
    
    clearValidationStyles(event.target);
    let isValid = true;
    
    const form = event.target;
    clearFormFeedback(form);
    
    // Get form elements
    const username = $('#admin_username')[0];
    const password = $('#admin_password')[0];
    const email = $('#admin_email')[0];
    const reservationReference = $('#reservation_reference')[0];
    const guestName = $('#guest_name')[0];
    const reservationPhone = $('#reservation_phone')[0];
    const reservationRoomType = $('#reservation_room_type')[0];
    const reservationStatus = $('#reservation_status')[0];
    const checkInUpdate = $('#check_in_update')[0];
    const checkOutUpdate = $('#check_out_update')[0];
    const paymentStatus = $('#payment_status')[0];
    const terms = $('input[name="ownership_confirmation"]')[0];
    const gdpr = $('input[name="privacy_compliance"]')[0];
    
    // Validate Username
    if (!username.value.trim()) {
        markAsInvalid(username);
        showErrorMessage(username, 'Utilizatorul este obligatoriu');
        isValid = false;
    } else if (username.value.trim().length < 4) {
        markAsInvalid(username);
        showErrorMessage(username, 'Utilizatorul trebuie să aibă cel puțin 4 caractere');
        isValid = false;
    } else if (!/^[a-zA-Z0-9_]+$/.test(username.value)) {
        markAsInvalid(username);
        showErrorMessage(username, 'Utilizatorul poate conține doar litere, cifre și underscore');
        isValid = false;
    } else {
        markAsValid(username);
        clearErrorMessage(username);
    }
    
    // Validate Password
    if (!password.value) {
        markAsInvalid(password);
        showErrorMessage(password, 'Parola este obligatorie');
        isValid = false;
    } else if (password.value.length < 8) {
        markAsInvalid(password);
        showErrorMessage(password, 'Parola trebuie să aibă cel puțin 8 caractere');
        isValid = false;
    } else if (!/(?=.*[a-z])(?=.*[A-Z])(?=.*\d)/.test(password.value)) {
        markAsInvalid(password);
        showErrorMessage(password, 'Parola trebuie să conțină litere mici, litere mari și cifre');
        isValid = false;
    } else {
        markAsValid(password);
        clearErrorMessage(password);
    }
    
    // Validate Email
    if (!email.value.trim()) {
        markAsInvalid(email);
        showErrorMessage(email, 'Email-ul este obligatoriu');
        isValid = false;
    } else if (!isValidEmail(email.value)) {
        markAsInvalid(email);
        showErrorMessage(email, 'Email-ul nu este valid');
        isValid = false;
    } else {
        markAsValid(email);
        clearErrorMessage(email);
    }

    // Validate Reservation Code
    if (!reservationReference.value.trim()) {
        markAsInvalid(reservationReference);
        showErrorMessage(reservationReference, 'Codul rezervării este obligatoriu');
        isValid = false;
    } else if (!/^HV-\d{4}-\d{4,6}$/.test(reservationReference.value.trim())) {
        markAsInvalid(reservationReference);
        showErrorMessage(reservationReference, 'Cod invalid (ex: HV-2026-0142)');
        isValid = false;
    } else {
        markAsValid(reservationReference);
        clearErrorMessage(reservationReference);
    }

    // Validate Guest Name
    if (!guestName.value.trim()) {
        markAsInvalid(guestName);
        showErrorMessage(guestName, 'Numele clientului este obligatoriu');
        isValid = false;
    } else if (guestName.value.trim().length < 3) {
        markAsInvalid(guestName);
        showErrorMessage(guestName, 'Numele clientului trebuie să aibă cel puțin 3 caractere');
        isValid = false;
    } else {
        markAsValid(guestName);
        clearErrorMessage(guestName);
    }

    // Validate Phone (optional)
    if (reservationPhone.value.trim() && !isValidPhone(reservationPhone.value)) {
        markAsInvalid(reservationPhone);
        showErrorMessage(reservationPhone, 'Telefonul clientului nu este valid');
        isValid = false;
    } else {
        markAsValid(reservationPhone);
        clearErrorMessage(reservationPhone);
    }

    // Validate Room Type
    if (!reservationRoomType.value) {
        markAsInvalid(reservationRoomType);
        showErrorMessage(reservationRoomType, 'Tipul de cameră este obligatoriu');
        isValid = false;
    } else {
        markAsValid(reservationRoomType);
        clearErrorMessage(reservationRoomType);
    }

    // Validate Reservation Status
    if (!reservationStatus.value) {
        markAsInvalid(reservationStatus);
        showErrorMessage(reservationStatus, 'Statusul rezervării este obligatoriu');
        isValid = false;
    } else {
        markAsValid(reservationStatus);
        clearErrorMessage(reservationStatus);
    }

    // Validate Check-in/Check-out
    if (!checkInUpdate.value) {
        markAsInvalid(checkInUpdate);
        showErrorMessage(checkInUpdate, 'Data de check-in este obligatorie');
        isValid = false;
    } else {
        markAsValid(checkInUpdate);
        clearErrorMessage(checkInUpdate);
    }

    if (!checkOutUpdate.value) {
        markAsInvalid(checkOutUpdate);
        showErrorMessage(checkOutUpdate, 'Data de check-out este obligatorie');
        isValid = false;
    } else if (checkInUpdate.value) {
        const checkInDate = new Date(checkInUpdate.value);
        const checkOutDate = new Date(checkOutUpdate.value);
        if (checkOutDate <= checkInDate) {
            markAsInvalid(checkOutUpdate);
            showErrorMessage(checkOutUpdate, 'Check-out trebuie să fie după check-in');
            isValid = false;
        } else {
            markAsValid(checkOutUpdate);
            clearErrorMessage(checkOutUpdate);
        }
    } else {
        markAsValid(checkOutUpdate);
        clearErrorMessage(checkOutUpdate);
    }

    // Validate Payment Status
    if (!paymentStatus.value) {
        markAsInvalid(paymentStatus);
        showErrorMessage(paymentStatus, 'Statusul plății este obligatoriu');
        isValid = false;
    } else {
        markAsValid(paymentStatus);
        clearErrorMessage(paymentStatus);
    }
    
    // Validate Terms
    if (!terms.checked) {
        markAsInvalid(terms);
        showErrorMessage(terms, 'Trebuie să confirmi că dețini autoritate');
        isValid = false;
    } else {
        markAsValid(terms);
        clearErrorMessage(terms);
    }
    
    // Validate GDPR
    if (!gdpr.checked) {
        markAsInvalid(gdpr);
        showErrorMessage(gdpr, 'Trebuie să accepți GDPR');
        isValid = false;
    } else {
        markAsValid(gdpr);
        clearErrorMessage(gdpr);
    }
    
    if (isValid) {
        const robotPassed = await runRobotChallenge();
        if (!robotPassed) {
            showFormError(form, 'Verificarea robot nu a fost finalizata corect. Incearca din nou.');
            return false;
        }

        showFormSuccess(event.target, 'Actualizarea rezervării a fost salvată cu succes!');
        setTimeout(() => {
            event.target.reset();
            clearValidationStyles(event.target);
        }, 2000);
    }
    
    return false;
}

// Displays the form success message.
function showFormSuccess(form, message) {
    const $form = asJQuery(form);
    let $successDiv = $form.children('.form-success').first();

    if (!$successDiv.length) {
        $successDiv = $('<div>', { class: 'form-success' });
        $form.prepend($successDiv);
    }

    $successDiv.text(message).addClass('show');

    $form.children('.form-error').removeClass('show');
}

// Attaches validation handlers when the page loads.
$(function () {
    initializeFieldDependencies();

    const $contactForm = $('#contactForm');
    const $adminForm = $('#hotelAdminForm');

    if ($contactForm.length) {
        $contactForm.on('submit', validateContactForm);
    }

    if ($adminForm.length) {
        $adminForm.on('submit', validateAdminForm);
    }
});
