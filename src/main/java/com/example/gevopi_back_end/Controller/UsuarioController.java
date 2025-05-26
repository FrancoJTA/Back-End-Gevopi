package com.example.gevopi_back_end.Controller;

import com.example.gevopi_back_end.Class.Acceso;
import com.example.gevopi_back_end.Entity.Usuario;
import com.example.gevopi_back_end.Service.UsuarioService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @MutationMapping
    public Boolean nuevoUsuarioGlobal(@Argument("input") Usuario input) {
        return usuarioService.registroGlobal(input);
    }

    @MutationMapping
    public Usuario registroUsuario(@Argument("input") Usuario input) {

        return usuarioService.registrarUsuario(input);
    }

    @MutationMapping
    public Acceso login(@Argument String ci, @Argument String password) {
        return usuarioService.login(ci, password);
    }

    @MutationMapping
    public String  actualizarPassword(@Argument int id, @Argument String password) {
        return usuarioService.actualizarPasswordTemporal(id, password);
    }

    @MutationMapping
    public Boolean activarAdmin(@Argument int id){
        return usuarioService.activarUsuario(id);
    }

    @MutationMapping
    public Boolean desactivarAdmin(@Argument int id){
        return usuarioService.desActivar(id);
    }

    @QueryMapping
    public List<Usuario> usuariosLista(){
        return usuarioService.obtenerUsuariosConRoles1y2();
    }
}