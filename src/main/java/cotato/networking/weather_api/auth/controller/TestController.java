package cotato.networking.weather_api.auth.controller;

import cotato.networking.weather_api.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@SecurityRequirement(name = "cookieAuth")
	@GetMapping("/test/hello")
	public ApiResponse<String> hello(Authentication authentication) {

		String loginId = authentication.getName();
		return ApiResponse.ok("Hello, user" + loginId);
	}
}
