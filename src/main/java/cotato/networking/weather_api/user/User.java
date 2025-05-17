package cotato.networking.weather_api.user;

import cotato.networking.weather_api.auth.dto.SignUpRequest;
import jakarta.persistence.*;
import lombok.*;

import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
@AllArgsConstructor
@Builder
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(nullable = false, name = "login_id")
	private String loginId;

	@Column(nullable = false, name = "password")
	private String password;

	public static User createUser(SignUpRequest request, PasswordEncoder encoder) {
		return User.builder()
			.loginId(request.loginId())
			.password(encoder.encode(request.password()))
			.build();
	}
}
