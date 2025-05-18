package cotato.networking.weather_api.weather.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cotato.networking.weather_api.common.response.ApiResponse;
import cotato.networking.weather_api.weather.dto.response.WeatherResponse;
import cotato.networking.weather_api.weather.exception.WeatherApiException;
import cotato.networking.weather_api.weather.service.WeatherService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/api/weather")
public class WeatherController {

	private final WeatherService weatherService;

	@GetMapping
	public Mono<ResponseEntity<ApiResponse<WeatherResponse>>> getWeather(
		@RequestParam double lat,
		@RequestParam double lon) {
		return weatherService.getWeatherData(lat, lon)
			.map(data -> ResponseEntity.ok(ApiResponse.ok(data)))
			.onErrorResume(IllegalArgumentException.class,
				e -> Mono.just(ResponseEntity.badRequest().body(ApiResponse.of(HttpStatus.BAD_REQUEST, null))))
			.onErrorResume(WeatherApiException.class,
				e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null))));
	}
}