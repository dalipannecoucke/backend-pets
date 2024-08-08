package be.vives.ti.pets.response;

import be.vives.ti.pets.model.Pet;

import java.time.LocalDate;

public class PetResponse {

    private Integer id;
    private String name;
    private LocalDate birthDate;
    private String type;

    public PetResponse(Pet pet) {
        id = pet.getId();
        name = pet.getName();
        birthDate = pet.getBirthDate();
        type = pet.getType().getName();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getType() {
        return type;
    }
}
