// Validation Helper Functions

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
    element.classList.add('is-invalid');
    element.classList.remove('is-valid');
}

// Marks a form field as valid.
function markAsValid(element) {
    element.classList.remove('is-invalid');
    element.classList.add('is-valid');
}

// Clears validation styles and previous inline errors.
function clearValidationStyles(form) {
    const inputs = form.querySelectorAll('input, textarea, select, .form-group');
    inputs.forEach(input => {
        input.classList.remove('is-invalid', 'is-valid');
    });

    form.querySelectorAll('.error-message').forEach(error => error.remove());
}

// Determines the error anchor element for displaying error messages
function getErrorAnchor(element) {
    if (element.classList && element.classList.contains('form-group')) {
        return element;
    }

    const label = element.closest ? element.closest('label') : null;
    if (label && label !== element) {
        return label;
    }

    return element;
}

// Displays an inline error message near a field.
function showErrorMessage(element, message) {
    const anchor = getErrorAnchor(element);
    const existingError = anchor.nextElementSibling;
    if (existingError && existingError.classList && existingError.classList.contains('error-message')) {
        existingError.remove();
    }
    
    const errorDiv = document.createElement('span');
    errorDiv.className = 'error-message';
    errorDiv.textContent = message;

    if (anchor.classList && anchor.classList.contains('form-group')) {
        anchor.insertAdjacentElement('afterend', errorDiv);
    } else if (anchor.tagName === 'LABEL') {
        anchor.insertAdjacentElement('afterend', errorDiv);
    } else {
        anchor.insertAdjacentElement('afterend', errorDiv);
    }
}

// Removes the inline error message for a field.
function clearErrorMessage(element) {
    const anchor = getErrorAnchor(element);
    const existingError = anchor.nextElementSibling;
    if (existingError && existingError.classList && existingError.classList.contains('error-message')) {
        existingError.remove();
    }
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
    const ageInput = document.getElementById('age');
    const birthdateInput = document.getElementById('birthdate');

    if (!ageInput || !birthdateInput) {
        return;
    }

    const ageValue = parseInt(ageInput.value, 10);
    const today = new Date();

    if (!Number.isNaN(ageValue) && ageValue > 0) {
        const maxBirthdate = new Date(today.getFullYear() - ageValue, today.getMonth(), today.getDate());
        const minBirthdate = new Date(today.getFullYear() - 120, today.getMonth(), today.getDate());
        birthdateInput.max = formatDateForInput(maxBirthdate);
        birthdateInput.min = formatDateForInput(minBirthdate);
    } else {
        birthdateInput.removeAttribute('max');
        birthdateInput.removeAttribute('min');
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
    const ageInput = document.getElementById('age');
    const birthdateInput = document.getElementById('birthdate');

    if (!ageInput || !birthdateInput) {
        return;
    }

    const calculatedAge = calculateAgeFromBirthdate(birthdateInput.value);
    if (calculatedAge) {
        ageInput.value = calculatedAge;
        markAsValid(ageInput);
        clearErrorMessage(ageInput);
        updateBirthdateConstraints();
    }
}

// Updates available room types based on guest count and syncs facilities
function updateRoomTypeOptions() {
    const data = getDependencyData();
    const guestCountInput = document.getElementById('guest_count');
    const roomTypeInputs = Array.from(document.querySelectorAll('input[name="room_type"]'));

    if (!guestCountInput || roomTypeInputs.length === 0) {
        return;
    }

    const allowedRoomTypes = data.roomTypesByGuestCount[guestCountInput.value] || Object.keys(data.roomLabels);

    roomTypeInputs.forEach(input => {
        const label = input.closest('label');
        input.disabled = !allowedRoomTypes.includes(input.value);
        if (label) {
            label.style.opacity = input.disabled ? '0.45' : '1';
            label.style.cursor = input.disabled ? 'not-allowed' : 'pointer';
        }
        if (input.disabled && input.checked) {
            input.checked = false;
        }
    });

    const checkedRoomType = roomTypeInputs.find(input => input.checked && !input.disabled);
    if (!checkedRoomType) {
        const firstAvailable = roomTypeInputs.find(input => !input.disabled);
        if (firstAvailable) {
            firstAvailable.checked = true;
        }
    }

    syncRequestedFacilitiesWithRoomType();
}

// Auto-checks facility checkboxes matching the selected room type
function syncRequestedFacilitiesWithRoomType() {
    const data = getDependencyData();
    const selectedRoomType = document.querySelector('input[name="room_type"]:checked');
    const facilityInputs = Array.from(document.querySelectorAll('input[name="requested_facilities"]'));

    if (!selectedRoomType || facilityInputs.length === 0) {
        return;
    }

    const roomFacilitiesMap = data.roomFacilitiesByRoomType || {};
    const selectedFacilities = roomFacilitiesMap[selectedRoomType.value] || [];

    facilityInputs.forEach(input => {
        input.checked = selectedFacilities.includes(input.value);
    });
}


// Initializes all field dependencies and attaches event listeners
function initializeFieldDependencies() {
    updateBirthdateConstraints();
    updateRoomTypeOptions();

    const ageInput = document.getElementById('age');
    const birthdateInput = document.getElementById('birthdate');
    const guestCountInput = document.getElementById('guest_count');
    const roomTypeInputs = Array.from(document.querySelectorAll('input[name="room_type"]'));

    if (ageInput) {
        ageInput.addEventListener('input', function() {
            updateBirthdateConstraints();
        });
    }

    if (birthdateInput) {
        birthdateInput.addEventListener('input', function() {
            syncAgeFromBirthdate();
        });

        birthdateInput.addEventListener('change', function() {
            syncAgeFromBirthdate();
        });
    }

    if (guestCountInput) {
        guestCountInput.addEventListener('change', function() {
            updateRoomTypeOptions();
        });
    }

    if (roomTypeInputs.length > 0) {
        roomTypeInputs.forEach(function (input) {
            input.addEventListener('change', function() {
                syncRequestedFacilitiesWithRoomType();
            });
        });
    }
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

// Randomly shuffles array elements using Fisher-Yates algorithm
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
    let overlay = document.getElementById('robotChallengeOverlay');
    if (overlay) {
        return overlay;
    }

    overlay = document.createElement('div');
    overlay.id = 'robotChallengeOverlay';
    overlay.className = 'robot-challenge-overlay';
    overlay.innerHTML = [
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
    ].join('');

    document.body.appendChild(overlay);

    const grid = overlay.querySelector('[data-robot-grid]');
    const refreshButton = overlay.querySelector('[data-robot-refresh]');
    const checkButton = overlay.querySelector('[data-robot-check]');

    grid.addEventListener('click', function (event) {
        const tileButton = event.target.closest('.robot-challenge-tile');
        if (!tileButton) {
            return;
        }
        tileButton.classList.toggle('is-selected');
    });

    refreshButton.addEventListener('click', function () {
        renderRobotChallenge(overlay);
    });

    checkButton.addEventListener('click', function () {
        if (!robotChallengeState.challenge) {
            return;
        }

        const selectedTiles = Array.from(overlay.querySelectorAll('.robot-challenge-tile.is-selected'));
        const selectedIds = selectedTiles.map(tile => tile.getAttribute('data-id'));
        const status = overlay.querySelector('[data-robot-status]');
        const expected = robotChallengeState.challenge.requiredCount;
        const correctSelections = selectedIds.filter(id => id === robotChallengeState.challenge.targetId).length;
        const wrongSelections = selectedIds.some(id => id !== robotChallengeState.challenge.targetId);

        if (!wrongSelections && correctSelections === expected && selectedIds.length === expected) {
            status.textContent = 'Perfect. Verificare reusita.';
            status.classList.remove('is-error');
            status.classList.add('is-success');
            closeRobotChallenge(overlay, true);
            return;
        }

        status.textContent = 'Nu este corect. Selecteaza exact toate imaginile cerute.';
        status.classList.remove('is-success');
        status.classList.add('is-error');
    });

    return overlay;
}

// Renders the challenge tiles and instructions in the robot challenge modal
function renderRobotChallenge(overlay) {
    const challenge = createRobotChallengeData();
    robotChallengeState.challenge = challenge;

    const instruction = overlay.querySelector('[data-robot-instruction]');
    const grid = overlay.querySelector('[data-robot-grid]');
    const status = overlay.querySelector('[data-robot-status]');

    instruction.textContent = `Selecteaza toate imaginile cu: ${challenge.targetLabel}`;
    status.textContent = `Trebuie sa gasesti ${challenge.requiredCount} imagini.`;
    status.classList.remove('is-error', 'is-success');

    grid.innerHTML = '';

    challenge.tiles.forEach(function (tile) {
        const tileButton = document.createElement('button');
        tileButton.type = 'button';
        tileButton.className = 'robot-challenge-tile';
        tileButton.setAttribute('data-id', tile.id);
        tileButton.innerHTML = `<span class="robot-challenge-image" style="background: linear-gradient(135deg, ${tile.colorA}, ${tile.colorB});">${tile.code}</span>`;
        grid.appendChild(tileButton);
    });
}

// Closes the robot challenge modal and resolves the promise with pass/fail result
function closeRobotChallenge(overlay, passed) {
    overlay.classList.remove('show');
    if (robotChallengeState.resolver) {
        const resolver = robotChallengeState.resolver;
        robotChallengeState.resolver = null;
        resolver(passed);
    }
}

// Displays robot challenge modal and returns promise that resolves when challenge completes
function runRobotChallenge() {
    const overlay = getRobotChallengeModal();
    overlay.classList.add('show');
    renderRobotChallenge(overlay);

    return new Promise(function (resolve) {
        robotChallengeState.resolver = resolve;
    });
}

// Displays error message at the top of the form
function showFormError(form, message) {
    let errorDiv = form.querySelector('.form-error');
    if (!errorDiv) {
        errorDiv = document.createElement('div');
        errorDiv.className = 'form-error';
        form.insertBefore(errorDiv, form.firstChild);
    }

    errorDiv.textContent = message;
    errorDiv.classList.add('show');

    const successDiv = form.querySelector('.form-success');
    if (successDiv) {
        successDiv.classList.remove('show');
    }
}

// Removes both success and error messages from the form
function clearFormFeedback(form) {
    const successDiv = form.querySelector('.form-success');
    const errorDiv = form.querySelector('.form-error');

    if (successDiv) {
        successDiv.classList.remove('show');
    }

    if (errorDiv) {
        errorDiv.classList.remove('show');
    }
}

// ====== RESERVATION FORM VALIDATION ======
// Validates all reservation form fields and runs robot challenge verification
async function validateReservationForm(event) {
    event.preventDefault();
    
    clearValidationStyles(event.target);
    let isValid = true;
    
    const form = event.target;
    clearFormFeedback(form);
    
    // Get form elements
    const lastName = document.getElementById('last_name');
    const firstName = document.getElementById('first_name');
    const age = document.getElementById('age');
    const gender = document.querySelector('input[name="gender"]:checked');
    const email = document.getElementById('email');
    const phone = document.getElementById('phone');
    const birthdate = document.getElementById('birthdate');
    const checkIn = document.getElementById('check_in');
    const checkOut = document.getElementById('check_out');
    const guestCount = document.getElementById('guest_count');
    const roomType = document.querySelector('input[name="room_type"]:checked');
    const confirmPassword = document.getElementById('confirm_password');
    const notes = document.getElementById('notes');
    const terms = document.getElementById('terms');
    
    // Validate Nume
    if (!lastName.value.trim()) {
        markAsInvalid(lastName);
        showErrorMessage(lastName, 'Numele este obligatoriu');
        isValid = false;
    } else if (lastName.value.trim().length < 2) {
        markAsInvalid(lastName);
        showErrorMessage(lastName, 'Numele trebuie să aibă cel puțin 2 caractere');
        isValid = false;
    } else if (lastName.value.trim().match(/[1-9!@#$%^&*()]/)) {
        markAsInvalid(lastName);
        showErrorMessage(lastName, 'Numele nu poate conține numere sau caractere speciale');
        isValid = false;
    } else {
        markAsValid(lastName);
        clearErrorMessage(lastName);
    }
    
    // Validate Prenume
    if (!firstName.value.trim()) {
        markAsInvalid(firstName);
        showErrorMessage(firstName, 'Prenumele este obligatoriu');
        isValid = false;
    } else if (firstName.value.trim().length < 2) {
        markAsInvalid(firstName);
        showErrorMessage(firstName, 'Prenumele trebuie să aibă cel puțin 2 caractere');
        isValid = false;
    }else if (firstName.value.trim().match(/[1-9!@#$%^&*()]/)) {
        markAsInvalid(firstName);
        showErrorMessage(firstName, 'Prenumele nu poate conține numere sau caractere speciale');
        isValid = false;
    } else {
        markAsValid(firstName);
        clearErrorMessage(firstName);
    }
    
    // Validate Age
    if (!age.value) {
        markAsInvalid(age);
        showErrorMessage(age, 'Vârsta este obligatorie');
        isValid = false;
    } else if (parseInt(age.value) < 18 || parseInt(age.value) > 100) {
        markAsInvalid(age);
        showErrorMessage(age, 'Vârsta trebuie să fie între 18 și 100 ani');
        isValid = false;
    } else {
        markAsValid(age);
        clearErrorMessage(age);
    }
    
    // Validate Gender
    if (!gender) {
        const genderGroup = document.querySelector('input[name="gender"]').closest('.form-group');
        markAsInvalid(genderGroup);
        showErrorMessage(genderGroup, 'Genul este obligatoriu');
        isValid = false;
    } else {
        const genderGroup = document.querySelector('input[name="gender"]').closest('.form-group');
        markAsValid(genderGroup);
        clearErrorMessage(genderGroup);
    }
    
    // Validate Email
    if (!email.value.trim()) {
        markAsInvalid(email);
        showErrorMessage(email, 'Email-ul este obligatoriu');
        isValid = false;
    } else if (!isValidEmail(email.value)) {
        markAsInvalid(email);
        showErrorMessage(email, 'Email-ul nu este valid (ex: user@example.com)');
        isValid = false;
    } else {
        markAsValid(email);
        clearErrorMessage(email);
    }
    
    // Validate Telefon (optional but if provided must be valid)
    if (phone.value.trim() && !isValidPhone(phone.value)) {
        markAsInvalid(phone);
        showErrorMessage(phone, 'Telefonul nu este valid (format: 07xx xxx xxx sau +40xxx xxx xxx)');
        isValid = false;
    } else if (phone.value.trim()) {
        markAsValid(phone);
        clearErrorMessage(phone);
    }

    if (birthdate.value) {
        const birthDateValue = new Date(birthdate.value);
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        const maxBirthDate = new Date(today.getFullYear() - parseInt(age.value, 10), today.getMonth(), today.getDate());
        const minBirthDate = new Date(today.getFullYear() - 120, today.getMonth(), today.getDate());
        if (birthDateValue > maxBirthDate || birthDateValue < minBirthDate) {
            markAsInvalid(birthdate);
            showErrorMessage(birthdate, 'Data nașterii trebuie să corespundă vârstei introduse');
            isValid = false;
        } else {
            markAsValid(birthdate);
            clearErrorMessage(birthdate);
        }
    }
    
    // Validate Check-in
    if (!checkIn.value) {
        markAsInvalid(checkIn);
        showErrorMessage(checkIn, 'Data check-in este obligatorie');
        isValid = false;
    } else {
        const checkinDate = new Date(checkIn.value);
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        if (checkinDate < today) {
            markAsInvalid(checkIn);
            showErrorMessage(checkIn, 'Data check-in nu poate fi în trecut');
            isValid = false;
        } else {
            markAsValid(checkIn);
            clearErrorMessage(checkIn);
        }
    }
    
    // Validate Check-out
    if (!checkOut.value) {
        markAsInvalid(checkOut);
        showErrorMessage(checkOut, 'Data check-out este obligatorie');
        isValid = false;
    } else if (checkIn.value) {
        const checkinDate = new Date(checkIn.value);
        const checkoutDate = new Date(checkOut.value);
        if (checkoutDate <= checkinDate) {
            markAsInvalid(checkOut);
            showErrorMessage(checkOut, 'Data check-out trebuie să fie după check-in');
            isValid = false;
        } else {
            markAsValid(checkOut);
            clearErrorMessage(checkOut);
        }
    }
    
    // Validate Number of Guests
    if (!guestCount.value) {
        markAsInvalid(guestCount);
        showErrorMessage(guestCount, 'Selectați numărul de persoane');
        isValid = false;
    } else {
        markAsValid(guestCount);
        clearErrorMessage(guestCount);
    }
    
    // Validate Room Type
    if (!roomType) {
        const cameraGroup = document.querySelector('input[name="room_type"]').closest('.form-group');
        markAsInvalid(cameraGroup);
        showErrorMessage(cameraGroup, 'Selectați un tip de cameră');
        isValid = false;
    } else {
        const cameraGroup = document.querySelector('input[name="room_type"]').closest('.form-group');
        markAsValid(cameraGroup);
        clearErrorMessage(cameraGroup);

        const allowedRoomTypes = getDependencyData().roomTypesByGuestCount[guestCount.value] || Object.keys(getDependencyData().roomLabels);
        if (guestCount.value && !allowedRoomTypes.includes(roomType.value)) {
            markAsInvalid(cameraGroup);
            showErrorMessage(cameraGroup, 'Tipul de cameră nu este disponibil pentru numărul de persoane selectat');
            isValid = false;
        }
    }
    
    // Validate Password
    if (!confirmPassword.value) {
        markAsInvalid(confirmPassword);
        showErrorMessage(confirmPassword, 'Parola de confirmare este obligatorie');
        isValid = false;
    } else if (confirmPassword.value.length < 6) {
        markAsInvalid(confirmPassword);
        showErrorMessage(confirmPassword, 'Parola trebuie să aibă cel puțin 6 caractere');
        isValid = false;
    } else {
        markAsValid(confirmPassword);
        clearErrorMessage(confirmPassword);
    }
    
    // Validate Terms
    if (!terms.checked) {
        markAsInvalid(terms);
        showErrorMessage(terms, 'Trebuie să acceptați termenii și condițiile');
        isValid = false;
    } else {
        markAsValid(terms);
        clearErrorMessage(terms);
    }
    
    // If form is valid, show success message
    if (isValid) {
        const robotPassed = await runRobotChallenge();
        if (!robotPassed) {
            showFormError(form, 'Verificarea robot nu a fost finalizata corect. Incearca din nou.');
            return false;
        }

        showFormSuccess(event.target, 'Rezervare trimisă cu succes!');
        window.scrollTo({ top: 0, behavior: 'smooth' });
        setTimeout(() => {
            event.target.reset();
            clearValidationStyles(event.target);
        }, 2000);
    }
    
    return false;
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
    const contactFullName = document.getElementById('contact_full_name');
    const email = document.getElementById('contact_email');
    const contactPhone = document.getElementById('contact_phone');
    const contactSubject = document.getElementById('contact_subject');
    const contactCategory = document.querySelector('input[name="contact_category"]:checked');
    const contactMessage = document.getElementById('contact_message');
    const contactFollowupDate = document.getElementById('contact_followup_date');
    const policy = document.querySelector('input[name="privacy_policy"]');
    
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
        const categoryGroup = document.querySelector('input[name="contact_category"]').closest('.form-group');
        markAsInvalid(categoryGroup);
        showErrorMessage(categoryGroup, 'Selectați o categorie');
        isValid = false;
    } else {
        const categoryGroup = document.querySelector('input[name="contact_category"]').closest('.form-group');
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
    const username = document.getElementById('admin_username');
    const password = document.getElementById('admin_password');
    const email = document.getElementById('admin_email');
    const reservationReference = document.getElementById('reservation_reference');
    const guestName = document.getElementById('guest_name');
    const reservationPhone = document.getElementById('reservation_phone');
    const reservationRoomType = document.getElementById('reservation_room_type');
    const reservationStatus = document.getElementById('reservation_status');
    const checkInUpdate = document.getElementById('check_in_update');
    const checkOutUpdate = document.getElementById('check_out_update');
    const paymentStatus = document.getElementById('payment_status');
    const terms = document.querySelector('input[name="ownership_confirmation"]');
    const gdpr = document.querySelector('input[name="privacy_compliance"]');
    
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
    let successDiv = form.querySelector('.form-success');
    if (!successDiv) {
        successDiv = document.createElement('div');
        successDiv.className = 'form-success';
        form.insertBefore(successDiv, form.firstChild);
    }
    successDiv.textContent = message;
    successDiv.classList.add('show');

    const errorDiv = form.querySelector('.form-error');
    if (errorDiv) {
        errorDiv.classList.remove('show');
    }
}

// Attaches validation handlers when the page loads.
document.addEventListener('DOMContentLoaded', function() {
    initializeFieldDependencies();

    const reservationForm = document.getElementById('reservationForm');
    const contactForm = document.getElementById('contactForm');
    const adminForm = document.getElementById('hotelAdminForm');
    
    if (reservationForm) {
        reservationForm.addEventListener('submit', validateReservationForm);
        reservationForm.addEventListener('reset', function() {
            setTimeout(function() {
                updateBirthdateConstraints();
                updateRoomTypeOptions();
            }, 0);
        });
    }
    
    if (contactForm) {
        contactForm.addEventListener('submit', validateContactForm);
    }
    
    if (adminForm) {
        adminForm.addEventListener('submit', validateAdminForm);
    }
});
