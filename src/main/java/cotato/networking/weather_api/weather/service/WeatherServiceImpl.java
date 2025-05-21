package cotato.networking.weather_api.weather.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import cotato.networking.weather_api.common.error.ErrorCode;
import cotato.networking.weather_api.common.error.exception.AppException;
import cotato.networking.weather_api.common.property.property.WeatherApiProperties;
import cotato.networking.weather_api.weather.dto.external.OpenWeatherResponse;
import cotato.networking.weather_api.weather.dto.response.WeatherResponse;
import cotato.networking.weather_api.weather.mapper.WeatherMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class WeatherServiceImpl implements WeatherService {

	private final WebClient weatherWebClient;
	private final WeatherApiProperties weatherApiProperties;
	private final WeatherMapper weatherMapper;

	@Override
	public Mono<WeatherResponse> getWeatherData(final double lat, final double lon) {
		return weatherWebClient.get()
			.uri(uriBuilder -> uriBuilder
				.path(weatherApiProperties.getPath())
				.queryParam("lat", lat)
				.queryParam("lon", lon)
				.queryParam("exclude", "minutely")
				.queryParam("appid", weatherApiProperties.getKey())
				.queryParam("units", "metric")
				.build())
			.retrieve()
			.bodyToMono(OpenWeatherResponse.class)
			.map(weatherMapper::mapToWeatherResponse)
			.onErrorMap(WebClientResponseException.class, e -> {
				log.error("날씨 API 호출 실패: {}", e.getMessage());
				if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
					return new AppException(ErrorCode.WEATHER_API_AUTH_ERROR);
				} else if (e.getStatusCode().is4xxClientError()) {
					return new AppException(ErrorCode.WEATHER_API_BAD_REQUEST);
				} else {
					return new AppException(ErrorCode.WEATHER_API_SERVER_ERROR);
				}
			});
	}
}