package tacos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import tacos.User;
import tacos.data.UserRepository;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> {
            User user = userRepo.findByUsername(username);
            if (user != null) return user;
            throw new UsernameNotFoundException(
                    "User '" + username + "' not found");
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests()
                .antMatchers("/design", "/orders")
                .access("hasRole('USER')")
                .antMatchers(HttpMethod.POST, "/api/ingredients")
                .hasAuthority("SCOPE_writeIngredients")
                .antMatchers(HttpMethod.DELETE, "/api/ingredients")
                .hasAuthority("SCOPE_deleteIngredients")
                .antMatchers("/", "/**").access("permitAll()")
                .and()
                .oauth2ResourceServer(oauth2 -> oauth2.jwt())
                .httpBasic()
                .realmName("Taco Cloud")
                .and()
                .logout().logoutSuccessUrl("/")
                .and()
                .csrf().ignoringAntMatchers("/h2-console/**", "/api/**")
                // Allow pages to be loaded in frames from the same origin; needed for H2-Console
                .and().headers().frameOptions().sameOrigin()
                .and().build();
    }
}
