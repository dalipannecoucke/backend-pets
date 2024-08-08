package be.vives.ti.pets.response;

import be.vives.ti.pets.model.Vet;

public class VetResponse {

    private Integer id;
    private String name;

    public VetResponse(Vet vet){
        id = vet.getId();
        name = vet.getFirstName() + " " + vet.getLastName();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
