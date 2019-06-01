package rocks.danielw.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rocks.danielw.model.entities.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	
  Optional<UserEntity> findByEmail(String email);

  Optional<UserEntity> findByUserId(String userId);

  boolean existsByEmail(String email);
	
}
