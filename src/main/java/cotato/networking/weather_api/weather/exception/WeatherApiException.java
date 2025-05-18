package cotato.networking.weather_api.weather.exception;

public class WeatherApiException extends RuntimeException {

	public WeatherApiException(String message) {
		super(message);
	}

	public WeatherApiException(String message, Throwable cause) {
		super(message, cause);
	}
}