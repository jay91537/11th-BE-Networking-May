package cotato.networking.weather_api.location.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cotato.networking.weather_api.location.domain.LocationEntity;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
	Page<LocationEntity> findAllByUserIdOrderByLocationIdDesc(Long userId, Pageable pageable);

	boolean existsByLocationNameAndUserId(String locationName, Long userId);
}