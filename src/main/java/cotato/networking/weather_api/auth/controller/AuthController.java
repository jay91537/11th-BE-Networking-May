package cotato.networking.weather_api.auth.controller;

import cotato.networking.weather_api.auth.service.AuthService;
import cotato.networking.weather_api.auth.dto.LoginRequest;
import cotato.networking.weather_api.auth.dto.LoginResponse;
import cotato.networking.weather_api.auth.dto.SignUpRequest;
import cotato.networking.weather_api.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/signup")
	public ApiResponse<Long> signUp(@Valid @RequestBody SignUpRequest request) {
		return ApiResponse.created(authService.signUp(request));
	}

	@PostMapping("/login")
	public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
		return ApiResponse.ok(authService.login(request, session));
	}

	@PostMapping("/logout")
	public ApiResponse<Void> logout(HttpServletRequest request, HttpServletResponse response) {
		authService.logout(request, response);
		return ApiResponse.noContent();
	}

}
