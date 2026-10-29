package edu.uam.prestamos_equipos.service;

import edu.uam.prestamos_equipos.dto.PrestamoRequest;
import edu.uam.prestamos_equipos.dto.PrestamoResponse;
import edu.uam.prestamos_equipos.exception.BadRequestException;
import edu.uam.prestamos_equipos.exception.ConflictException;
import edu.uam.prestamos_equipos.exception.NotFoundException;
import edu.uam.prestamos_equipos.model.Equipo;
import edu.uam.prestamos_equipos.model.EstadoEquipo;
import edu.uam.prestamos_equipos.model.EstadoPrestamo;
import edu.uam.prestamos_equipos.model.Prestamo;
import edu.uam.prestamos_equipos.repository.EquipoRepository;
import edu.uam.prestamos_equipos.repository.PrestamoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final EquipoRepository equipoRepository;

    public PrestamoService(PrestamoRepository prestamoRepository, EquipoRepository equipoRepository) {
        this.prestamoRepository = prestamoRepository;
        this.equipoRepository = equipoRepository;
    }

    @Transactional(readOnly = true)
    public List<PrestamoResponse> listar() {
        return prestamoRepository.findAllByOrderByIdDesc().stream().map(PrestamoResponse::from).toList();
    }

    @Transactional
    public PrestamoResponse registrar(PrestamoRequest req) {
        if (req.fechaPrevistaDevolucion().isBefore(req.fechaPrestamo())) {
            throw new BadRequestException(
                    "La fecha prevista de devolución no puede ser anterior a la fecha del préstamo");
        }

        Equipo equipo = equipoRepository.findById(req.equipoId())
                .orElseThrow(() -> new NotFoundException("Equipo con id " + req.equipoId() + " no encontrado"));

        if (equipo.getEstado() == EstadoEquipo.PRESTADO
                || prestamoRepository.existsByEquipoIdAndEstado(equipo.getId(), EstadoPrestamo.ACTIVO)) {
            throw new ConflictException("El equipo ya está PRESTADO y tiene un préstamo activo");
        }

        Prestamo p = new Prestamo();
        p.setEquipo(equipo);
        p.setNombreSolicitante(req.nombreSolicitante().trim());
        p.setFechaPrestamo(req.fechaPrestamo());
        p.setFechaPrevistaDevolucion(req.fechaPrevistaDevolucion());
        p.setEstado(EstadoPrestamo.ACTIVO);

        equipo.setEstado(EstadoEquipo.PRESTADO);
        equipoRepository.save(equipo);

        return PrestamoResponse.from(prestamoRepository.save(p));
    }

    @Transactional
    public PrestamoResponse devolver(Long id) {
        Prestamo p = prestamoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Préstamo con id " + id + " no encontrado"));

        if (p.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new ConflictException("El préstamo ya fue devuelto");
        }

        p.setEstado(EstadoPrestamo.DEVUELTO);
        Equipo equipo = p.getEquipo();
        equipo.setEstado(EstadoEquipo.DISPONIBLE);
        equipoRepository.save(equipo);

        return PrestamoResponse.from(prestamoRepository.save(p));
    }
}
