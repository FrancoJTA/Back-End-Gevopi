package com.example.gevopi_back_end.Service;

import org.springframework.stereotype.Service;
import com.example.gevopi_back_end.Entity.Usuario;
import com.example.gevopi_back_end.Repository.UsuarioRepository;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Registro (sin encriptación)
    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepository.existsByEmailOrCi(usuario.getEmail(), usuario.getCi())) {
            throw new RuntimeException("El email o CI ya están registrados");
        }
        return usuarioRepository.save(usuario); // Guarda en texto plano
    }

    // Login (validación en texto plano)
    public Usuario login(String ci, String password) {
        Usuario usuario = usuarioRepository.findByCi(ci)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getPassword().equals(password)) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        return usuario;
    }
}
