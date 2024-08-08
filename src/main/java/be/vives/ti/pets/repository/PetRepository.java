
package be.vives.ti.pets.repository;

import be.vives.ti.pets.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PetRepository extends JpaRepository<Pet, Integer> {

    List<Pet> findByTypeName(String petType);
    List<Pet> findByOwnerId(Integer ownerId);
    List<Pet> findByOwnerLastNameIgnoreCase(String ownerLastName);

}
