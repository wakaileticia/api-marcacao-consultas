package com.fiap.eca.api_marcacao_consultas.service;

import com.fiap.eca.api_marcacao_consultas.model.Usuario;
import com.fiap.eca.api_marcacao_consultas.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ====== CRUD ======

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public List<Usuario> listarMedicos() {
        return usuarioRepository.findByTipo("MEDICO");
    }

    public List<Usuario> buscarMedicosPorEspecialidade(String especialidade) {
        return usuarioRepository.findByTipoAndEspecialidade("MEDICO", especialidade);
    }

    public Usuario salvarUsuario(Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioExistente.isPresent()) {
            throw new RuntimeException("Erro: Este e-mail já está cadastrado.");
        }

        // Criptografa a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizarUsuario(Long id, Usuario usuario) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!usuarioExistente.getEmail().equals(usuario.getEmail())) {
            Optional<Usuario> usuarioComMesmoEmail = usuarioRepository.findByEmail(usuario.getEmail());
            if (usuarioComMesmoEmail.isPresent()) {
                throw new RuntimeException("Erro: Este e-mail já está sendo usado por outro usuário.");
            }
        }

        usuarioExistente.setNome(usuario.getNome());
        usuarioExistente.setEmail(usuario.getEmail());

        if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
            usuarioExistente.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        if (usuario.getTipo() != null && !usuario.getTipo().isEmpty()) {
            usuarioExistente.setTipo(usuario.getTipo());
        }

        return usuarioRepository.save(usuarioExistente);
    }

    public void excluirUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    // ====== AUTENTICAÇÃO ======

    public Usuario autenticar(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new RuntimeException("Senha incorreta");
        }
        System.out.println("Tipo de encoder em uso: " + passwordEncoder.getClass().getName());
        System.out.println("Hash do banco: " + usuario.getSenha());
        System.out.println("Senha digitada: " + senha);
        System.out.println("Resultado match: " + passwordEncoder.matches(senha, usuario.getSenha()));


        return usuario;
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }


    // Método para admin alterar senha de qualquer usuário
    public Usuario alterarSenha(Long usuarioId, String novaSenha) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        return usuarioRepository.save(usuario);
    }
}
