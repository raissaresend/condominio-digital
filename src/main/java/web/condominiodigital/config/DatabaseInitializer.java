package web.condominiodigital.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import web.condominiodigital.model.Unidade;
import web.condominiodigital.model.Usuario;
import web.condominiodigital.repository.UnidadeRepository;
import web.condominiodigital.repository.UsuarioRepository;

@Configuration
public class DatabaseInitializer {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, UnidadeRepository unidadeRepository) {
        return args -> {
            if (usuarioRepository.count() == 0) {
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("123456"));
                admin.setRole("ROLE_PORTEIRO");
                usuarioRepository.save(admin);
            }
            
            if (unidadeRepository.count() == 0) {
                Unidade u1 = new Unidade();
                u1.setIdentificacao("Apto 101");
                unidadeRepository.save(u1);
                
                Unidade u2 = new Unidade();
                u2.setIdentificacao("Apto 102");
                unidadeRepository.save(u2);
            }
        };
    }
}
