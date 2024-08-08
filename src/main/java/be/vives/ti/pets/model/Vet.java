package be.vives.ti.pets.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "vets")
public class Vet extends BaseEntity {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @ManyToMany
    @JoinTable(name = "vet_specialties", joinColumns = @JoinColumn(name = "vet_id"),
        inverseJoinColumns = @JoinColumn(name = "specialty_id"))
    private Set<Specialty> specialties;

    public Vet(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialties = new HashSet<>();
    }

    public Vet() {

    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Specialty> getSpecialties() {
        if (this.specialties == null) {
            this.specialties = new HashSet<>();
        }
        return this.specialties;
    }

    public void setSpecialties(List<Specialty> specialties) {
        this.specialties = new HashSet<>(specialties);
    }

    public void addSpecialty(Specialty specialty) {
        getSpecialties().add(specialty);
    }

    public void clearSpecialties() {
        getSpecialties().clear();
    }


    public void removeSpecialty(Specialty specialty) {
        this.getSpecialties().remove(specialty);
    }
}
