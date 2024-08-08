package be.vives.ti.pets.repository;

import be.vives.ti.pets.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SpecialtyRepository extends JpaRepository<Specialty, String> {

    List<Specialty> findByName(String name);
}
