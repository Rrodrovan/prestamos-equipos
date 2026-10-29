package edu.uam.prestamos_equipos.dto;

import edu.uam.prestamos_equipos.model.Equipo;
import edu.uam.prestamos_equipos.model.EstadoEquipo;

public record EquipoResponse(
        Long id,
        String codigoPatrimonial,
        String nombre,
        String tipo,
        EstadoEquipo estado
) {
    public static EquipoResponse from(Equipo e) {
        return new EquipoResponse(e.getId(), e.getCodigoPatrimonial(), e.getNombre(), e.getTipo(), e.getEstado());
    }
}
