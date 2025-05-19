package cotato.networking.weather_api.auth.service;

import cotato.networking.weather_api.auth.repository.AuthRepository;
import cotato.networking.weather_api.auth.dto.LoginRequest;
import cotato.networking.weather_api.auth.dto.LoginResponse;
import cotato.networking.weather_api.auth.dto.SignUpRequest;
import cotato.networking.weather_api.common.error.ErrorCode;
import cotato.networking.weather_api.common.error.exception.AppException;
import cotato.networking.weather_api.common.security.CustomUserDetails;
import cotato.networking.weather_api.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final AuthRepository authRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public Long signUp(SignUpRequest request) {
		if (authRepository.existsByLoginId(request.loginId())) {
			throw new AppException(ErrorCode.USER_INPUT_EXCEPTION);
		}

		User user = User.createUser(request, passwordEncoder);

		return authRepository.save(user).getId();
	}

	@Transactional
	public LoginResponse login(LoginRequest request, HttpSession session) {

		UsernamePasswordAuthenticationToken authToken =
			new UsernamePasswordAuthenticationToken(
				request.loginId(),
				request.password()
			);

		Authentication authentication = authenticationManager.authenticate(authToken);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		session.setAttribute(
			HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
			SecurityContextHolder.getContext()
		);

		CustomUserDetails principal = (CustomUserDetails)authentication.getPrincipal();
		return new LoginResponse(principal.getId());
	}

	public void logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		new SecurityContextLogoutHandler().logout(request, response, authentication);
	}
}
