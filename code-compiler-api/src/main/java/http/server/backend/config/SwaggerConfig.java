package http.server.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI SwaggerApi() {
        return new OpenAPI().info(new Info()
                .title("Task API")
                .version("1.0")
                .description("Documentation of the API using Swagger OpenAPI"));
    }
}
