package cotato.networking.weather_api.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import cotato.networking.weather_api.auth.dto.LoginRequest;
import cotato.networking.weather_api.auth.dto.LoginResponse;
import cotato.networking.weather_api.auth.dto.SignUpRequest;
import cotato.networking.weather_api.user.repository.UserRepository;
import cotato.networking.weather_api.common.error.ErrorCode;
import cotato.networking.weather_api.common.error.exception.AppException;
import cotato.networking.weather_api.common.security.CustomUserDetails;
import cotato.networking.weather_api.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public Long signUp(SignUpRequest request) {
		if (userRepository.existsByLoginId(request.loginId())) {
			throw new AppException(ErrorCode.USER_INPUT_EXCEPTION);
		}

		User user = User.createUser(request, passwordEncoder);

		return userRepository.save(user).getId();
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
