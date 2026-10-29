# TecnoUAM · Préstamo de equipos (proyecto completo)

Proyecto Maven + Spring Boot 4.1.2 + Java 21 + PostgreSQL + JWT, con el React dentro de `src/main/resources/static/index.html`.

## Cómo abrirlo
1. Descomprime y abre la carpeta `prestamos-equipos` en IntelliJ (File -> Open -> selecciona el `pom.xml` -> Open as Project).
2. File -> Project Structure -> SDK: **Temurin 21** (no uses el JDK de IBM).
   Settings -> Build Tools -> Maven -> Importing y Runner: el mismo Temurin 21.
3. Reload All Maven Projects (panel Maven).
4. Crea la BD:  `CREATE DATABASE tecnouam_prestamos;`
5. Copia `.env.example` a `.env` y completa DB_PASSWORD y JWT_SECRET (32+ caracteres).
6. Ejecuta `PrestamosEquiposApplication` y abre http://localhost:8080
7. Importa `postman/TecnoUAM-Prestamos.postman_collection.json` y ejecútala con el Runner.

Cuentas de prueba: `admin / Admin123*` (ADMIN) y `user / User123*` (USER).

Este ZIP no incluye `mvnw`. Usa el Maven de IntelliJ, o copia `mvnw`, `mvnw.cmd` y la carpeta `.mvn` de tu proyecto original.
## Endpoints

| Método y ruta | Rol | Respuesta |
|---|---|---|
| POST /api/auth/login | público | 200 / 401 |
| GET /api/equipos, GET /api/equipos/{id} | USER, ADMIN | 200 / 404 |
| POST /api/equipos | ADMIN | 201 / 400 / 409 |
| PUT /api/equipos/{id} | ADMIN | 200 / 400 / 404 / 409 |
| DELETE /api/equipos/{id} | ADMIN | 204 / 404 / 409 (préstamo activo) |
| GET /api/prestamos | USER, ADMIN | 200 |
| POST /api/prestamos | ADMIN | 201 / 400 / 404 / 409 (equipo ya prestado) |
| PUT /api/prestamos/{id}/devolver | ADMIN | 200 / 404 / 409 |

## Para el README del repositorio y la defensa

- Reglas de negocio: `EquipoService` y `PrestamoService`.
- Flujo JWT: `AuthService` → `JwtService.generarToken` → `JwtAuthFilter` valida cada petición → `SecurityConfig` aplica roles.
- 401 = sin token o token inválido (no autenticado). 403 = autenticado pero sin permiso (USER intentando escribir).
- Relación JPA: `Equipo` 1 — N `Prestamo` (`@OneToMany` / `@ManyToOne`).
- Al eliminar un equipo sin préstamos activos, se borra también su historial de préstamos devueltos (`CascadeType.REMOVE`).
- El estado del equipo solo cambia por préstamos y devoluciones (el PUT de equipos no lo modifica).
- Fechas en formato `yyyy-MM-dd`.
