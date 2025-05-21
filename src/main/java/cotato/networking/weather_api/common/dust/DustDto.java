package cotato.networking.weather_api.common.dust;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class DustDto {

	private String stationName;     // 측정소 이름
	private String dataTime;        // 데이터 시각

	private String pm10Value;       // PM10 농도
	private String pm10Grade;       // PM10 지수(1~4)

	private String pm25Value;       // PM2.5 농도
	private String pm25Grade;       // PM2.5 지수(1~4)
}
