package be.vives.ti.pets.controller;

import be.vives.ti.pets.exceptions.BadRequestException;
import be.vives.ti.pets.exceptions.ResourceNotFoundException;
import be.vives.ti.pets.model.Owner;
import be.vives.ti.pets.repository.OwnerRepository;
import be.vives.ti.pets.request.OwnerRequest;
import be.vives.ti.pets.response.OwnerPetResponse;
import be.vives.ti.pets.response.OwnerResponse;
import jakarta.validation.Valid;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
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
@RequestMapping("/api/owners")
public class OwnerController {

    private final OwnerRepository ownerRepository;


    public OwnerController(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @GetMapping
    public ResponseEntity<List<OwnerResponse>> getOwnersOrderByLastName() {
        return new ResponseEntity<>(this.ownerRepository.findAll(Sort.by("lastName")).stream().map(OwnerResponse::new).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = "/{ownerId}")
    public ResponseEntity<OwnerPetResponse> getOwner(@PathVariable("ownerId") Integer ownerId) {
        Optional<Owner> ownerOptional = ownerRepository.findById(ownerId);
        return ownerOptional.map(owner -> new ResponseEntity<>(new OwnerPetResponse(owner), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Void> addOwner(@RequestBody @Valid OwnerRequest ownerRequest) {
        HttpHeaders headers = new HttpHeaders();

        Owner owner = new Owner(ownerRequest.getFirstName(),
                ownerRequest.getLastName(),
                ownerRequest.getAddress(),
                ownerRequest.getCity(),
                ownerRequest.getTelephone());

        Owner newOwner = ownerRepository.save(owner);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newOwner.getId())
                .toUri();

        headers.setLocation(location);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @DeleteMapping("/{ownerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ownerId(@PathVariable("ownerId") Integer ownerId) {
        Owner owner = ownerRepository.findById(ownerId).orElseThrow(() -> new ResourceNotFoundException(ownerId, "owner"));

        if(!owner.getPets().isEmpty()){
            throw new BadRequestException("Owner still have pets");
        }

        try {
            ownerRepository.deleteById(ownerId);
        } catch (EmptyResultDataAccessException e) {
            // fine
        }
    }
}
