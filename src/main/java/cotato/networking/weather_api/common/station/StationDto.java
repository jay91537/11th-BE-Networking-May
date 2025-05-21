package cotato.networking.weather_api.common.station;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StationDto {

	private final String stationName;
	private final String stationCode;
	private final String address;
	private final double distanceTm;
}
