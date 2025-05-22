package cotato.networking.weather_api.weather.service;

import cotato.networking.weather_api.weather.dto.response.WeatherResponse;

public interface WeatherService {

	WeatherResponse getWeatherData(double lat, double lon);
}