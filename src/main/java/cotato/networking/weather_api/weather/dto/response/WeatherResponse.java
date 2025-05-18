package cotato.networking.weather_api.weather.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WeatherResponse {
	private CurrentWeather current;
	private HourlyWeather[] hourly;
	private DailyWeather[] daily;

	@Builder
	public WeatherResponse(CurrentWeather current, HourlyWeather[] hourly, DailyWeather[] daily) {
		this.current = current;
		this.hourly = hourly;
		this.daily = daily;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class CurrentWeather {
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
		private LocalDateTime datetime;
		private double temperature;
		private double feelsLike;
		private Weather weather;
		private int humidity;
		private double windSpeed;
		private String windDirection;
		private String uvIndex;
		@JsonFormat(pattern = "HH:mm")
		private LocalDateTime sunrise;

		@Builder
		public CurrentWeather(LocalDateTime datetime, double temperature, double feelsLike,
			Weather weather, int humidity, double windSpeed,
			String windDirection, String uvIndex, LocalDateTime sunrise) {
			this.datetime = datetime;
			this.temperature = temperature;
			this.feelsLike = feelsLike;
			this.weather = weather;
			this.humidity = humidity;
			this.windSpeed = windSpeed;
			this.windDirection = windDirection;
			this.uvIndex = uvIndex;
			this.sunrise = sunrise;
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class HourlyWeather {
		@JsonFormat(pattern = "HH:mm")
		private LocalDateTime hour;
		private double temperature;
		private String icon;

		@Builder
		public HourlyWeather(LocalDateTime hour, double temperature, String icon) {
			this.hour = hour;
			this.temperature = temperature;
			this.icon = icon;
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class DailyWeather {
		@JsonFormat(pattern = "MM.dd")
		private LocalDateTime date;
		private double morningTemp;
		private double afternoonTemp;
		private Weather weather;

		@Builder
		public DailyWeather(LocalDateTime date, double morningTemp, double afternoonTemp, Weather weather) {
			this.date = date;
			this.morningTemp = morningTemp;
			this.afternoonTemp = afternoonTemp;
			this.weather = weather;
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Weather {
		private String main;
		private String icon;

		@Builder
		public Weather(String main, String icon) {
			this.main = main;
			this.icon = icon;
		}
	}
}