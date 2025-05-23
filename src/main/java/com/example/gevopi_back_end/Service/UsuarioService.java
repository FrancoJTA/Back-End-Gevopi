package com.example.gevopi_back_end.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import com.example.gevopi_back_end.Entity.Usuario;
import com.example.gevopi_back_end.Repository.UsuarioRepository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.Map;
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

        // Construir mutación GraphQL
        String mutation = "mutation Mutation($input: UsuarioInput!) { nuevoUsuarioGlobal(input: $input) { id } }";

        // Variables para la mutación
        Map<String, Object> variables = new HashMap<>();
        variables.put("input", userToMap(savedUser));

        // Body completo de la petición GraphQL
        Map<String, Object> body = new HashMap<>();
        body.put("query", mutation);
        body.put("variables", variables);

        try {
            webClient.post()
                    .uri(EXTERNAL_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
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

    private Map<String, Object> userToMap(Usuario user) {
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", user.getNombre());
        map.put("apellido", user.getApellido());
        map.put("email", user.getEmail());
        map.put("telefono", user.getTelefono());
        map.put("ci", user.getCi());
        map.put("password", user.getPassword());
        return map;
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
