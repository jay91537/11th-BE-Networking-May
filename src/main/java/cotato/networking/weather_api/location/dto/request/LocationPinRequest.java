package cotato.networking.weather_api.location.dto.request;

import jakarta.validation.constraints.NotNull;

public record LocationPinRequest(
	@NotNull(message = "위치 ID는 필수입니다") Long locationId,

	@NotNull(message = "핀 상태는 필수입니다") Boolean isPinned) {
}
