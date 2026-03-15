-- =============================================================
-- ACTUALIZACIÓN: Tabla POSTULANTE - Almacenar campos faltantes
-- =============================================================
-- Este script agrega los campos necesarios para el registro 
-- de postulantes con tipo de interés (NAUTICO o INSTALACIONES)
-- =============================================================

USE Neptuno;

-- 1. Verificar si la columna 'telefono' existe, si no crearla
ALTER TABLE postulante ADD COLUMN IF NOT EXISTS telefono VARCHAR(20) NULL;

-- 2. Verificar si la columna 'ciudad' existe, si no crearla
ALTER TABLE postulante ADD COLUMN IF NOT EXISTS ciudad VARCHAR(100) NULL;

-- 3. Verificar si la columna 'tipo_interes' existe, si no crearla
ALTER TABLE postulante ADD COLUMN IF NOT EXISTS tipo_interes VARCHAR(20) 
  DEFAULT 'INSTALACIONES' CHECK (tipo_interes IN ('NAUTICO', 'INSTALACIONES'));

-- 4. Actualizar fecha_registro para soportar hora (si es necesario)
-- (si ya es DATETIME, este comando no hace nada)

-- 5. Crear índices para búsquedas rápidas
CREATE INDEX IF NOT EXISTS IX_postulante_telefono ON postulante(telefono);
CREATE INDEX IF NOT EXISTS IX_postulante_tipo_interes ON postulante(tipo_interes);

-- Confirmar visualmente los cambios
DESCRIBE postulante;
