package com.fiap.eca.api_marcacao_consultas.controller;

import com.fiap.eca.api_marcacao_consultas.model.Usuario;
import com.fiap.eca.api_marcacao_consultas.repository.UsuarioRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class TesteController {

    private final UsuarioRepository usuarioRepository;

    public TesteController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/teste-conexao")
    public List<Usuario> testarConexao() {
        return usuarioRepository.findAll();
    }
}
