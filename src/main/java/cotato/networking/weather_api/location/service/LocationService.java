package cotato.networking.weather_api.location.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cotato.networking.weather_api.common.error.ErrorCode;
import cotato.networking.weather_api.common.error.exception.AppException;
import cotato.networking.weather_api.location.domain.LocationEntity;
import cotato.networking.weather_api.location.dto.request.LocationPinRequest;
import cotato.networking.weather_api.location.dto.request.LocationRequest;
import cotato.networking.weather_api.location.dto.response.LocationGetResponse;
import cotato.networking.weather_api.location.repository.LocationRepository;
import cotato.networking.weather_api.user.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class LocationService {

	private final LocationRepository locationRepository;

	@Transactional
	public Long addLocation(LocationRequest request, User user) {
		if (locationRepository.existsByLocationNameAndUserId(request.locationName(), user.getId())) {
			throw new AppException(ErrorCode.LOCATION_NAME_DUPLICATED_EXCEPTION);
		}
		LocationEntity locationEntity = LocationEntity.builder()
			.locationName(request.locationName())
			.latitude(request.latitude())
			.longitude(request.longitude())
			.userId(user.getId())
			.build();
		return locationRepository.save(locationEntity).getLocationId();
	}

	public Page<LocationGetResponse> getLocations(User user, Pageable pageable) {
		return locationRepository.findAllByUserIdOrderByLocationIdDesc(user.getId(), pageable)
			.map(LocationGetResponse::from);
	}

	@Transactional
	public void pinLocation(LocationPinRequest request, User user) {
		LocationEntity locationEntity = locationRepository.findById(request.locationId())
			.orElseThrow(() -> new AppException(ErrorCode.LOCATION_NOT_FOUND_EXCEPTION));

		if (!locationEntity.getUserId().equals(user.getId())) {
			throw new AppException(ErrorCode.LOCATION_ACCESS_DENIED_EXCEPTION);
		}

		locationEntity.updatePinStatus(request.isPinned());
	}
}