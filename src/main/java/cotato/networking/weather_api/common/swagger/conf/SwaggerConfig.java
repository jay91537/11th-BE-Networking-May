package cotato.networking.weather_api.common.swagger.conf;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cotato.networking.weather_api.common.property.property.SwaggerProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

	private final SwaggerProperties swaggerProperties;

	@Bean
	public OpenAPI openAPI() {

		SecurityScheme cookieScheme = new SecurityScheme()
			.type(SecurityScheme.Type.APIKEY)
			.in(SecurityScheme.In.COOKIE).name("JSESSIONID");

		return new OpenAPI()
			.addSecurityItem(new SecurityRequirement().addList("cookieAuth"))
			.components(new Components().addSecuritySchemes("cookieAuth", cookieScheme))
			.info(new Info().title("API Docs").version("v1"));
	}

	private Info apiInfo() {
		return new Info()
			.title("Networking 6조 api 명세서")
			.description("Networking 6조 명세서입니다.")
			.version("1.0.0");
	}
}