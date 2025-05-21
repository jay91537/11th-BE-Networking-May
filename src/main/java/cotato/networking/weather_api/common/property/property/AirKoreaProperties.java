package cotato.networking.weather_api.common.property.property;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "airkorea")
public class AirKoreaProperties {
	private String dustUrl;
	private String stationUrl;
	private String serviceKey;
}


