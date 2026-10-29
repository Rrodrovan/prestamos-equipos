package edu.uam.prestamos_equipos.exception;

/** Conflicto con una regla de negocio -> HTTP 409 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
