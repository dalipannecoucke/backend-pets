package be.vives.ti.pets.response;

import be.vives.ti.pets.model.Owner;

public class OwnerResponse {

    private Integer id;
    private String firstName;
    private String lastName;
    private String telephone;

    public OwnerResponse(Owner owner) {
        this.id = owner.getId();
        this.firstName = owner.getFirstName();
        this.lastName = owner.getLastName();
        this.telephone = owner.getTelephone();
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTelephone() {
        return telephone;
    }
}
