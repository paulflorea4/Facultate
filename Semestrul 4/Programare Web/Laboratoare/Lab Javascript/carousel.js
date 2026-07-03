// Initializes carousel behavior when the page is ready.
document.addEventListener('DOMContentLoaded', function () {
    const carousel = document.querySelector('[data-carousel]');
    const track = document.querySelector('[data-carousel-track]');
    const prevButton = document.querySelector('[data-carousel-prev]');
    const nextButton = document.querySelector('[data-carousel-next]');
    const slides = window.HotelCarouselSlides || [];

    if (!carousel || !track || slides.length < 4) {
        return;
    }

    let currentIndex = 1;
    let timerId = null;
    let isPaused = false;

    // Builds carousel slides including cloned edge slides for looping.
    function renderSlides() {
        track.innerHTML = '';

        const lastSlide = slides[slides.length - 1];
        const firstSlide = slides[0];

        // Creates and appends each slide element in display order.
        [lastSlide].concat(slides, [firstSlide]).forEach(function (slide, index) {
            const slideLink = document.createElement('a');
            slideLink.className = 'carousel-slide';
            slideLink.href = slide.link;
            slideLink.style.backgroundImage = 'linear-gradient(180deg, rgba(19, 35, 57, 0.15), rgba(19, 35, 57, 0.72)), url("' + slide.image + '")';
            slideLink.setAttribute('aria-label', slide.text);

            const overlay = document.createElement('div');
            overlay.className = 'carousel-overlay';

            const caption = document.createElement('p');
            caption.className = 'carousel-caption';
            caption.textContent = slide.text;

            const cta = document.createElement('span');
            cta.className = 'carousel-cta';
            cta.textContent = 'Află mai mult';

            overlay.appendChild(caption);
            overlay.appendChild(cta);
            slideLink.appendChild(overlay);
            track.appendChild(slideLink);

            slideLink.dataset.index = String(index);
        });
    }

    // Applies the current slide offset to the track.
    function updateCarousel() {
        const offset = -currentIndex * 100;
        track.style.transform = 'translateX(' + offset + '%)';
    }

    // Sets the current slide index with optional transition animation.
    function setCarouselPosition(index, animate) {
        const previousTransition = track.style.transition;
        currentIndex = index;
        track.style.transition = animate ? previousTransition || '' : 'none';
        updateCarousel();
        track.offsetHeight;
        if (!animate) {
            track.style.transition = previousTransition || '';
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
    track.addEventListener('transitionend', function () {
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
    prevButton.addEventListener('click', function () {
        showPrev();
        restartAutoPlayTemporarily();
    });

    // Handles next button clicks.
    nextButton.addEventListener('click', function () {
        showNext();
        restartAutoPlayTemporarily();
    });

    // Pauses autoplay while the carousel is hovered.
    carousel.addEventListener('mouseenter', function () {
        isPaused = true;
    });

    // Resumes autoplay when the mouse leaves the carousel.
    carousel.addEventListener('mouseleave', function () {
        isPaused = false;
    });

    renderSlides();
    track.style.transition = 'transform 0.6s ease';
    setCarouselPosition(1, false);
    updateCarousel();
    startAutoPlay();
});
