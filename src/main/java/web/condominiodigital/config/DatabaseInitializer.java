package web.condominiodigital.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import web.condominiodigital.model.*;
import web.condominiodigital.repository.*;

import java.time.LocalDateTime;

@Configuration
public class DatabaseInitializer {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initDatabase(
            UsuarioRepository usuarioRepository, 
            UnidadeRepository unidadeRepository,
            AvisoMuralRepository avisoMuralRepository,
            TransportadoraRepository transportadoraRepository,
            EncomendaRepository encomendaRepository) {
        return args -> {
            // Usuarios
            if (usuarioRepository.count() == 0) {
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("123456"));
                admin.setRole("ROLE_PORTEIRO");
                usuarioRepository.save(admin);
                
                Usuario user = new Usuario();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("123456"));
                user.setRole("ROLE_MORADOR");
                usuarioRepository.save(user);
            }
            
            // Unidades
            Unidade u1 = null;
            if (unidadeRepository.count() == 0) {
                u1 = new Unidade();
                u1.setIdentificacao("Apto 101");
                u1 = unidadeRepository.save(u1);
                
                Unidade u2 = new Unidade();
                u2.setIdentificacao("Apto 102");
                unidadeRepository.save(u2);
            } else {
                if(!unidadeRepository.findAll().isEmpty()) {
                    u1 = unidadeRepository.findAll().get(0);
                }
            }
            
            // Avisos
            if (avisoMuralRepository.count() == 0) {
                AvisoMural aviso1 = new AvisoMural();
                aviso1.setTitulo("Manutenção da Piscina");
                aviso1.setConteudo("Informamos que a piscina estará fechada para limpeza e manutenção de rotina nesta sexta-feira.");
                aviso1.setDataPublicacao(LocalDateTime.now().minusDays(1));
                avisoMuralRepository.save(aviso1);

                AvisoMural aviso2 = new AvisoMural();
                aviso2.setTitulo("Assembleia Geral Extraordinária");
                aviso2.setConteudo("Convocamos todos os moradores para a assembleia no dia 20, às 19h no salão de festas. Pauta: Votação da nova pintura.");
                aviso2.setDataPublicacao(LocalDateTime.now().minusDays(3));
                avisoMuralRepository.save(aviso2);
            }

            // Transportadoras
            Transportadora t1 = null;
            if (transportadoraRepository.count() == 0) {
                t1 = new Transportadora();
                t1.setNome("Correios");
                t1.setCnpj("34.028.316/0001-03");
                t1 = transportadoraRepository.save(t1);

                Transportadora t2 = new Transportadora();
                t2.setNome("Mercado Livre Logística");
                t2.setCnpj("03.007.331/0001-41");
                transportadoraRepository.save(t2);
            } else {
                if(!transportadoraRepository.findAll().isEmpty()) {
                    t1 = transportadoraRepository.findAll().get(0);
                }
            }

            // Encomendas
            if (encomendaRepository.count() == 0 && u1 != null && t1 != null) {
                Encomenda enc1 = new Encomenda();
                enc1.setDescricao("Caixa Média - Amazon Prime");
                enc1.setDataRecebimento(LocalDateTime.now().minusHours(2));
                enc1.setStatus(StatusEncomenda.AGUARDANDO_RETIRADA);
                enc1.setUnidade(u1);
                enc1.setTransportadora(t1);
                encomendaRepository.save(enc1);
                
                Encomenda enc2 = new Encomenda();
                enc2.setDescricao("Envelope - Cartão de Crédito");
                enc2.setDataRecebimento(LocalDateTime.now().minusDays(1));
                enc2.setStatus(StatusEncomenda.ENTREGUE);
                enc2.setUnidade(u1);
                enc2.setTransportadora(t1);
                encomendaRepository.save(enc2);
            }
        };
    }
}
