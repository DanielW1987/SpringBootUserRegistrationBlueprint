package rocks.danielw.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rocks.danielw.model.entities.AddressEntity;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

  Optional<AddressEntity> findByAddressId(String addressId);
	
}
