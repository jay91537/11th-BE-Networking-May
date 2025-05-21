package cotato.networking.weather_api.common.dust;

import cotato.networking.weather_api.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/dust")
@RequiredArgsConstructor
public class DustController {

	private final DustService dustService;

	@GetMapping
	public ApiResponse<DustDto> getDustInfo(@RequestParam String city, @RequestParam String stationName) {

		// 한글 인코딩 처리
		String encodedCity;
		try {
			encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("URL 인코딩 실패", e);
		}

		return ApiResponse.ok(dustService.getDustInfo(stationName, encodedCity));
	}
}
