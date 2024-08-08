package be.vives.ti.pets.controller;

import be.vives.ti.pets.exceptions.ResourceNotFoundException;
import be.vives.ti.pets.model.Specialty;
import be.vives.ti.pets.repository.SpecialtyRepository;
import be.vives.ti.pets.response.SpecialtyResponse;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(exposedHeaders = "*")
@RequestMapping("api/specialties")
public class SpecialtyController {

    private final SpecialtyRepository specialtyRepository;

    public SpecialtyController(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    @GetMapping
    public List<SpecialtyResponse> findAllSpecialties(@RequestParam(name = "name", required = false) String name){
        List<Specialty> specialties;
        if(name == null){
            specialties = specialtyRepository.findAll(Sort.by("code"));
        }
        else {
            specialties = specialtyRepository.findByName(name);
        }

        return specialties.stream().map(SpecialtyResponse::new).collect(Collectors.toList());
    }

    @GetMapping("/{specialtyCode}")
    public SpecialtyResponse findAllSpecialtiesByCode(@PathVariable("specialtyCode") String specialtyCode){
        return new SpecialtyResponse(specialtyRepository.findById(specialtyCode).orElseThrow(() -> new ResourceNotFoundException(specialtyCode, "specialty")));
    }

}
