package cotato.networking.weather_api.weather.service;

import cotato.networking.weather_api.weather.dto.response.WeatherResponse;
import reactor.core.publisher.Mono;

public interface WeatherService {

	Mono<WeatherResponse> getWeatherData(double lat, double lon);
}