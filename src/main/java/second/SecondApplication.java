package second;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Second-Edu Api", version = "3.0", description = "Second-Edu"))
@SecurityScheme(name = "second", scheme = "Bearer", bearerFormat = "JWT",
        type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@EnableJpaRepositories("second.education.repository")
@EntityScan(basePackages = "second.education.domain")
public class SecondApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecondApplication.class, args);
    }

}
