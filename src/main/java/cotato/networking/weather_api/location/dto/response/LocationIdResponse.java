package cotato.networking.weather_api.location.dto.response;

public record LocationIdResponse(
	Long locationId) {
	public static LocationIdResponse from(Long locationId) {
		return new LocationIdResponse(locationId);
	}
}