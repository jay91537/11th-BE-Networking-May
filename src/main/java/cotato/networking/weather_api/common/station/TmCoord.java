package cotato.networking.weather_api.common.station;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TmCoord {

	private final double x;
	private final double y;
}
