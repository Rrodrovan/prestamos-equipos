package edu.uam.prestamos_equipos.repository;

import edu.uam.prestamos_equipos.model.EstadoPrestamo;
import edu.uam.prestamos_equipos.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    boolean existsByEquipoIdAndEstado(Long equipoId, EstadoPrestamo estado);

    List<Prestamo> findAllByOrderByIdDesc();
}
