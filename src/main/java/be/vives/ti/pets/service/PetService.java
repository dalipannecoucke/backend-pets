package be.vives.ti.pets.service;

import be.vives.ti.pets.model.Pet;
import be.vives.ti.pets.repository.PetRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Stupid service class with very little business logic

@Service
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Page<Pet> findAll(Pageable pageable){
        return petRepository.findAll(pageable);
    }

    public Optional<Pet> findById(Integer petId){
        return petRepository.findById(petId);
    }

    public List<Pet> findByTypeName(String petType){
        return petRepository.findByTypeName(petType);
    }
    public List<Pet> findByOwnerLastNameIgnoreCase(String ownerLastName){
        return petRepository.findByOwnerLastNameIgnoreCase(ownerLastName);
    }

    public Pet save(Pet pet){
        return petRepository.save(pet);
    }

    public void deleteById(Integer id){
        petRepository.deleteById(id);
    }
}
