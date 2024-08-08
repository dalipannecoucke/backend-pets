package be.vives.ti.pets.response;

import be.vives.ti.pets.model.Specialty;
import be.vives.ti.pets.model.Vet;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class VetWithSpecialtiesResponse extends VetResponse{

    private Set<Specialty> specialties;

    public VetWithSpecialtiesResponse(Vet vet) {
        super(vet);
        this.specialties = vet.getSpecialties();
    }

    public List<SpecialtyResponse> getSpecialties(){
        return specialties.stream().map(SpecialtyResponse::new).collect(Collectors.toList());
    }
}
