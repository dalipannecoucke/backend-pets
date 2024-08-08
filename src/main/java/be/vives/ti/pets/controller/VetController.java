package be.vives.ti.pets.controller;

import be.vives.ti.pets.exceptions.ResourceNotFoundException;
import be.vives.ti.pets.model.Specialty;
import be.vives.ti.pets.model.Vet;
import be.vives.ti.pets.repository.SpecialtyRepository;
import be.vives.ti.pets.repository.VetRepository;
import be.vives.ti.pets.request.SpecialtyRequest;
import be.vives.ti.pets.request.VetRequest;
import be.vives.ti.pets.response.VetResponse;
import be.vives.ti.pets.response.VetWithSpecialtiesResponse;
import jakarta.validation.Valid;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(exposedHeaders = "*")
@RequestMapping("api/vets")
public class VetController {

    private final VetRepository vetRepository;
    private final SpecialtyRepository specialtyRepository;

    public VetController(VetRepository vetRepository, SpecialtyRepository specialtyRepository) {
        this.vetRepository = vetRepository;
        this.specialtyRepository = specialtyRepository;
    }

    @GetMapping
    public List<VetResponse> getAllVets(){
        return vetRepository.findAll().stream().map(VetResponse::new).collect(Collectors.toList());
    }

    @GetMapping("/{vetId}")
    public VetResponse getVet(@PathVariable("vetId") Integer vetId) {
        return new VetResponse(vetRepository.findById(vetId).orElseThrow(() -> new ResourceNotFoundException(vetId, "vet")));
    }

    @GetMapping("/{vetId}/specialties")
    public VetWithSpecialtiesResponse getVetWithSpecialties(@PathVariable(name = "vetId") Integer vetId) {
        return new VetWithSpecialtiesResponse(vetRepository.findById(vetId).orElseThrow(() -> new ResourceNotFoundException(vetId, "vet")));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createVet(@RequestBody @Valid VetRequest vetRequest) {
        Vet vet = new Vet(vetRequest.getFirstName(), vetRequest.getLastName());
        Vet vetWithId = vetRepository.save(vet);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(vetWithId.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{vetId}")
    public VetResponse putVet(@PathVariable(name = "vetId") Integer vetId,
                                    @RequestBody @Valid VetRequest vetRequest) {
        Vet vet = vetRepository.findById(vetId).orElseThrow(() -> new ResourceNotFoundException(vetId, "vet"));
        vet.setFirstName(vetRequest.getFirstName());
        vet.setLastName(vetRequest.getLastName());

        return new VetResponse(vetRepository.save(vet));
    }

    @PatchMapping("/{vetId}")
    public VetResponse patchVet(@PathVariable(name = "vetId") Integer vetId,
                              @RequestBody VetRequest vetRequest) {
        Vet vet = vetRepository.findById(vetId).orElseThrow(() -> new ResourceNotFoundException(vetId, "vet"));
        if(vetRequest.getFirstName() != null) {
            vet.setFirstName(vetRequest.getFirstName());
        }
        if(vetRequest.getLastName() != null) {
            vet.setLastName(vetRequest.getLastName());
        }

        return new VetResponse(vetRepository.save(vet));
    }

    @DeleteMapping("/{vetId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVet(@PathVariable("vetId") Integer vetId) {
        Vet vet = vetRepository.findById(vetId).orElseThrow(() -> new ResourceNotFoundException(vetId, "vet"));

        try {
            vetRepository.deleteById(vetId);
        } catch (EmptyResultDataAccessException e) {
            // fine
        }
    }

    @PostMapping("/{vetId}/specialties")
    @ResponseStatus(HttpStatus.CREATED)
    public void addSpecialtyToVet(@PathVariable(name = "vetId") Integer vetId, @RequestBody @Valid SpecialtyRequest specialtyRequest){
        Vet vet = vetRepository.findById(vetId).orElseThrow(() -> new ResourceNotFoundException(vetId, "vet"));
        Specialty specialty = specialtyRepository.findById(specialtyRequest.getCode()).orElseThrow(() -> new ResourceNotFoundException(specialtyRequest.getCode(), "specialty"));

        if(vet.getSpecialties().contains(specialty)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, specialty.getCode() + " is already added");
        }

        vet.addSpecialty(specialty);
        vetRepository.save(vet);
    }

    @DeleteMapping("/{vetId}/specialties")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllSpecialtyOfVet(@PathVariable(name = "vetId") Integer vetId) {
        Vet vet = vetRepository.findById(vetId).orElseThrow(() -> new ResourceNotFoundException(vetId, "vet"));
        vet.clearSpecialties();
        vetRepository.save(vet);
    }

    @DeleteMapping("/{vetId}/specialties/{specialtyCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSpecialty(@PathVariable(name = "vetId") Integer vetId,
                                     @PathVariable(name = "specialtyCode") String specialtyCode) {
        Vet vet = vetRepository.findById(vetId).orElseThrow(() -> new ResourceNotFoundException(vetId, "vet"));
        Specialty specialty = specialtyRepository.findById(specialtyCode).orElseThrow(() -> new ResourceNotFoundException(specialtyCode, "specialty"));
        vet.removeSpecialty(specialty);
        vetRepository.save(vet);
    }

}
