package edu.uam.prestamos_equipos.service;

import edu.uam.prestamos_equipos.dto.LoginRequest;
import edu.uam.prestamos_equipos.dto.LoginResponse;
import edu.uam.prestamos_equipos.model.Usuario;
import edu.uam.prestamos_equipos.repository.UsuarioRepository;
import edu.uam.prestamos_equipos.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest req) {
        Usuario u = usuarioRepository.findByUsername(req.username())
                .orElseThrow(() -> new BadCredentialsException("Credenciales incorrectas"));

        if (!passwordEncoder.matches(req.password(), u.getPassword())) {
            throw new BadCredentialsException("Credenciales incorrectas");
        }

        String token = jwtService.generarToken(u.getUsername(), u.getRol().name());
        return new LoginResponse(token, "Bearer", u.getUsername(), u.getRol().name());
    }
}
