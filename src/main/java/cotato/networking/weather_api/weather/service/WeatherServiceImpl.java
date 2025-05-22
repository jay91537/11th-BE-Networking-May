package cotato.networking.weather_api.weather.service;

import java.net.URI;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import cotato.networking.weather_api.common.error.ErrorCode;
import cotato.networking.weather_api.common.error.exception.AppException;
import cotato.networking.weather_api.common.property.property.WeatherApiProperties;
import cotato.networking.weather_api.weather.dto.external.OpenWeatherResponse;
import cotato.networking.weather_api.weather.dto.response.WeatherResponse;
import cotato.networking.weather_api.weather.mapper.WeatherMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class WeatherServiceImpl implements WeatherService {

	private final RestTemplate restTemplate;
	private final WeatherApiProperties weatherApiProperties;
	private final WeatherMapper weatherMapper;

	@Override
	public WeatherResponse getWeatherData(final double lat, final double lon) {
		URI uri = UriComponentsBuilder
			.fromHttpUrl(weatherApiProperties.getBaseUrl())
			.path(weatherApiProperties.getPath())
			.queryParam("lat", lat)
			.queryParam("lon", lon)
			.queryParam("exclude", "minutely")
			.queryParam("appid", weatherApiProperties.getKey())
			.queryParam("units", "metric")
			.build()
			.toUri();

		try {
			ResponseEntity<OpenWeatherResponse> response = restTemplate.exchange(
				uri,
				HttpMethod.GET,
				null,
				OpenWeatherResponse.class
			);

			return weatherMapper.mapToWeatherResponse(response.getBody());

		} catch (HttpClientErrorException e) {
			log.error("날씨 API 클라이언트 에러: {}", e.getMessage());
			if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				throw new AppException(ErrorCode.WEATHER_API_AUTH_ERROR);
			} else {
				throw new AppException(ErrorCode.WEATHER_API_BAD_REQUEST);
			}
		} catch (HttpServerErrorException e) {
			log.error("날씨 API 서버 에러: {}", e.getMessage());
			throw new AppException(ErrorCode.WEATHER_API_SERVER_ERROR);
		} catch (RestClientException e) {
			log.error("날씨 API 호출 실패: {}", e.getMessage());
			throw new AppException(ErrorCode.WEATHER_API_SERVER_ERROR);
		}
	}
}