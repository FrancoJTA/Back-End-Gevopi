package com.example.gevopi_back_end.Service;

import com.example.gevopi_back_end.Entity.Rol;
import com.example.gevopi_back_end.Repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import com.example.gevopi_back_end.Entity.Usuario;
import com.example.gevopi_back_end.Repository.UsuarioRepository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private WebClient webClient;

    private static final String EXTERNAL_API_URL = "http://Global-Api:2020/global_registro/alasA";

    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepository.existsByEmailOrCi(usuario.getEmail(), usuario.getCi())) {
            throw new RuntimeException("El email o CI ya están registrados");
        }
        usuario.setActivo(false);

        Rol rol = rolRepository.findById(2)
                .orElseThrow(() -> new RuntimeException("Rol con ID 2 no encontrado"));

        usuario.setRoles(Collections.singleton(rol));

        Usuario savedUser = usuarioRepository.save(usuario);

        try {
            webClient.post()
                    .uri(EXTERNAL_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(savedUser)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException ex) {
            throw new RuntimeException("Error enviando usuario a API externa: " + ex.getResponseBodyAsString());
        } catch (Exception e) {
            throw new RuntimeException("Error enviando usuario a API externa: " + e.getMessage());
        }

        return savedUser;
    }

    public Boolean registroGlobal(Usuario usuario) {
        if (usuarioRepository.existsByEmailOrCi(usuario.getEmail(), usuario.getCi())) {
            throw new RuntimeException("El email o CI ya están registrados");
        }

        usuario.setActivo(false);

        Rol rol = rolRepository.findById(2)
                .orElseThrow(() -> new RuntimeException("Rol con ID 2 no encontrado"));

        usuario.setRoles(Collections.singleton(rol));
        usuarioRepository.save(usuario);
        return true;
    }

    public Usuario login(String ci, String password) {
        Usuario usuario = usuarioRepository.findByCi(ci)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getPassword().equals(password)) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        return usuario;
    }
}
