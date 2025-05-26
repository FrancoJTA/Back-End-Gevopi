package com.example.gevopi_back_end.Controller;

import com.example.gevopi_back_end.Entity.Usuario;
import com.example.gevopi_back_end.Service.UsuarioService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @MutationMapping
    public Usuario nuevoUsuarioGlobal(@Argument("input") Usuario input) {
        return usuarioService.registroGlobal(input);
    }

    @MutationMapping
    public Usuario registroUsuario(@Argument("input") Usuario input) {
        return usuarioService.registrarUsuario(input);
    }


    //arreglar
    @MutationMapping
    public Usuario login(@Argument String ci, @Argument String password) {
        return usuarioService.login(ci, password);
    }
}