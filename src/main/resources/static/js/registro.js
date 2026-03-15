/* ============================================================
   registro.js — Club Náutico Neptuno
   Interactividad del formulario de registro
   ============================================================ */

(function () {
    'use strict';

    /* ── Campos obligatorios para calcular progreso ── */
    const REQUIRED_IDS = ['dni', 'nombre', 'apellido', 'correo', 'telefono', 'direccion', 'ciudad'];

    /* ── Referencias DOM ── */
    const progressFill = document.getElementById('progressFill');
    const progressPct  = document.getElementById('progressPct');
    const btnEnviar    = document.getElementById('btnEnviar');
    const terminos     = document.getElementById('terminos');
    const form         = document.getElementById('formRegistro');

    /* ─────────────────────────────────────────────
       PROGRESO
    ───────────────────────────────────────────── */
    function updateProgress() {
        const filled = REQUIRED_IDS.filter(id => {
            const el = document.getElementById(id);
            return el && el.value.trim().length > 0;
        }).length;

        const pct = Math.round((filled / REQUIRED_IDS.length) * 100);
        if (progressFill) { progressFill.style.width = pct + '%'; }
        if (progressPct)  { progressPct.textContent  = pct + '%'; }
    }

    REQUIRED_IDS.forEach(id => {
        const el = document.getElementById(id);
        if (el) {
            el.addEventListener('input',  updateProgress);
            el.addEventListener('change', updateProgress);
        }
    });

    /* ─────────────────────────────────────────────
       HABILITAR BOTÓN (solo cuando términos marcados)
    ───────────────────────────────────────────── */
    if (terminos) {
        terminos.addEventListener('change', function () {
            btnEnviar.disabled = !this.checked;
        });
    }

    /* ─────────────────────────────────────────────
       DNI — solo dígitos
    ───────────────────────────────────────────── */
    const dniInput = document.getElementById('dni');
    if (dniInput) {
        dniInput.addEventListener('input', function () {
            this.value = this.value.replace(/\D/g, '').slice(0, 8);
            updateProgress();
        });
    }

    /* ─────────────────────────────────────────────
       TELÉFONO — solo dígitos + guión/espacio
    ───────────────────────────────────────────── */
    const telInput = document.getElementById('telefono');
    if (telInput) {
        telInput.addEventListener('input', function () {
            this.value = this.value.replace(/[^\d\s\-+]/g, '').slice(0, 15);
            updateProgress();
        });
    }

    /* ─────────────────────────────────────────────
       FILE UPLOAD — drag & drop + feedback visual
    ───────────────────────────────────────────── */
    function setupUpload(inputId, zoneId, nameId) {
        const input = document.getElementById(inputId);
        const zone  = document.getElementById(zoneId);
        const nameEl = document.getElementById(nameId);
        if (!input || !zone) return;

        /* Drag handlers */
        ['dragenter', 'dragover'].forEach(evt => {
            zone.addEventListener(evt, e => {
                e.preventDefault();
                zone.classList.add('dragging');
            });
        });
        ['dragleave', 'drop'].forEach(evt => {
            zone.addEventListener(evt, () => zone.classList.remove('dragging'));
        });

        zone.addEventListener('drop', e => {
            e.preventDefault();
            if (e.dataTransfer.files.length > 0) {
                input.files = e.dataTransfer.files;
                showFilename();
                validateFile(input);
            }
        });

        input.addEventListener('change', () => {
            showFilename();
            validateFile(input);
        });

        function showFilename() {
            if (input.files.length > 0) {
                zone.classList.add('has-file');
                if (nameEl) nameEl.textContent = '✓ ' + input.files[0].name;
            }
        }

        function validateFile(inp) {
            if (!inp.files.length) return;
            const file     = inp.files[0];
            const maxBytes = 5 * 1024 * 1024; // 5 MB
            const allowed  = /\.(pdf|jpg|jpeg|png)$/i;

            if (!allowed.test(file.name)) {
                showUploadError(zone, nameEl, 'Formato no permitido. Use PDF, JPG o PNG.');
                inp.value = '';
                return;
            }
            if (file.size > maxBytes) {
                showUploadError(zone, nameEl, 'El archivo supera los 5 MB permitidos.');
                inp.value = '';
                return;
            }
        }

        function showUploadError(z, n, msg) {
            z.classList.remove('has-file');
            z.classList.add('is-error'); // will flash red briefly
            if (n) n.textContent = '✗ ' + msg;
            n.style.color = 'var(--error)';
            setTimeout(() => {
                z.classList.remove('is-error');
                if (n) { n.textContent = ''; n.style.color = ''; }
            }, 3500);
        }
    }

    setupUpload('archivoDni',  'zoneDni',  'nameDni');
    setupUpload('archivoFoto', 'zoneFoto', 'nameFoto');

    /* ─────────────────────────────────────────────
       SIDEBAR — highlight step on scroll
    ───────────────────────────────────────────── */
    const stepItems  = document.querySelectorAll('#stepNav li');
    const sectionIds = ['sec-tipo', 'sec-personal', 'sec-contacto', 'sec-documentos'];

    if ('IntersectionObserver' in window) {
        const obs = new IntersectionObserver(entries => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const id = entry.target.id;
                    stepItems.forEach(li => {
                        li.classList.remove('active');
                        if (li.dataset.sec === id) li.classList.add('active');
                    });
                }
            });
        }, { threshold: 0.35 });

        sectionIds.forEach(id => {
            const el = document.getElementById(id);
            if (el) obs.observe(el);
        });
    }

    /* ─────────────────────────────────────────────
       VALIDACIÓN CLIENTE ANTES DE ENVIAR
    ───────────────────────────────────────────── */
    if (form) {
        form.addEventListener('submit', function (e) {
            /* Verificar términos */
            if (!terminos || !terminos.checked) {
                e.preventDefault();
                terminos && terminos.focus();
                return;
            }

            /* Verificar campos obligatorios vacíos */
            let firstEmpty = null;
            REQUIRED_IDS.forEach(id => {
                const el = document.getElementById(id);
                if (el && el.value.trim().length === 0) {
                    el.classList.add('fi-error');
                    if (!firstEmpty) firstEmpty = el;
                } else if (el) {
                    el.classList.remove('fi-error');
                }
            });

            if (firstEmpty) {
                e.preventDefault();
                firstEmpty.scrollIntoView({ behavior: 'smooth', block: 'center' });
                firstEmpty.focus();
                return;
            }

            /* Estado visual de envío */
            btnEnviar.disabled = true;
            btnEnviar.innerHTML = '<svg viewBox="0 0 24 24" style="animation:spin .8s linear infinite" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10" opacity=".3"/><path d="M12 2a10 10 0 0 1 10 10"/></svg> Enviando…';
        });
    }

    /* Spin keyframe (inline) */
    const style = document.createElement('style');
    style.textContent = '@keyframes spin { to { transform: rotate(360deg); } }';
    document.head.appendChild(style);

    /* Inicializar progreso */
    updateProgress();

})();
