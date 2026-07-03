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
                .requestMatchers("/login", "/css/**", "/js/**", "/images/**", "/tailwind.css").permitAll()
                // Rotas exclusivas do Porteiro
                .requestMatchers("/transportadora/**").hasAuthority("ROLE_PORTEIRO")
                .requestMatchers("/usuario/**").hasAuthority("ROLE_PORTEIRO")
                .requestMatchers("/unidade/**").hasAuthority("ROLE_PORTEIRO")
                .requestMatchers("/relatorios/**").hasAuthority("ROLE_PORTEIRO")
                
                // Rotas de Modificação de Encomenda e Aviso
                .requestMatchers("/encomenda/cadastrar", "/encomenda/remover/**").hasAuthority("ROLE_PORTEIRO")
                .requestMatchers("/encomenda/alterar/**").hasAnyAuthority("ROLE_PORTEIRO", "ROLE_MORADOR")
                .requestMatchers("/aviso/cadastrar", "/aviso/alterar/**", "/aviso/remover/**").hasAuthority("ROLE_PORTEIRO")
                
                // Moradores e Porteiros podem visualizar (qualquer um logado)
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
