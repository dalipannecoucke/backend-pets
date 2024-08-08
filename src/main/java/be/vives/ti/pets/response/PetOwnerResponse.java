package be.vives.ti.pets.response;

import be.vives.ti.pets.model.Owner;
import be.vives.ti.pets.model.Pet;

public class PetOwnerResponse extends PetResponse {

    private Owner owner;

    public PetOwnerResponse(Pet pet) {
        super(pet);
        owner = pet.getOwner();
    }

    public OwnerResponse owner(){
        return new OwnerResponse(owner);
    }
}
