package be.vives.ti.pets.controller;


import be.vives.ti.pets.exceptions.ResourceNotFoundException;
import be.vives.ti.pets.model.Owner;
import be.vives.ti.pets.model.Pet;
import be.vives.ti.pets.model.PetType;
import be.vives.ti.pets.repository.OwnerRepository;
import be.vives.ti.pets.repository.PetTypeRepository;
import be.vives.ti.pets.request.PetCreateRequest;
import be.vives.ti.pets.request.PetOwnerCreateRequest;
import be.vives.ti.pets.request.PetUpdateRequest;
import be.vives.ti.pets.response.PetOwnerResponse;
import be.vives.ti.pets.response.PetResponse;
import be.vives.ti.pets.service.PetService;
import jakarta.validation.Valid;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(exposedHeaders = "*")
@RequestMapping("api/pets")
public class PetController {

    private final PetService petService;
    private final OwnerRepository ownerRepository;
    private final PetTypeRepository petTypeRepository;

    public PetController(PetService petService, OwnerRepository ownerRepository, PetTypeRepository petTypeRepository) {
        this.petService = petService;
        this.ownerRepository = ownerRepository;
        this.petTypeRepository = petTypeRepository;
    }

    @GetMapping
    public Page<PetOwnerResponse> getAllPets(Pageable pageable) {
        return this.petService.findAll(pageable).map(PetOwnerResponse::new);
    }

    @GetMapping(params = "type")
    public List<PetResponse> getPetsOfType(@RequestParam("type") String petType) {
        List<Pet> pets = petService.findByTypeName(petType);
        return pets.stream().map(PetResponse::new).collect(Collectors.toList());
    }

    @GetMapping(value = "/{petId}")
    public PetOwnerResponse getPetById(@PathVariable("petId") Integer petId) {
        Pet pet = petService.findById(petId).orElseThrow(() -> new ResourceNotFoundException(petId, "pet"));
        return new PetOwnerResponse(pet);
    }

    @GetMapping(value = "/owner/{ownerLastName}")
    public List<PetResponse> getPetByOwnerId(@PathVariable("ownerLastName") String ownerLastName) {
        return petService.findByOwnerLastNameIgnoreCase(ownerLastName).stream().map(PetResponse::new).collect(Collectors.toList());
    }

    @PostMapping(value = "/owner/{ownerId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createPetWithExistingOwner(@RequestBody @Valid PetCreateRequest petCreateRequest, @PathVariable("ownerId") Integer ownerId){
        Owner owner = ownerRepository.findById(ownerId).orElseThrow(() -> new ResourceNotFoundException(ownerId, "owner"));

        Optional<PetType> petTypeOptional = petTypeRepository.findByName(petCreateRequest.getType());
        if (petTypeOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(petCreateRequest.getType() + " is invalid petType");
        }

        Pet newPet = petService.save(new Pet(petCreateRequest.getName(),
                petCreateRequest.getBirthDate(),
                petTypeOptional.get(),
                owner));
        
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/pets/{id}")
                .buildAndExpand(newPet.getId())
                .toUri();

        //Send location in response
        return ResponseEntity.created(location).build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createPetWithOwner(@RequestBody @Valid PetOwnerCreateRequest petOwnerCreateRequest){
        Owner owner = new Owner(petOwnerCreateRequest.getOwner().getFirstName(),
                petOwnerCreateRequest.getOwner().getLastName(),
                petOwnerCreateRequest.getOwner().getAddress(),
                petOwnerCreateRequest.getOwner().getCity(),
                petOwnerCreateRequest.getOwner().getTelephone());

        Optional<PetType> petTypeOptional = petTypeRepository.findByName(petOwnerCreateRequest.getType());
        if (petTypeOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(petOwnerCreateRequest.getType() + " is invalid petType");
        }

        Pet newPet = petService.save(new Pet(petOwnerCreateRequest.getName(),
                petOwnerCreateRequest.getBirthDate(),
                petTypeOptional.get(),
                owner));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newPet.getId())
                .toUri();

        //Send location in response
        return ResponseEntity.created(location).build();
    }


    @PutMapping("/{petId}")
    public PetResponse updatePet(@PathVariable(name = "petId") Integer petId,
                              @RequestBody @Valid PetUpdateRequest petUpdateRequest) {
        Pet pet = petService.findById(petId).orElseThrow(() -> new ResourceNotFoundException(petId, "pet"));
        pet.setName(petUpdateRequest.getName());
        pet.setBirthDate(petUpdateRequest.getBirthDate());
        return new PetResponse(petService.save(pet));
    }

    @PatchMapping("/{petId}")
    public PetResponse patchPet(@PathVariable(name = "petId") Integer petId,
                                @RequestBody PetUpdateRequest petUpdateRequest) {
        Pet pet = petService.findById(petId).orElseThrow(() -> new ResourceNotFoundException(petId, "pet"));
        if(petUpdateRequest.getName() != null) {
            pet.setName(petUpdateRequest.getName());
        }
        if(petUpdateRequest.getBirthDate() != null) {
            pet.setBirthDate(petUpdateRequest.getBirthDate());
        }

        return new PetResponse(petService.save(pet));
    }

    @DeleteMapping("/{petId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletePet(@PathVariable("petId") Integer petId) {
        Optional<Pet> petOptional = petService.findById(petId);
        if (petOptional.isPresent()) {
            try {
                petService.deleteById(petId);
            } catch (EmptyResultDataAccessException e) {
                // fine
            }
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}

