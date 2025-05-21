package cotato.networking.weather_api.location.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LocationRequest(
	@NotBlank(message = "위치명은 필수입니다.")
	@Size(min = 2, max = 10, message = "위치명은 2자 이상 10자 이하이어야 합니다.")
	String locationName,

	@Min(value = -90, message = "위도는 -90도에서 90도 사이여야 합니다.")
	@Max(value = 90, message = "위도는 -90도에서 90도 사이여야 합니다.")
	Double latitude,

	@Min(value = -180, message = "경도는 -180도에서 180도 사이여야 합니다.") @Max(value = 180, message = "경도는 -180도에서 180도 사이여야 합니다.")
	Double longitude
) {
}