package be.vives.ti.pets.request;

import jakarta.validation.constraints.NotEmpty;

public class SpecialtyRequest {

    @NotEmpty
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
