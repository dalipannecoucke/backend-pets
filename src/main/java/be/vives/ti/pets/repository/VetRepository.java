package be.vives.ti.pets.repository;

import be.vives.ti.pets.model.Vet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VetRepository extends JpaRepository<Vet, Integer> {
}
