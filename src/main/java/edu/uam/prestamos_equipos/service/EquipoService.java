package edu.uam.prestamos_equipos.service;

import edu.uam.prestamos_equipos.dto.EquipoRequest;
import edu.uam.prestamos_equipos.dto.EquipoResponse;
import edu.uam.prestamos_equipos.exception.ConflictException;
import edu.uam.prestamos_equipos.exception.NotFoundException;
import edu.uam.prestamos_equipos.model.Equipo;
import edu.uam.prestamos_equipos.model.EstadoEquipo;
import edu.uam.prestamos_equipos.model.EstadoPrestamo;
import edu.uam.prestamos_equipos.repository.EquipoRepository;
import edu.uam.prestamos_equipos.repository.PrestamoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EquipoService {

    private final EquipoRepository equipoRepository;
    private final PrestamoRepository prestamoRepository;

    public EquipoService(EquipoRepository equipoRepository, PrestamoRepository prestamoRepository) {
        this.equipoRepository = equipoRepository;
        this.prestamoRepository = prestamoRepository;
    }

    @Transactional(readOnly = true)
    public List<EquipoResponse> listar() {
        return equipoRepository.findAll().stream().map(EquipoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public EquipoResponse obtener(Long id) {
        return EquipoResponse.from(buscar(id));
    }

    @Transactional
    public EquipoResponse crear(EquipoRequest req) {
        String codigo = req.codigoPatrimonial().trim();
        if (equipoRepository.existsByCodigoPatrimonial(codigo)) {
            throw new ConflictException("Ya existe un equipo con el código patrimonial " + codigo);
        }
        Equipo e = new Equipo();
        e.setCodigoPatrimonial(codigo);
        e.setNombre(req.nombre().trim());
        e.setTipo(req.tipo().trim());
        e.setEstado(EstadoEquipo.DISPONIBLE);
        return EquipoResponse.from(equipoRepository.save(e));
    }

    /** El estado NO se modifica aquí: solo cambia con préstamos y devoluciones. */
    @Transactional
    public EquipoResponse actualizar(Long id, EquipoRequest req) {
        Equipo e = buscar(id);
        String codigo = req.codigoPatrimonial().trim();
        if (equipoRepository.existsByCodigoPatrimonialAndIdNot(codigo, id)) {
            throw new ConflictException("Ya existe otro equipo con el código patrimonial " + codigo);
        }
        e.setCodigoPatrimonial(codigo);
        e.setNombre(req.nombre().trim());
        e.setTipo(req.tipo().trim());
        return EquipoResponse.from(equipoRepository.save(e));
    }

    @Transactional
    public void eliminar(Long id) {
        Equipo e = buscar(id);
        if (prestamoRepository.existsByEquipoIdAndEstado(id, EstadoPrestamo.ACTIVO)) {
            throw new ConflictException("No se puede eliminar el equipo: tiene préstamos activos");
        }
        equipoRepository.delete(e);
    }

    private Equipo buscar(Long id) {
        return equipoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Equipo con id " + id + " no encontrado"));
    }
}
