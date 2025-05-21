package cotato.networking.weather_api.location.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "location")
public class LocationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long locationId;

	@Column(name = "location_name", nullable = false)
	private String locationName;

	@Column(name = "latitude", nullable = false)
	private Double latitude;

	@Column(name = "longitude", nullable = false)
	private Double longitude;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "is_pinned", nullable = false)
	private Boolean isPinned;

	@Builder
	public LocationEntity(String locationName, Double latitude, Double longitude, Long userId) {
		this.locationName = locationName;
		this.latitude = latitude;
		this.longitude = longitude;
		this.userId = userId;
		this.isPinned = false;
	}

	public void updatePinStatus(Boolean isPinned) {
		this.isPinned = isPinned;
	}
}