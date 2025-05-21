package cotato.networking.weather_api.weather.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cotato.networking.weather_api.common.response.ApiResponse;
import cotato.networking.weather_api.weather.dto.response.WeatherResponse;
import cotato.networking.weather_api.weather.service.WeatherService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/api/weather")
public class WeatherController {

	private final WeatherService weatherService;

	@GetMapping
	public Mono<ResponseEntity<ApiResponse<WeatherResponse>>> getWeather(
		@RequestParam(required = true) @Min(value = -90, message = "위도는 -90도 이상이어야 합니다") @Max(value = 90, message = "위도는 90도 이하여야 합니다") final double lat,
		@RequestParam(required = true) @Min(value = -180, message = "경도는 -180도 이상이어야 합니다") @Max(value = 180, message = "경도는 180도 이하여야 합니다") final double lon) {
		return weatherService.getWeatherData(lat, lon)
			.map(data -> ResponseEntity.ok(ApiResponse.ok(data)));
	}
}