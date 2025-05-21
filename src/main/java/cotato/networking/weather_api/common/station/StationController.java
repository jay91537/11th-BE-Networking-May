package cotato.networking.weather_api.common.station;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cotato.networking.weather_api.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/station")
@RequiredArgsConstructor
public class StationController {

	private final StationService stationService;

	@GetMapping()
	public ApiResponse<StationDto> nearestStation(@RequestParam double lat,
		@RequestParam double lon) {
		return ApiResponse.ok(stationService.nearestStation(lat, lon));
	}
}
