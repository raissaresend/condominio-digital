package web.condominiodigital.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                /**
                 * Regras de Autorização
                 * As permissões são aplicadas na ordem de declaração. Regras mais específicas
                 * devem preceder as regras mais gerais.
                 */
                .requestMatchers("/login", "/css/**", "/js/**", "/images/**", "/tailwind.css").permitAll()
                
                // Rotas Administrativas (Portaria)
                .requestMatchers("/transportadora/**", "/usuario/**", "/unidade/**", "/relatorios/**").hasAuthority("ROLE_PORTEIRO")
                
                // Gestão de Operações do Condomínio
                .requestMatchers("/encomenda/cadastrar", "/encomenda/remover/**").hasAuthority("ROLE_PORTEIRO")
                .requestMatchers("/encomenda/alterar/**").hasAnyAuthority("ROLE_PORTEIRO", "ROLE_MORADOR")
                .requestMatchers("/aviso/cadastrar", "/aviso/alterar/**", "/aviso/remover/**").hasAuthority("ROLE_PORTEIRO")
                
                // Require authentication for any other route
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );
            
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
