package web.condominiodigital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.condominiodigital.model.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}
