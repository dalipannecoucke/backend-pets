package be.vives.ti.pets.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class PetOwnerCreateRequest extends PetUpdateRequest{

    @NotEmpty
    private String type;

    @NotNull
    private OwnerRequest owner;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public OwnerRequest getOwner() {
        return owner;
    }

    public void setOwner(OwnerRequest owner) {
        this.owner = owner;
    }
}
