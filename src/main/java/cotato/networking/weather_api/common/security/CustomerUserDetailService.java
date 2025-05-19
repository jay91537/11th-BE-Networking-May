package cotato.networking.weather_api.common.security;

import cotato.networking.weather_api.auth.repository.AuthRepository;
import cotato.networking.weather_api.common.error.ErrorCode;
import cotato.networking.weather_api.common.error.exception.AppException;
import cotato.networking.weather_api.user.User;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerUserDetailService implements UserDetailsService {

	private final AuthRepository authRepository;

	@Override
	public UserDetails loadUserByUsername(String username) {
		User user = authRepository.findByLoginId(username)
			.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

		List<GrantedAuthority> authorities =
			List.of(new SimpleGrantedAuthority("ROLE_USER"));

		return new CustomUserDetails(
			user.getId(),
			user.getLoginId(),
			user.getPassword(),
			authorities
		);
	}
}
