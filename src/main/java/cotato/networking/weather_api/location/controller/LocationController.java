package cotato.networking.weather_api.location.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cotato.networking.weather_api.common.response.ApiResponse;
import cotato.networking.weather_api.common.response.PageResponse;
import cotato.networking.weather_api.location.dto.request.LocationPinRequest;
import cotato.networking.weather_api.location.dto.request.LocationRequest;
import cotato.networking.weather_api.location.dto.response.LocationGetResponse;
import cotato.networking.weather_api.location.dto.response.LocationIdResponse;
import cotato.networking.weather_api.location.service.LocationService;
import cotato.networking.weather_api.user.User;
import cotato.networking.weather_api.user.annotation.CurrentUser;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/api/locations")
public class LocationController {

	private final LocationService locationService;

	@PostMapping
	public ResponseEntity<ApiResponse<LocationIdResponse>> addLocation(
		@RequestBody @Valid LocationRequest request,
		@CurrentUser User user) {
		return ResponseEntity.ok(
			ApiResponse.created(LocationIdResponse.from(locationService.addLocation(request, user))));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<LocationGetResponse>>> getLocations(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@CurrentUser User user) {

		Pageable pageable = PageRequest.of(page, size);
		return ResponseEntity.ok(
			ApiResponse.ok(PageResponse.of(locationService.getLocations(user, pageable))));
	}

	@PutMapping("/pin")
	public ResponseEntity<ApiResponse<Void>> pinLocation(
		@RequestBody @Valid LocationPinRequest locationPinRequest,
		@CurrentUser User user) {

		locationService.pinLocation(locationPinRequest, user);
		return ResponseEntity.ok(ApiResponse.noContent());
	}
}