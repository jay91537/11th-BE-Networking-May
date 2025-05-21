package cotato.networking.weather_api.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cotato.networking.weather_api.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByLoginId(String loginId);

	boolean existsByLoginId(String loginId);
}
