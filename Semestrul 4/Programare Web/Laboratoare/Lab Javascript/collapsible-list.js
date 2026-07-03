// Initializes collapsible list interactions after DOM load.
document.addEventListener('DOMContentLoaded', function () {
    const rootList = document.querySelector('[data-collapsible-list]');

    if (!rootList) {
        return;
    }

    // Toggles a collapsible item and updates accessibility state.
    const toggleItem = function (item) {
        const toggle = item.querySelector(':scope > .collapsible-toggle');
        if (!toggle) {
            return;
        }

        const isExpanded = item.classList.contains('is-expanded');

        item.classList.toggle('is-expanded', !isExpanded);
        toggle.setAttribute('aria-expanded', String(!isExpanded));
    };

    // Handles mouse clicks for expanding and collapsing list items.
    rootList.addEventListener('click', function (event) {
        const item = event.target.closest('.collapsible-item');
        if (!item || !rootList.contains(item)) {
            return;
        }

        const directSublist = item.querySelector(':scope > .collapsible-sublist');
        if (!directSublist) {
            return;
        }

        if (directSublist.contains(event.target)) {
            return;
        }

        toggleItem(item);
    });

    // Handles keyboard toggling for accessible list interaction.
    rootList.addEventListener('keydown', function (event) {
        const toggle = event.target.closest('.collapsible-toggle');
        if (!toggle || !rootList.contains(toggle)) {
            return;
        }

        if (event.key === 'Enter' || event.key === ' ') {
            event.preventDefault();
            const item = toggle.closest('.collapsible-item');
            if (item) {
                toggleItem(item);
            }
        }
    });
});
