package cotato.networking.weather_api.common.property.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import cotato.networking.weather_api.common.property.property.SwaggerProperties;
import cotato.networking.weather_api.common.property.property.WeatherApiProperties;

// 전역적으로 사용되는 상수
@Configuration
@EnableConfigurationProperties(value = {
	SwaggerProperties.class,
	WeatherApiProperties.class,
})
public class PropertyConfig {
}