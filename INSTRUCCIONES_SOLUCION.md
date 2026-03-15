# SOLUCIÓN: Error HTTP 413 y Sincronización de Base de Datos

## 🔴 PROBLEMA IDENTIFICADO

1. **Error HTTP 413**: Límite de tamaño de solicitud muy bajo (archivos adjuntos bloqueados)
2. **Mapeo incorrecto de tablas**: Tabla SQL no coincidía con modelo Java
3. **Campos faltantes en BD**: telefono, ciudad, tipo_interes

---

## ✅ CORRECCIONES REALIZADAS

### A) application.properties
- ✓ Aumentado límite de tamaño de solicitud a 10MB
- ✓ Configurado multipart para archivos

### B) Modelo Postulante.java
- ✓ Corregido nombre de tabla: `postulantes` → `postulante`
- ✓ Mapeados nombres exactos de columnas SQL
- ✓ Agregadas anotaciones @Column con nombres correctos

### C) PostulanteService.java
- ✓ Actualizado mapeo nombres/apellido_paterno/apellido_materno
- ✓ Parseando apellidos correctamente desde el DTO

---

## 🔧 PASOS QUE DEBES HACER (EN ORDEN)

### PASO 1: Ejecutar Script SQL
Abre MySQL Workbench o el cliente MySQL y ejecuta el contenido del archivo:
```
ACTUALIZAR_BD_NEPTUNO.sql
```

**Esto agrega:**
- `telefono` (VARCHAR 20)
- `ciudad` (VARCHAR 100)  
- `tipo_interes` (ENUM: NAUTICO / INSTALACIONES)

### PASO 2: Compilar el Proyecto
Ejecuta en terminal (en la raíz del proyecto):
```powershell
mvn clean compile
```

### PASO 3: Limpiar y Reiniciar la Aplicación
```powershell
mvn spring-boot:run
```

### PASO 4: Probar el Registro
1. Ve a `http://localhost:8080/registro`
2. Selecciona "Área de interés" (Náutica o Instalaciones)
3. Rellena todos los campos
4. Envía el formulario

---

## 📋 IMPORTANTE: Estructura de Apellidos

El formulario pide **"Apellidos"** (un campo), pero la BD tiene:
- `apellido_paterno` 
- `apellido_materno`

**SOLUCIÓN**: Se divide automáticamente en el servicio:
- Primer elemento → `apellido_paterno`
- Segundo elemento → `apellido_materno`

**Ejemplo**: Si escribes "García López"
- Se guarda: García (paterno) | López (materno)

---

## 🎯 ESTRUCTURA FINAL (BD MySQL)

```
postulante
├── id_postulante (PK, AUTO_INCREMENT)
├── dni (VARCHAR 8, UNIQUE)
├── nombres (VARCHAR 100)
├── apellido_paterno (VARCHAR 100)
├── apellido_materno (VARCHAR 100)
├── correo_electronico (VARCHAR 100, UNIQUE) ← Desde 'correo'
├── telefono (VARCHAR 20) ← NUEVO
├── direccion (VARCHAR 200)
├── ciudad (VARCHAR 100) ← NUEVO
├── fecha_registro (DATE/DATETIME)
├── tipo_interes (VARCHAR 20: NAUTICO/INSTALACIONES) ← NUEVO
└── estado_postulacion (VARCHAR 20: PENDIENTE)
```

---

## ❓ PREGUNTAS QUE PODRÍAS TENER

**P: ¿Y si ya tengo datos en la tabla postulante?**  
R: El script SQL usa `ADD COLUMN IF NOT EXISTS`, así que no sobrescribe datos existentes.

**P: ¿Puedo cambiar los tipos de interés?**  
R: Sí, modifica el enum `TipoInteres.java` si necesitas más opciones.

**P: ¿El archivo adjunto es requerido?**  
R: No, en el formulario solo hay campos "Documentos adjuntos" opcionales. El limite de 10MB solo previene errores.

---

## 📞 FLUJO CORRECTO DE REGISTRO

1. Usuario carga formulario → `GET /registro`
2. Usuario selecciona "Área de interés" (NAUTICO/INSTALACIONES)
3. Usuario rellena: DNI, Nombres, Apellidos, Correo, Teléfono, Dirección, Ciudad
4. Usuario envía → `POST /registro/guardar`
5. Backend valida y guarda en tabla `postulante`
6. Usuario ve pantalla de éxito con número de seguimiento

---

EL PROYECTO HARÁ **SOLO REGISTRO**, sin login ni otros flujos. ✅
