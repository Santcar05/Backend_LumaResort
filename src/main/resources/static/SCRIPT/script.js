// ---------------------- Carrusel tipo slider con puntos (landing o portada) ----------------------
/*document.addEventListener('DOMContentLoaded', () => {
  const slider = document.querySelector('.Slider');
  const slides = document.querySelectorAll('.SlideContainer');
  const dots = document.querySelectorAll('.SliderNav a');
  let currentIndex = 0;
  let autoSlideInterval;

  // Si no hay puntos ni slides, no inicializar (solo portada)
  if (!slider || slides.length === 0 || dots.length === 0) return;

  function goToSlide(index) {
    currentIndex = index;
    slider.scrollTo({
      left: slider.offsetWidth * index,
      behavior: 'smooth',
    });
    updateDots(index);
  }

  function updateDots(index) {
    dots.forEach((dot, i) => {
      if (i === index) {
        dot.classList.add('active');
      } else {
        dot.classList.remove('active');
      }
    });
  }

  function nextSlide() {
    currentIndex++;
    const totalSlides = slides.length;

    slider.scrollTo({
      left: slider.offsetWidth * currentIndex,
      behavior: 'smooth',
    });

    if (currentIndex >= totalSlides / 2) {
      setTimeout(() => {
        slider.scrollLeft = 0;
        currentIndex = 0;
      }, 500);
    }

    updateDots(currentIndex % (totalSlides / 2));
  }

  function startAutoSlide() {
    autoSlideInterval = setInterval(nextSlide, 4000);
  }

  function pauseAutoSlide() {
    clearInterval(autoSlideInterval);
  }

  dots.forEach((dot, index) => {
    dot.addEventListener('click', e => {
      e.preventDefault();
      pauseAutoSlide();
      goToSlide(index);
      setTimeout(startAutoSlide, 15000);
    });
  });

  slider.addEventListener('mouseenter', pauseAutoSlide);
  slider.addEventListener('mouseleave', startAutoSlide);

  slider.addEventListener('scroll', () => {
    const index = Math.round(slider.scrollLeft / slider.offsetWidth);
    if (index !== currentIndex) {
      currentIndex = index;
      updateDots(index);
    }
  });

  goToSlide(0);
  startAutoSlide();
});
*/

//------------------------------------------------------------------------
// ---------------------- Tema y logo ----------------------
document.addEventListener('DOMContentLoaded', function () {
  const logo = document.querySelector('.LogoImg');
  const themeDuration = 700;

  const savedTheme = localStorage.getItem('theme') || 'light';
  document.documentElement.setAttribute('data-theme', savedTheme);

  logo.addEventListener('click', function () {
    if (logo.classList.contains('theme-changing')) return;
    logo.classList.add('theme-changing', 'logo-active');
    logo.classList.add('logo-click-animation');
    createRippleEffect(logo);

    setTimeout(() => {
      const currentTheme = document.documentElement.getAttribute('data-theme');
      const newTheme = currentTheme === 'light' ? 'dark' : 'light';

      document.body.classList.add('theme-transition-active');
      document.documentElement.setAttribute('data-theme', newTheme);
      localStorage.setItem('theme', newTheme);

      setTimeout(() => {
        logo.classList.remove('logo-click-animation', 'theme-changing');
        document.body.classList.remove('theme-transition-active');
        setTimeout(() => {
          logo.classList.remove('logo-active');
        }, 100);
      }, themeDuration);
    }, 150);
  });

  const prefersDark = window.matchMedia('(prefers-color-scheme: dark)');
  if (prefersDark.matches && !localStorage.getItem('theme')) {
    document.documentElement.setAttribute('data-theme', 'dark');
  }

  prefersDark.addEventListener('change', e => {
    if (!localStorage.getItem('theme')) {
      document.documentElement.setAttribute('data-theme', e.matches ? 'dark' : 'light');
    }
  });

  function createRippleEffect(element) {
    const ripple = document.createElement('div');
    ripple.className = 'theme-ripple-effect';
    element.appendChild(ripple);
    setTimeout(() => ripple.remove(), themeDuration);
  }
});

//------------------------------------------------------------------------
// ---------------------- Carrusel infinito de servicios ----------------------
// ---------------------- Carrusel tipo slider con puntos (landing o portada) ----------------------
document.addEventListener('DOMContentLoaded', () => {
  const slider = document.querySelector('.Slider');
  const slides = document.querySelectorAll('.SlideContainer');
  const dots = document.querySelectorAll('.SliderNav a');
  let currentIndex = 0;
  let autoSlideInterval;

  // Si no hay puntos ni slides, no inicializar (solo portada)
  if (!slider || slides.length === 0 || dots.length === 0) return;

  function goToSlide(index) {
    currentIndex = index;
    slider.scrollTo({
      left: slider.offsetWidth * index,
      behavior: 'smooth',
    });
    updateDots(index);
  }

  function updateDots(index) {
    dots.forEach((dot, i) => {
      if (i === index) {
        dot.classList.add('active');
      } else {
        dot.classList.remove('active');
      }
    });
  }

  function nextSlide() {
    currentIndex++;
    const totalSlides = slides.length;

    slider.scrollTo({
      left: slider.offsetWidth * currentIndex,
      behavior: 'smooth',
    });

    if (currentIndex >= totalSlides / 2) {
      setTimeout(() => {
        slider.scrollLeft = 0;
        currentIndex = 0;
      }, 500);
    }

    updateDots(currentIndex % (totalSlides / 2));
  }

  function startAutoSlide() {
    autoSlideInterval = setInterval(nextSlide, 4000);
  }

  function pauseAutoSlide() {
    clearInterval(autoSlideInterval);
  }

  dots.forEach((dot, index) => {
    dot.addEventListener('click', e => {
      e.preventDefault();
      pauseAutoSlide();
      goToSlide(index);
      setTimeout(startAutoSlide, 15000);
    });
  });

  slider.addEventListener('mouseenter', pauseAutoSlide);
  slider.addEventListener('mouseleave', startAutoSlide);

  slider.addEventListener('scroll', () => {
    const index = Math.round(slider.scrollLeft / slider.offsetWidth);
    if (index !== currentIndex) {
      currentIndex = index;
      updateDots(index);
    }
  });

  goToSlide(0);
  startAutoSlide();
});

//------------------------------------------------------------------------
// ---------------------- Tema y logo ----------------------
document.addEventListener('DOMContentLoaded', function () {
  const logo = document.querySelector('.LogoImg');
  const themeDuration = 700;

  // Aplicar tema guardado o por defecto
  const savedTheme = localStorage.getItem('theme') || 'light';
  document.documentElement.setAttribute('data-theme', savedTheme);

  // -------- Cambio de tema con el logo --------
  logo.addEventListener('click', function () {
    if (logo.classList.contains('theme-changing')) return;
    logo.classList.add('theme-changing', 'logo-active', 'logo-click-animation');
    createRippleEffect(logo);

    setTimeout(() => {
      const currentTheme = document.documentElement.getAttribute('data-theme');
      const newTheme = currentTheme === 'light' ? 'dark' : 'light';

      applyTheme(newTheme);
    }, 150);
  });

  // -------- Cambio de tema desde el submenu --------
  window.changeTheme = function (theme) {
    // Evita conflictos si ya está animando
    if (logo.classList.contains('theme-changing')) return;
    logo.classList.add('theme-changing', 'logo-active', 'logo-click-animation');
    createRippleEffect(logo);

    setTimeout(() => {
      applyTheme(theme);
    }, 150);
  };

  // -------- Detectar preferencias del sistema --------
  const prefersDark = window.matchMedia('(prefers-color-scheme: dark)');
  if (prefersDark.matches && !localStorage.getItem('theme')) {
    document.documentElement.setAttribute('data-theme', 'dark');
  }

  prefersDark.addEventListener('change', e => {
    if (!localStorage.getItem('theme')) {
      document.documentElement.setAttribute('data-theme', e.matches ? 'dark' : 'light');
    }
  });

  // -------- Función genérica para aplicar tema --------
  function applyTheme(newTheme) {
    document.body.classList.add('theme-transition-active');
    document.documentElement.setAttribute('data-theme', newTheme);
    localStorage.setItem('theme', newTheme);

    setTimeout(() => {
      logo.classList.remove('logo-click-animation', 'theme-changing');
      document.body.classList.remove('theme-transition-active');
      setTimeout(() => logo.classList.remove('logo-active'), 100);
    }, themeDuration);
  }

  // -------- Efecto visual de ripple --------
  function createRippleEffect(element) {
    const ripple = document.createElement('div');
    ripple.className = 'theme-ripple-effect';
    element.appendChild(ripple);
    setTimeout(() => ripple.remove(), themeDuration);
  }
});

//------------------------------------------------------------------------
// ---------------------- Carrusel infinito de servicios ----------------------
document.addEventListener('DOMContentLoaded', () => {
  const sliderTrack = document.querySelector('.SliderTrack');
  if (!sliderTrack) return;

  // Duplicar tarjetas solo una vez
  if (!sliderTrack.dataset.duplicated) {
    const originalCards = Array.from(sliderTrack.children);
    originalCards.forEach(card => {
      sliderTrack.appendChild(card.cloneNode(true));
    });
    sliderTrack.dataset.duplicated = 'true';
  }

  let position = 0;
  const speed = 1; // píxeles por frame
  const sliderWidth = sliderTrack.scrollWidth / 2;
  let animationFrameId;
  let isPaused = false;

  function animate() {
    if (!isPaused) {
      position += speed;
      if (position >= sliderWidth) position -= sliderWidth;
      sliderTrack.style.transform = `translateX(${-position}px)`;
      animationFrameId = requestAnimationFrame(animate);
    }
  }

  function pauseCarousel() {
    isPaused = true;
    cancelAnimationFrame(animationFrameId);
  }

  function resumeCarousel() {
    if (!isPaused) {
      return;
    }
    isPaused = false;
    animate();
  }

  animate();

  // Pausar/reanudar al pasar sobre cualquier botón "Detalles"
  const detalleButtons = sliderTrack.querySelectorAll('.btn-principal');
  detalleButtons.forEach(btn => {
    btn.addEventListener('mouseenter', pauseCarousel);
    btn.addEventListener('mouseleave', resumeCarousel);
  });
});

//_--------------------DURACION DEL MENU DERECHO---------------------
// delayed-dropdown.js
document.addEventListener('DOMContentLoaded', () => {
  const menus = document.querySelectorAll('.dropdown-menu.right-menu');

  menus.forEach(menu => {
    // 0) Ajustes de transición y estado (sin tocar tu CSS fuente)
    menu.style.setProperty('transition', 'opacity 0.8s ease', 'important');
    // No forzamos estado inicial; dejemos que tu CSS lo tenga oculto

    let hideTimer = null;
    let isHiding = false;

    const showMenu = () => {
      isHiding = false;
      clearTimeout(hideTimer);

      // Si tu CSS usa display:none/visibility:hidden, lo anulamos en línea
      menu.style.setProperty('display', 'block', 'important');
      menu.style.setProperty('visibility', 'visible', 'important');
      menu.style.setProperty('pointer-events', 'auto', 'important');

      // Forzar que la transición se aplique correctamente (fade-in si estaba en 0)
      // requestAnimationFrame asegura que el cambio a opacity=1 sea transicionable
      requestAnimationFrame(() => {
        menu.style.setProperty('opacity', '1', 'important');
      });
    };

    const startHideAfterDelay = () => {
      clearTimeout(hideTimer);
      hideTimer = setTimeout(() => {
        isHiding = true;
        // Inicia fade-out (0.8s)
        menu.style.setProperty('opacity', '0', 'important');

        // Cuando acabe el fade, si nadie interrumpió, ocultamos del todo
        const onEnd = e => {
          if (e.propertyName === 'opacity' && isHiding) {
            menu.style.setProperty('visibility', 'hidden', 'important');
            menu.style.setProperty('pointer-events', 'none', 'important');
            // Si tu layout lo requiere, puedes descomentar la siguiente línea:
            // menu.style.setProperty("display", "none", "important");
          }
        };
        menu.addEventListener('transitionend', onEnd, { once: true });

        // Fallback por si transitionend no dispara (Safari/condiciones extrañas)
        setTimeout(() => {
          if (isHiding) {
            menu.style.setProperty('visibility', 'hidden', 'important');
            menu.style.setProperty('pointer-events', 'none', 'important');
            // menu.style.setProperty("display", "none", "important");
          }
        }, 820);
      }, 2000); // <-- 2 segundos antes de empezar el fade-out
    };

    // 1) Definimos un "área de activación": el propio menú + su contenedor y el img más cercano.
    const container = menu.parentElement;
    const triggers = new Set([menu, container, menu.previousElementSibling].filter(Boolean));

    // Si hay una imagen dentro del contenedor (típico caso), la añadimos como trigger
    if (container) {
      const img = container.querySelector('img');
      if (img) triggers.add(img);
    }

    // 2) Eventos: entrar muestra; salir programa ocultado
    triggers.forEach(el => {
      el.addEventListener('mouseenter', showMenu);
      el.addEventListener('mouseleave', startHideAfterDelay);
    });
  });
});
//------------------------------------------------------------------------
// ---------------------- DURACION DEL MENU IZQUIERDO ----------------------
// delayed-dropdown.js
document.addEventListener('DOMContentLoaded', () => {
  const menus = document.querySelectorAll('.dropdown-menu.left-menu');

  menus.forEach(menu => {
    // 0) Ajustes de transición y estado (sin tocar tu CSS fuente)
    menu.style.setProperty('transition', 'opacity 0.8s ease', 'important');
    // No forzamos estado inicial; dejemos que tu CSS lo tenga oculto

    let hideTimer = null;
    let isHiding = false;

    const showMenu = () => {
      isHiding = false;
      clearTimeout(hideTimer);

      // Si tu CSS usa display:none/visibility:hidden, lo anulamos en línea
      menu.style.setProperty('display', 'block', 'important');
      menu.style.setProperty('visibility', 'visible', 'important');
      menu.style.setProperty('pointer-events', 'auto', 'important');

      // Forzar que la transición se aplique correctamente (fade-in si estaba en 0)
      // requestAnimationFrame asegura que el cambio a opacity=1 sea transicionable
      requestAnimationFrame(() => {
        menu.style.setProperty('opacity', '1', 'important');
      });
    };

    const startHideAfterDelay = () => {
      clearTimeout(hideTimer);
      hideTimer = setTimeout(() => {
        isHiding = true;
        // Inicia fade-out (0.8s)
        menu.style.setProperty('opacity', '0', 'important');

        // Cuando acabe el fade, si nadie interrumpió, ocultamos del todo
        const onEnd = e => {
          if (e.propertyName === 'opacity' && isHiding) {
            menu.style.setProperty('visibility', 'hidden', 'important');
            menu.style.setProperty('pointer-events', 'none', 'important');
            // Si tu layout lo requiere, puedes descomentar la siguiente línea:
            // menu.style.setProperty("display", "none", "important");
          }
        };
        menu.addEventListener('transitionend', onEnd, { once: true });

        // Fallback por si transitionend no dispara (Safari/condiciones extrañas)
        setTimeout(() => {
          if (isHiding) {
            menu.style.setProperty('visibility', 'hidden', 'important');
            menu.style.setProperty('pointer-events', 'none', 'important');
            // menu.style.setProperty("display", "none", "important");
          }
        }, 820);
      }, 2000); // <-- 2 segundos antes de empezar el fade-out
    };

    // 1) Definimos un "área de activación": el propio menú + su contenedor y el img más cercano.
    const container = menu.parentElement;
    const triggers = new Set([menu, container, menu.previousElementSibling].filter(Boolean));

    // Si hay una imagen dentro del contenedor (típico caso), la añadimos como trigger
    if (container) {
      const img = container.querySelector('img');
      if (img) triggers.add(img);
    }

    // 2) Eventos: entrar muestra; salir programa ocultado
    triggers.forEach(el => {
      el.addEventListener('mouseenter', showMenu);
      el.addEventListener('mouseleave', startHideAfterDelay);
    });
  });
});
