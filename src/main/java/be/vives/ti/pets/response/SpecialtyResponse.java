package be.vives.ti.pets.response;

import be.vives.ti.pets.model.Specialty;

public class SpecialtyResponse {

    private String code;
    private String name;

    public SpecialtyResponse(Specialty specialty){
        code = specialty.getCode();
        name = specialty.getName();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
