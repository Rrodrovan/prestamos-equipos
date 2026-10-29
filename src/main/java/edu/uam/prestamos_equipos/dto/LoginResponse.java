package edu.uam.prestamos_equipos.dto;

public record LoginResponse(String token, String tipo, String username, String rol) {
}
