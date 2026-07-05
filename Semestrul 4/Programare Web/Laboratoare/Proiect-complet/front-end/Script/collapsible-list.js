(function ($) {
    $(function () {
        const $rootList = $('[data-collapsible-list]');

        if (!$rootList.length) {
            return;
        }

        // Toggles a collapsible item and updates accessibility state.
        const toggleItem = function ($item) {
            const $toggle = $item.children('.collapsible-toggle').first();
            if (!$toggle.length) {
                return;
            }

            const isExpanded = $item.toggleClass('is-expanded').hasClass('is-expanded');
            $toggle.attr('aria-expanded', isExpanded);
        };

        // Handles mouse clicks for expanding and collapsing list items.
        $rootList.on('click', '.collapsible-item', function (event) {
            const $item = $(this);
            const $target = $(event.target);
            const $directSublist = $item.children('.collapsible-sublist').first();

            if (!$directSublist.length) {
                return;
            }

            if ($target.closest('.collapsible-sublist').is($directSublist)) {
                return;
            }

            toggleItem($item);
        });

        // Handles keyboard toggling for accessible list interaction.
        $rootList.on('keydown', '.collapsible-toggle', function (event) {
            if (event.key === 'Enter' || event.key === ' ') {
                event.preventDefault();
                const $item = $(this).closest('.collapsible-item');
                if ($item.length) {
                    toggleItem($item);
                }
            }
        });
    });
}(jQuery));