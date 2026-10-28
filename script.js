/* ==========================================================================
   KATIA.DESIGN - Interactive Logic
   ========================================================================== */

document.addEventListener('DOMContentLoaded', () => {

  // 2. Project Discussion Modal
  const modal = document.getElementById('discuss-modal');
  const openModalBtn = document.getElementById('open-discuss-modal');
  const closeModalBtn = document.getElementById('close-discuss-modal');
  const projectForm = document.getElementById('project-form');
  const successBox = document.getElementById('form-success-message');

  if (modal && openModalBtn) {
    openModalBtn.addEventListener('click', () => {
      if (typeof modal.showModal === 'function') {
        modal.showModal();
      } else {
        modal.setAttribute('open', '');
      }
    });
  }

  if (modal && closeModalBtn) {
    closeModalBtn.addEventListener('click', () => {
      modal.close();
    });
  }

  // Light dismiss: close modal when clicking on backdrop
  if (modal) {
    modal.addEventListener('click', (event) => {
      const rect = modal.getBoundingClientRect();
      const isInDialog = (
        rect.top <= event.clientY &&
        event.clientY <= rect.top + rect.height &&
        rect.left <= event.clientX &&
        event.clientX <= rect.left + rect.width
      );
      if (!isInDialog) {
        modal.close();
      }
    });
  }

  // Form submission handler
  window.handleFormSubmit = () => {
    if (projectForm && successBox) {
      projectForm.style.display = 'none';
      successBox.style.display = 'block';

      // Reset after 3.5 seconds
      setTimeout(() => {
        projectForm.reset();
        projectForm.style.display = 'flex';
        successBox.style.display = 'none';
        modal.close();
      }, 3500);
    }
  };

  // 3. Card subtle tilt on mouse move
  const cards = document.querySelectorAll('[data-tilt]');
  cards.forEach(card => {
    card.addEventListener('mousemove', (e) => {
      const rect = card.getBoundingClientRect();
      const x = e.clientX - rect.left;
      const y = e.clientY - rect.top;
      const glow = card.querySelector('.card-glow');
      if (glow) {
        glow.style.transform = `translate(${x - rect.width / 2}px, ${y - rect.height / 2}px)`;
        glow.style.opacity = '1';
      }
    });

    card.addEventListener('mouseleave', () => {
      const glow = card.querySelector('.card-glow');
      if (glow) {
        glow.style.opacity = '0';
      }
    });
  });

  // 4. Smooth Anchor Scrolling
  document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function(e) {
      const targetId = this.getAttribute('href');
      if (targetId && targetId !== '#') {
        const targetElement = document.querySelector(targetId);
        if (targetElement) {
          e.preventDefault();
          targetElement.scrollIntoView({
            behavior: 'smooth',
            block: 'start'
          });
        }
      }
    });
  });
});
