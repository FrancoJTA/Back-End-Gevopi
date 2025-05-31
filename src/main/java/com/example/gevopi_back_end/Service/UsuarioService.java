package com.example.gevopi_back_end.Service;

import com.example.gevopi_back_end.Class.Acceso;
import com.example.gevopi_back_end.Entity.Rol;
import com.example.gevopi_back_end.Repository.RolRepository;
import com.example.gevopi_back_end.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import com.example.gevopi_back_end.Entity.Usuario;
import com.example.gevopi_back_end.Repository.UsuarioRepository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private JwtUtil jwtUtil;

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

    public Usuario registroGlobal(Usuario usuario) {
        if (usuarioRepository.existsByEmailOrCi(usuario.getEmail(), usuario.getCi())) {
            throw new RuntimeException("El email o CI ya están registrados");
        }

        usuario.setActivo(false);

        Rol rol = rolRepository.findById(2)
                .orElseThrow(() -> new RuntimeException("Rol con ID 2 no encontrado"));

        usuario.setRoles(Collections.singleton(rol));

        return usuarioRepository.save(usuario);

    }

    public Acceso login(String ci, String password) {
        Acceso acceso = new Acceso();

        Optional<Usuario> optionalUsuario = usuarioRepository.findByCi(ci);

        if (optionalUsuario.isEmpty()) {
            acceso.setAcceso(false);
            acceso.setToken("");
            return acceso;
        }

        Usuario usuario = optionalUsuario.get();

        if (!usuario.getPassword().equals(password)) {
            acceso.setAcceso(false);
            acceso.setToken("");
            return acceso;
        }

        boolean tieneRolPermitido = usuario.getRoles().stream()
                .anyMatch(rol -> rol.getId() == 1 || rol.getId() == 2);

        if (!tieneRolPermitido) {
            acceso.setAcceso(false);
            acceso.setToken("");
            return acceso;
        }

        acceso.setAcceso(true);
        acceso.setId(usuario.getId());
        if (usuario.getActivo()) {
            String token = jwtUtil.generateToken(usuario.getCi());
            acceso.setToken(token);
        } else {
            acceso.setToken("");
        }

        return acceso;
    }


    public String actualizarPasswordTemporal(int id, String password){

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setActivo(true);
        usuario.setPassword(password);
        usuarioRepository.save(usuario);
        String token = jwtUtil.generateToken(usuario.getCi());

        return token;
    }

    public Boolean activarUsuario(int id) {
        Usuario user=usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Id no encontrado"));
        if(user.getPassword() == null || user.getPassword().isEmpty() || user.getPassword().isBlank() ){

            String otp = generateOTP();

            user.setPassword(otp);
            usuarioRepository.save(user);
            return true;
        }
        else {
            return false;
        }
    }

    public Boolean desActivar(int id) {
        Usuario user=usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Id no encontrado"));
        if(user.getActivo()){
            user.setActivo(false);
            user.setPassword("");
            usuarioRepository.save(user);
            return true;
        }
        else {
            return false;
        }
    }

    private String generateOTP() {
        Random random = new Random();
        return String.format("%05d", random.nextInt(100000));
    }

    public List<Usuario> obtenerUsuariosConRoles1() {
        List<Usuario> todos = usuarioRepository.findAll();

        return todos.stream()
                .filter(usuario -> usuario.getRoles().stream()
                        .anyMatch(rol ->  rol.getId() == 2))
                .collect(Collectors.toList());
    }
}
