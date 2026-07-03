// Initializes carousel behavior when the page is ready.
(function ($) {
    $(function () {
        const $carousel = $('[data-carousel]');
        const $track = $('[data-carousel-track]');
        const $prevButton = $('[data-carousel-prev]');
        const $nextButton = $('[data-carousel-next]');
        const slides = window.HotelCarouselSlides || [];

        if (!$carousel.length || !$track.length || slides.length < 4) {
            return;
        }

        let currentIndex = 1;
        let timerId = null;
        let isPaused = false;

        // Builds carousel slides including cloned edge slides for looping.
        function renderSlides() {
            $track.empty();

            const lastSlide = slides[slides.length - 1];
            const firstSlide = slides[0];

            // Creates and appends each slide element in display order.
            $.each([lastSlide].concat(slides, [firstSlide]), function (index, slide) {
                const $slideLink = $('<a>', {
                    class: 'carousel-slide',
                    href: slide.link,
                    'aria-label': slide.text,
                    'data-index': String(index)
                }).css('background-image', 'linear-gradient(180deg, rgba(19, 35, 57, 0.15), rgba(19, 35, 57, 0.72)), url("' + slide.image + '")');

                const $overlay = $('<div>', { class: 'carousel-overlay' });
                const $caption = $('<p>', { class: 'carousel-caption', text: slide.text });
                const $cta = $('<span>', { class: 'carousel-cta', text: 'Află mai mult' });

                $overlay.append($caption, $cta);
                $slideLink.append($overlay);
                $track.append($slideLink);
            });
        }

        // Applies the current slide offset to the track.
        function updateCarousel() {
            const offset = -currentIndex * 100;
            $track.css('transform', 'translateX(' + offset + '%)');
        }

        // Sets the current slide index with optional transition animation.
        function setCarouselPosition(index, animate) {
            const previousTransition = $track.css('transition');
            currentIndex = index;
            $track.css('transition', animate ? (previousTransition || '') : 'none');
            updateCarousel();
            // Force reflow before restoring transition for boundary jump.
            $track[0].offsetHeight;
            if (!animate) {
                $track.css('transition', previousTransition || '');
            }
        }

        // Advances to the next slide.
        function showNext() {
            currentIndex += 1;
            updateCarousel();
        }

        // Moves to the previous slide.
        function showPrev() {
            currentIndex -= 1;
            updateCarousel();
        }

        // Fixes index position after transition on cloned boundary slides.
        $track.on('transitionend', function () {
            if (currentIndex === slides.length + 1) {
                setCarouselPosition(1, false);
            }

            if (currentIndex === 0) {
                setCarouselPosition(slides.length, false);
            }
        });

        // Starts automatic slide advancement.
        function startAutoPlay() {
            stopAutoPlay();
            // Advances slides on a fixed interval when autoplay is active.
            timerId = window.setInterval(function () {
                if (!isPaused) {
                    showNext();
                }
            }, 3000);
        }

        // Stops automatic slide advancement.
        function stopAutoPlay() {
            if (timerId) {
                window.clearInterval(timerId);
                timerId = null;
            }
        }

        // Temporarily pauses autoplay after manual navigation.
        function restartAutoPlayTemporarily() {
            isPaused = true;
            // Resumes autoplay state after a short cooldown.
            window.setTimeout(function () {
                isPaused = false;
            }, 3200);
        }

        // Handles previous button clicks.
        $prevButton.on('click', function () {
            showPrev();
            restartAutoPlayTemporarily();
        });

        // Handles next button clicks.
        $nextButton.on('click', function () {
            showNext();
            restartAutoPlayTemporarily();
        });

        // Pauses autoplay while the carousel is hovered.
        $carousel.on('mouseenter', function () {
            isPaused = true;
        });

        // Resumes autoplay when the mouse leaves the carousel.
        $carousel.on('mouseleave', function () {
            isPaused = false;
        });

        renderSlides();
        $track.css('transition', 'transform 0.6s ease');
        setCarouselPosition(1, false);
        updateCarousel();
        startAutoPlay();
    });
}(jQuery));
