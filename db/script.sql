

CREATE TABLE IF NOT EXISTS equipos (
    id                  BIGSERIAL PRIMARY KEY,
    codigo_patrimonial  VARCHAR(50)  NOT NULL UNIQUE,
    nombre              VARCHAR(120) NOT NULL,
    tipo                VARCHAR(60)  NOT NULL,
    estado              VARCHAR(20)  NOT NULL CHECK (estado IN ('DISPONIBLE', 'PRESTADO'))
);

CREATE TABLE IF NOT EXISTS prestamos (
    id                         BIGSERIAL PRIMARY KEY,
    equipo_id                  BIGINT       NOT NULL REFERENCES equipos(id),
    nombre_solicitante         VARCHAR(120) NOT NULL,
    fecha_prestamo             DATE         NOT NULL,
    fecha_prevista_devolucion  DATE         NOT NULL,
    estado                     VARCHAR(20)  NOT NULL CHECK (estado IN ('ACTIVO', 'DEVUELTO')),
    CONSTRAINT ck_fechas CHECK (fecha_prevista_devolucion >= fecha_prestamo)
);

CREATE TABLE IF NOT EXISTS usuarios (
    id        BIGSERIAL PRIMARY KEY,
    username  VARCHAR(60)  NOT NULL UNIQUE,
    password  VARCHAR(100) NOT NULL,          -- BCrypt
    rol       VARCHAR(20)  NOT NULL CHECK (rol IN ('USER', 'ADMIN'))
);

-- Cuentas de prueba (contraseñas guardadas con BCrypt)
--   admin / Admin123*   (ADMIN)
--   user  / User123*    (USER)
INSERT INTO usuarios (username, password, rol) VALUES
  ('admin', '$2a$10$Cjlw0UbXRYIDjBV8q.ocHunyuvXWiN/UJNuLMIJvxBDcAzWC7eY1y', 'ADMIN'),
  ('user',  '$2a$10$lMG/Ndo2gYzFqDIPkl9DSeC5D2i1sCz5wWq15Pdt4qbkWQEpmXLQ.', 'USER')
ON CONFLICT (username) DO NOTHING;

INSERT INTO equipos (codigo_patrimonial, nombre, tipo, estado) VALUES
  ('PAT-0001', 'Laptop Dell Latitude 5420',       'Laptop',    'DISPONIBLE'),
  ('PAT-0002', 'Proyector Epson X49',             'Proyector', 'DISPONIBLE'),
  ('PAT-0003', 'Tableta Samsung Galaxy Tab A8',   'Tableta',   'DISPONIBLE')
ON CONFLICT (codigo_patrimonial) DO NOTHING;
