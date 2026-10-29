package edu.uam.prestamos_equipos.config;

import edu.uam.prestamos_equipos.model.Equipo;
import edu.uam.prestamos_equipos.model.Rol;
import edu.uam.prestamos_equipos.model.Usuario;
import edu.uam.prestamos_equipos.repository.EquipoRepository;
import edu.uam.prestamos_equipos.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Datos mínimos para la demostración (solo si las tablas están vacías).
 * Cuentas de prueba:  admin / Admin123*   y   user / User123*
 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner cargarDatos(UsuarioRepository usuarios,
                                  EquipoRepository equipos,
                                  PasswordEncoder encoder) {
        return args -> {
            if (usuarios.count() == 0) {
                usuarios.save(new Usuario("admin", encoder.encode("Admin123*"), Rol.ADMIN));
                usuarios.save(new Usuario("user", encoder.encode("User123*"), Rol.USER));
            }
            if (equipos.count() == 0) {
                equipos.save(nuevo("PAT-0001", "Laptop Dell Latitude 5420", "Laptop"));
                equipos.save(nuevo("PAT-0002", "Proyector Epson X49", "Proyector"));
                equipos.save(nuevo("PAT-0003", "Tableta Samsung Galaxy Tab A8", "Tableta"));
            }
        };
    }

    private Equipo nuevo(String codigo, String nombre, String tipo) {
        Equipo e = new Equipo();
        e.setCodigoPatrimonial(codigo);
        e.setNombre(nombre);
        e.setTipo(tipo);
        return e;
    }
}
