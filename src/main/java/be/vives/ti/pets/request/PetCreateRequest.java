package be.vives.ti.pets.request;

import jakarta.validation.constraints.NotEmpty;

public class PetCreateRequest extends PetUpdateRequest {

    @NotEmpty
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
