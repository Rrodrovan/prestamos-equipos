package edu.uam.prestamos_equipos.exception;

/** Datos inválidos -> HTTP 400 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
