package cotato.networking.weather_api.common.dust;

import cotato.networking.weather_api.common.error.ErrorCode;
import cotato.networking.weather_api.common.error.exception.AppException;
import cotato.networking.weather_api.common.property.property.AirKoreaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DustService {
	private final RestTemplate restTemplate;
	private final AirKoreaProperties airKoreaProperties;

	public DustDto getDustInfo(String stationName, String encodedCity) {

		URI uri = UriComponentsBuilder
			.fromHttpUrl(airKoreaProperties.getDustUrl())
			.queryParam("serviceKey", airKoreaProperties.getServiceKey())
			.queryParam("returnType", "json")
			.queryParam("numOfRows", 100)
			.queryParam("pageNo", 1)
			.queryParam("sidoName", encodedCity)
			.queryParam("ver", 1.0)
			.build(true)
			.toUri();

		ResponseEntity<Map> res = restTemplate.exchange(uri, HttpMethod.GET, null, Map.class);

		Map<String, Object> body = (Map<String, Object>)
			((Map<String, Object>)res.getBody().get("response")).get("body");
		List<Map<String, Object>> items = (List<Map<String, Object>>)body.get("items");

		if (items == null || items.isEmpty()) {
			throw new AppException(ErrorCode.DUST_FETCH_FAILURE);
		}

		// stationName이 일치하는 단 하나의 측정소 찾기
		Map<String, Object> station = items.stream()
			.filter(i -> stationName.equals(i.get("stationName")))
			.findFirst()
			.orElseThrow(() -> new AppException(ErrorCode.STATION_NOT_FOUND));

		// DTO 변환 후 리턴
		return DustDto.builder()
			.stationName((String)station.get("stationName"))
			.dataTime((String)station.get("dataTime"))
			.pm10Value((String)station.get("pm10Value"))
			.pm10Grade((String)station.get("pm10Grade"))
			.pm25Value((String)station.get("pm25Value"))
			.pm25Grade((String)station.get("pm25Grade"))
			.build();
	}
}
