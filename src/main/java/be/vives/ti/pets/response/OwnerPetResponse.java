package be.vives.ti.pets.response;

import be.vives.ti.pets.model.Owner;

import java.util.List;
import java.util.stream.Collectors;

public class OwnerPetResponse extends OwnerResponse{

    private List<String> pets;

    public OwnerPetResponse(Owner owner) {
        super(owner);
        pets = owner.getPets().stream().map(p -> p.getName() + " the " + p.getType().getName()).collect(Collectors.toList());
    }

    public List<String> getPets() {
        return pets;
    }
}
