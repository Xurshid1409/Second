package second.education.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import second.education.security.UserDetailsImpl;

/*@EnableJpaAuditing
@Configuration*/
public class AuditConfiguration {

    /*@Bean
    public String auditorRef() {

                Authentication authentication = SecurityContextHolder.getContext()
              .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return ((UserDetailsImpl) authentication.getPrincipal()).getUsername();

        };
    }*/
}