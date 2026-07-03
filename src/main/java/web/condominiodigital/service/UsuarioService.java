package web.condominiodigital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import web.condominiodigital.model.Usuario;
import web.condominiodigital.repository.UsuarioRepository;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorCodigo(Long codigo) {
        return usuarioRepository.findById(codigo).orElse(null);
    }

    public void salvar(Usuario usuario) {
        if (usuario.getCodigo() == null) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        } else {
            Usuario usuarioExistente = buscarPorCodigo(usuario.getCodigo());
            if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
                usuario.setPassword(usuarioExistente.getPassword());
            } else {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
        }
        usuarioRepository.save(usuario);
    }

    public void excluir(Long codigo) {
        usuarioRepository.deleteById(codigo);
    }
}
