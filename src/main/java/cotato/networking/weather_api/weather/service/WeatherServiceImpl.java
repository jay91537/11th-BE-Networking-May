package cotato.networking.weather_api.weather.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import cotato.networking.weather_api.common.property.property.WeatherApiProperties;
import cotato.networking.weather_api.weather.dto.external.OpenWeatherResponse;
import cotato.networking.weather_api.weather.dto.response.WeatherResponse;
import cotato.networking.weather_api.weather.dto.response.WeatherResponse.CurrentWeather;
import cotato.networking.weather_api.weather.dto.response.WeatherResponse.DailyWeather;
import cotato.networking.weather_api.weather.dto.response.WeatherResponse.HourlyWeather;
import cotato.networking.weather_api.weather.dto.response.WeatherResponse.Weather;
import cotato.networking.weather_api.weather.exception.WeatherApiException;
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
	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
	private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public Mono<WeatherResponse> getWeatherData(double lat, double lon) {
		validateCoordinates(lat, lon);

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
			.map(this::mapToWeatherResponse)
			.onErrorMap(WebClientResponseException.class, e -> {
				log.error("날씨 API 호출 실패: {}", e.getMessage());
				if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
					return new WeatherApiException("API 키 인증 실패");
				} else if (e.getStatusCode().is4xxClientError()) {
					return new WeatherApiException("잘못된 요청 파라미터");
				} else {
					return new WeatherApiException("날씨 서비스 오류: " + e.getMessage());
				}
			});
	}

	private WeatherResponse mapToWeatherResponse(OpenWeatherResponse openWeatherResponse) {
		return WeatherResponse.builder()
			.current(mapCurrent(openWeatherResponse.getCurrent()))
			.hourly(mapHourly(openWeatherResponse.getHourly()))
			.daily(mapDaily(openWeatherResponse.getDaily()))
			.build();
	}

	private CurrentWeather mapCurrent(OpenWeatherResponse.Current current) {
		Weather weather = null;
		if (!current.getWeather().isEmpty()) {
			OpenWeatherResponse.Weather w = current.getWeather().get(0);
			weather = Weather.builder()
				.main(w.getMain())
				.icon(w.getIcon())
				.build();
		}

		return CurrentWeather.builder()
			.datetime(convertToLocalDateTime(current.getDt()))
			.temperature(roundToOneDecimal(current.getTemp()))
			.feelsLike(roundToOneDecimal(current.getFeelsLike()))
			.weather(weather)
			.humidity(current.getHumidity())
			.windSpeed(roundToOneDecimal(current.getWindSpeed()))
			.windDirection(getWindDirection(current.getWindDeg()))
			.uvIndex(getUvIndexDescription(current.getUvi()))
			.sunrise(convertToLocalDateTime(current.getSunrise()))
			.build();
	}

	private HourlyWeather[] mapHourly(java.util.List<OpenWeatherResponse.Hourly> hourlyList) {
		return hourlyList.stream()
			.limit(24) // 최대 24시간
			.map(h -> {
				String icon = "";
				if (!h.getWeather().isEmpty()) {
					icon = h.getWeather().get(0).getIcon();
				}

				return HourlyWeather.builder()
					.hour(convertToLocalDateTime(h.getDt()))
					.temperature(roundToOneDecimal(h.getTemp()))
					.icon(icon)
					.build();
			})
			.toArray(HourlyWeather[]::new);
	}

	private DailyWeather[] mapDaily(java.util.List<OpenWeatherResponse.Daily> dailyList) {
		return dailyList.stream()
			.limit(5) // 최대 5일
			.map(d -> {
				Weather weather = null;
				if (!d.getWeather().isEmpty()) {
					OpenWeatherResponse.Weather w = d.getWeather().get(0);
					weather = Weather.builder()
						.main(w.getMain())
						.icon(w.getIcon())
						.build();
				}

				return DailyWeather.builder()
					.date(convertToLocalDateTime(d.getDt()))
					.morningTemp(roundToOneDecimal(d.getTemp().getDay()))
					.afternoonTemp(roundToOneDecimal(d.getTemp().getNight()))
					.weather(weather)
					.build();
			})
			.toArray(DailyWeather[]::new);
	}

	private LocalDateTime convertToLocalDateTime(long timestamp) {
		return LocalDateTime.ofInstant(
			Instant.ofEpochSecond(timestamp),
			ZoneId.systemDefault());
	}

	private String getWindDirection(int degrees) {
		String[] directions = {"북풍", "북동풍", "동풍", "남동풍", "남풍", "남서풍", "서풍", "북서풍"};
		return directions[(int)Math.round(degrees % 360 / 45.0) % 8];
	}

	private String getUvIndexDescription(double uvi) {
		if (uvi < 3) {
			return "낮음";
		} else if (uvi < 6) {
			return "보통";
		} else if (uvi < 8) {
			return "높음";
		} else if (uvi < 11) {
			return "매우 높음";
		} else {
			return "위험";
		}
	}

	private double roundToOneDecimal(double value) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(1, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	private void validateCoordinates(double lat, double lon) {
		if (lat < -90 || lat > 90) {
			throw new IllegalArgumentException("위도는 -90에서 90 사이여야 합니다.");
		}
		if (lon < -180 || lon > 180) {
			throw new IllegalArgumentException("경도는 -180에서 180 사이여야 합니다.");
		}
	}
}