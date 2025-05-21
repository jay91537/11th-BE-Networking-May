package cotato.networking.weather_api.location.dto.response;

import cotato.networking.weather_api.location.domain.LocationEntity;

public record LocationGetResponse(
	Long locationId,
	String locationName,
	Double latitude,
	Double longitude,
	Boolean isPinned
) {

	public static LocationGetResponse from(LocationEntity locationEntity) {
		return new LocationGetResponse(
			locationEntity.getLocationId(),
			locationEntity.getLocationName(),
			locationEntity.getLatitude(),
			locationEntity.getLongitude(),
			locationEntity.getIsPinned());
	}
}