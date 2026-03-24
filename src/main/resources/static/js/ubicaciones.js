// Sistema de carga dinámica de ubicaciones geográficas

document.addEventListener('DOMContentLoaded', function() {
    initializeUbicaciones();
});

function initializeUbicaciones() {
    const selectPais = document.getElementById('selectPais');
    const selectDepartamento = document.getElementById('selectDepartamento');
    const selectProvincia = document.getElementById('selectProvincia');
    const selectDistrito = document.getElementById('selectDistrito');
    
    if (!selectPais) return;
    
    // Cargar países
    cargarPaises();
    
    // Event listeners
    selectPais.addEventListener('change', function() {
        limpiarSelects(selectDepartamento, selectProvincia, selectDistrito);
        if (this.value) {
            cargarDepartamentos(this.value);
            guardarValorOculto('pais', this.options[this.selectedIndex].text);
        }
    });
    
    selectDepartamento.addEventListener('change', function() {
        limpiarSelects(selectProvincia, selectDistrito);
        if (this.value) {
            cargarProvincias(document.getElementById('selectPais').value, this.value);
            guardarValorOculto('departamento', this.options[this.selectedIndex].text);
        }
    });
    
    selectProvincia.addEventListener('change', function() {
        limpiarSelects(selectDistrito);
        if (this.value) {
            cargarDistritos(
                document.getElementById('selectPais').value,
                document.getElementById('selectDepartamento').value,
                this.value
            );
            guardarValorOculto('provincia', this.options[this.selectedIndex].text);
        }
    });
    
    selectDistrito.addEventListener('change', function() {
        if (this.value) {
            guardarValorOculto('distrito', this.options[this.selectedIndex].text);
            guardarValorOculto('idUbicacion', this.value);
        }
    });
}

function cargarPaises() {
    fetch('/api/ubicaciones/paises')
        .then(response => response.json())
        .then(data => {
            const select = document.getElementById('selectPais');
            data.forEach(pais => {
                const option = document.createElement('option');
                option.value = pais.id;
                option.textContent = pais.nombre;
                select.appendChild(option);
            });
        })
        .catch(error => console.error('Error cargando países:', error));
}

function cargarDepartamentos(paisId) {
    fetch(`/api/ubicaciones/departamentos/${paisId}`)
        .then(response => response.json())
        .then(data => {
            const select = document.getElementById('selectDepartamento');
            select.innerHTML = '<option value="">Seleccionar departamento</option>';
            data.forEach(dept => {
                const option = document.createElement('option');
                option.value = dept.id;
                option.textContent = dept.nombre;
                select.appendChild(option);
            });
            select.disabled = false;
        })
        .catch(error => console.error('Error cargando departamentos:', error));
}

function cargarProvincias(paisId, deptId) {
    fetch(`/api/ubicaciones/provincias/${paisId}/${deptId}`)
        .then(response => response.json())
        .then(data => {
            const select = document.getElementById('selectProvincia');
            select.innerHTML = '<option value="">Seleccionar provincia</option>';
            data.forEach(prov => {
                const option = document.createElement('option');
                option.value = prov.id;
                option.textContent = prov.nombre;
                select.appendChild(option);
            });
            select.disabled = false;
        })
        .catch(error => console.error('Error cargando provincias:', error));
}

function cargarDistritos(paisId, deptId, provId) {
    fetch(`/api/ubicaciones/distritos/${paisId}/${deptId}/${provId}`)
        .then(response => response.json())
        .then(data => {
            const select = document.getElementById('selectDistrito');
            select.innerHTML = '<option value="">Seleccionar distrito</option>';
            data.forEach(dist => {
                const option = document.createElement('option');
                option.value = dist.id;
                option.textContent = dist.nombre;
                select.appendChild(option);
            });
            select.disabled = false;
        })
        .catch(error => console.error('Error cargando distritos:', error));
}

function limpiarSelects(...selects) {
    selects.forEach(select => {
        select.innerHTML = '<option value="">Seleccionar...</option>';
        select.disabled = true;
    });
}

function guardarValorOculto(campo, valor) {
    const input = document.getElementById('hidden' + campo.charAt(0).toUpperCase() + campo.slice(1));
    if (input) {
        input.value = valor;
    }
}
