package com.example.gevopi_back_end.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import com.example.gevopi_back_end.Entity.Usuario;
import com.example.gevopi_back_end.Repository.UsuarioRepository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private WebClient webClient;
    private static final String EXTERNAL_API_URL = "http://Global_Api:2020/global_registro/alasA";

    // Registro (sin encriptación)
    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepository.existsByEmailOrCi(usuario.getEmail(), usuario.getCi())) {
            throw new RuntimeException("El email o CI ya están registrados");
        }
        Usuario savedUser = usuarioRepository.save(usuario);

        try {
            webClient.post()
                    .uri(EXTERNAL_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(savedUser)
                    .retrieve()
                    .bodyToMono(String.class)  // O puedes definir un DTO si quieres parsear la respuesta
                    .block();
        } catch (WebClientResponseException ex) {
            // Aquí puedes manejar errores específicos de la llamada externa
            throw new RuntimeException("Error enviando usuario a API externa: " + ex.getResponseBodyAsString());
        } catch (Exception e) {
            // Otros errores (timeout, conexión, etc)
            throw new RuntimeException("Error enviando usuario a API externa: " + e.getMessage());
        }

        return savedUser;
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
