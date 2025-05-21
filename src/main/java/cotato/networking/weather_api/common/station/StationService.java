package cotato.networking.weather_api.common.station;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import cotato.networking.weather_api.common.error.ErrorCode;
import cotato.networking.weather_api.common.error.exception.AppException;
import cotato.networking.weather_api.common.property.property.AirKoreaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class StationService {
	private final CoordService coordService;
	private final AirKoreaProperties airKoreaProperties;
	private final RestTemplate restTemplate;

	public StationDto nearestStation(double lat, double lon) {

		TmCoord tm = coordService.toTm(lat, lon);

		URI uri = UriComponentsBuilder
			.fromHttpUrl(airKoreaProperties.getStationUrl())
			.queryParam("serviceKey", airKoreaProperties.getServiceKey())
			.queryParam("returnType", "json")
			.queryParam("tmX", tm.getX())
			.queryParam("tmY", tm.getY())
			.queryParam("ver", 1.1)
			.build(true)
			.toUri();

		Map<?, ?> json = restTemplate.getForObject(uri, Map.class);
		Map<?, ?> body = (Map<?, ?>)((Map<?, ?>)json.get("response")).get("body");

		// 관측소들이 가까운 순으로 정렬되어 리스트로 반환되므로 첫 번째 원소만 가져옴
		Map<?, ?> nearStation = ((List<Map<?, ?>>)body.get("items")).get(0);

		if (nearStation == null || nearStation.isEmpty()) {
			throw new AppException(ErrorCode.STATION_FETCH_FAILURE);
		}

		return StationDto.builder()
			.stationName((String)nearStation.get("stationName"))
			.stationCode((String)nearStation.get("stationCode"))
			.address((String)nearStation.get("addr"))
			.distanceTm(((Number)nearStation.get("tm")).doubleValue())
			.build();

	}
}
