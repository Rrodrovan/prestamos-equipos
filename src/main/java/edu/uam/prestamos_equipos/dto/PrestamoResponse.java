package edu.uam.prestamos_equipos.dto;

import edu.uam.prestamos_equipos.model.EstadoPrestamo;
import edu.uam.prestamos_equipos.model.Prestamo;

import java.time.LocalDate;

public record PrestamoResponse(
        Long id,
        Long equipoId,
        String equipoCodigo,
        String equipoNombre,
        String nombreSolicitante,
        LocalDate fechaPrestamo,
        LocalDate fechaPrevistaDevolucion,
        EstadoPrestamo estado
) {
    public static PrestamoResponse from(Prestamo p) {
        return new PrestamoResponse(
                p.getId(),
                p.getEquipo().getId(),
                p.getEquipo().getCodigoPatrimonial(),
                p.getEquipo().getNombre(),
                p.getNombreSolicitante(),
                p.getFechaPrestamo(),
                p.getFechaPrevistaDevolucion(),
                p.getEstado());
    }
}
