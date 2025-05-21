package cotato.networking.weather_api.weather.mapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Component;

import cotato.networking.weather_api.weather.dto.external.OpenWeatherResponse;
import cotato.networking.weather_api.weather.dto.response.WeatherResponse;
import cotato.networking.weather_api.weather.dto.response.WeatherResponse.CurrentWeather;
import cotato.networking.weather_api.weather.dto.response.WeatherResponse.DailyWeather;
import cotato.networking.weather_api.weather.dto.response.WeatherResponse.HourlyWeather;
import cotato.networking.weather_api.weather.dto.response.WeatherResponse.Weather;

@Component
public class WeatherMapper {

	public WeatherResponse mapToWeatherResponse(final OpenWeatherResponse openWeatherResponse) {
		return WeatherResponse.builder()
			.current(mapCurrent(openWeatherResponse.getCurrent()))
			.hourly(mapHourly(openWeatherResponse.getHourly()))
			.daily(mapDaily(openWeatherResponse.getDaily()))
			.build();
	}

	private CurrentWeather mapCurrent(final OpenWeatherResponse.Current current) {
		final Weather weather = !current.getWeather().isEmpty()
			? Weather.builder()
			.main(current.getWeather().get(0).getMain())
			.icon(current.getWeather().get(0).getIcon())
			.build()
			: null;

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

	private HourlyWeather[] mapHourly(final java.util.List<OpenWeatherResponse.Hourly> hourlyList) {
		return hourlyList.stream()
			.limit(24) // 최대 24시간
			.map(h -> {
				final String icon = !h.getWeather().isEmpty() ? h.getWeather().get(0).getIcon() : "";

				return HourlyWeather.builder()
					.hour(convertToLocalDateTime(h.getDt()))
					.temperature(roundToOneDecimal(h.getTemp()))
					.icon(icon)
					.build();
			})
			.toArray(HourlyWeather[]::new);
	}

	private DailyWeather[] mapDaily(final java.util.List<OpenWeatherResponse.Daily> dailyList) {
		return dailyList.stream()
			.limit(5) // 최대 5일
			.map(d -> {
				final Weather weather = !d.getWeather().isEmpty()
					? Weather.builder()
					.main(d.getWeather().get(0).getMain())
					.icon(d.getWeather().get(0).getIcon())
					.build()
					: null;

				return DailyWeather.builder()
					.date(convertToLocalDateTime(d.getDt()))
					.morningTemp(roundToOneDecimal(d.getTemp().getDay()))
					.afternoonTemp(roundToOneDecimal(d.getTemp().getNight()))
					.weather(weather)
					.build();
			})
			.toArray(DailyWeather[]::new);
	}

	private LocalDateTime convertToLocalDateTime(final long timestamp) {
		return LocalDateTime.ofInstant(
			Instant.ofEpochSecond(timestamp),
			ZoneId.systemDefault());
	}

	private String getWindDirection(final int degrees) {
		final String[] directions = {"북풍", "북동풍", "동풍", "남동풍", "남풍", "남서풍", "서풍", "북서풍"};
		return directions[(int)Math.round(degrees % 360 / 45.0) % 8];
	}

	private String getUvIndexDescription(final double uvi) {
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

	private double roundToOneDecimal(final double value) {
		final BigDecimal bd = new BigDecimal(value);
		final BigDecimal rounded = bd.setScale(1, RoundingMode.HALF_UP);
		return rounded.doubleValue();
	}
}