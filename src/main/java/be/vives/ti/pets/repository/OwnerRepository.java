package be.vives.ti.pets.repository;

import be.vives.ti.pets.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Integer> {
}
