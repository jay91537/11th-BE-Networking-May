package cotato.networking.weather_api.weather.dto.external;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OpenWeatherResponse {
	private Current current;
	private List<Hourly> hourly;
	private List<Daily> daily;

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Current {
		private long dt;
		private double temp;
		@JsonProperty("feels_like")
		private double feelsLike;
		private int humidity;
		@JsonProperty("wind_speed")
		private double windSpeed;
		@JsonProperty("wind_deg")
		private int windDeg;
		@JsonProperty("wind_gust")
		private double windGust;
		private double uvi;
		private long sunrise;
		private long sunset;
		private List<Weather> weather;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Hourly {
		private long dt;
		private double temp;
		@JsonProperty("feels_like")
		private double feelsLike;
		private int humidity;
		@JsonProperty("wind_speed")
		private double windSpeed;
		private double pop; // 강수 확률
		private List<Weather> weather;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Daily {
		private long dt;
		private Temp temp;
		@JsonProperty("feels_like")
		private FeelsLike feelsLike;
		private String summary;
		private int humidity;
		@JsonProperty("wind_speed")
		private double windSpeed;
		private double uvi;
		private long sunrise;
		private long sunset;
		private List<Weather> weather;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Weather {
		private String main;
		private String description;
		private String icon;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Temp {
		private double day;
		private double night;
		private double min;
		private double max;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class FeelsLike {
		private double day;
		private double night;
	}
}