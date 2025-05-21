package cotato.networking.weather_api.common.security;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import cotato.networking.weather_api.user.repository.UserRepository;
import cotato.networking.weather_api.common.error.ErrorCode;
import cotato.networking.weather_api.common.error.exception.AppException;
import cotato.networking.weather_api.user.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerUserDetailService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) {
		User user = userRepository.findByLoginId(username)
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
