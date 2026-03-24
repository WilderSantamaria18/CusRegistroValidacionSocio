// Sistema de carga dinámica de ubicaciones geográficas

document.addEventListener('DOMContentLoaded', function() {
    initializeUbicaciones();
});

function initializeUbicaciones() {
    const selectPais = document.getElementById('selectPais');
    const selectDepartamento = document.getElementById('selectDepartamento');
    const selectProvincia = document.getElementById('selectProvincia');
    const selectDistrito = document.getElementById('selectDistrito');
    
    if (!selectPais) {
        console.error("Elementos de ubicación no encontrados");
        return;
    }
    
    // Event listeners
    selectPais.addEventListener('change', function() {
        console.log("País seleccionado:", this.value);
        limpiarSelects(selectDepartamento, selectProvincia, selectDistrito);
        if (this.value) {
            cargarDepartamentos(this.value);
            guardarValorOculto('pais', this.options[this.selectedIndex].text);
        }
    });
    
    selectDepartamento.addEventListener('change', function() {
        console.log("Departamento seleccionado:", this.value);
        limpiarSelects(selectProvincia, selectDistrito);
        if (this.value) {
            cargarProvincias(document.getElementById('selectPais').value, this.value);
            guardarValorOculto('departamento', this.options[this.selectedIndex].text);
        }
    });
    
    selectProvincia.addEventListener('change', function() {
        console.log("Provincia seleccionada:", this.value);
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
        console.log("Distrito seleccionado:", this.value, this.options[this.selectedIndex].text);
        if (this.value) {
            guardarValorOculto('distrito', this.options[this.selectedIndex].text);
            guardarValorOculto('idUbicacion', this.value);
        }
    });
    
    // Cargar países al iniciar
    cargarPaises();
}

function cargarPaises() {
    console.log("Cargando países...");
    fetch('/api/ubicaciones/paises')
        .then(response => {
            if (!response.ok) throw new Error('Error al cargar países');
            return response.json();
        })
        .then(data => {
            console.log("Países cargados:", data);
            const select = document.getElementById('selectPais');
            select.innerHTML = '<option value="">Seleccionar país</option>';
            data.forEach(pais => {
                const option = document.createElement('option');
                option.value = pais.id;
                option.textContent = pais.nombre;
                select.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Error cargando países:', error);
            alert('No se pudieron cargar los países. Por favor, recargue la página.');
        });
}

function cargarDepartamentos(paisId) {
    console.log("Cargando departamentos para país:", paisId);
    fetch(`/api/ubicaciones/departamentos/${paisId}`)
        .then(response => {
            if (!response.ok) throw new Error('Error al cargar departamentos');
            return response.json();
        })
        .then(data => {
            console.log("Departamentos cargados:", data);
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
        .catch(error => {
            console.error('Error cargando departamentos:', error);
            const select = document.getElementById('selectDepartamento');
            select.innerHTML = '<option value="">Error al cargar departamentos</option>';
        });
}

function cargarProvincias(paisId, deptId) {
    console.log("Cargando provincias para país:", paisId, "departamento:", deptId);
    fetch(`/api/ubicaciones/provincias/${paisId}/${deptId}`)
        .then(response => {
            if (!response.ok) throw new Error('Error al cargar provincias');
            return response.json();
        })
        .then(data => {
            console.log("Provincias cargadas:", data);
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
        .catch(error => {
            console.error('Error cargando provincias:', error);
            const select = document.getElementById('selectProvincia');
            select.innerHTML = '<option value="">Error al cargar provincias</option>';
        });
}

function cargarDistritos(paisId, deptId, provId) {
    console.log("Cargando distritos para país:", paisId, "departamento:", deptId, "provincia:", provId);
    fetch(`/api/ubicaciones/distritos/${paisId}/${deptId}/${provId}`)
        .then(response => {
            if (!response.ok) throw new Error('Error al cargar distritos');
            return response.json();
        })
        .then(data => {
            console.log("Distritos cargados:", data);
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
        .catch(error => {
            console.error('Error cargando distritos:', error);
            const select = document.getElementById('selectDistrito');
            select.innerHTML = '<option value="">Error al cargar distritos</option>';
        });
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
        console.log("Guardado campo oculto:", campo, "=", valor);
    } else {
        console.warn("Campo oculto no encontrado:", campo);
    }
}
