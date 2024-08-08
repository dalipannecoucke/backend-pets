package be.vives.ti.pets.repository;

import be.vives.ti.pets.model.PetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PetTypeRepository extends JpaRepository<PetType, Integer> {

    Optional<PetType> findByName(String name);

}
