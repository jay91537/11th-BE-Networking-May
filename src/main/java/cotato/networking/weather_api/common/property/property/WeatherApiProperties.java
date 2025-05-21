package cotato.networking.weather_api.common.property.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "weather.api")
public class WeatherApiProperties {
	private String baseUrl;
	private String path;
	private String key;
	private int connectTimeout;
	private int readTimeout;
	private int writeTimeout;
}