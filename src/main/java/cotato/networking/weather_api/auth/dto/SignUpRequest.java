package cotato.networking.weather_api.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
	@NotBlank(message = "아이디는 4자 이상 20자 이하로 입력해 주세요.") @Size(min = 4, max = 20) String loginId,
	@NotBlank(message = "비밀번호는 8자 이상으로 입력해 주세요.") @Size(min = 8) String password) {
}

