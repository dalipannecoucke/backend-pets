package be.vives.ti.pets.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "specialties")
public class Specialty {

    @Id
    @Column
    private String code;

    @Column
    private String name;

    public Specialty(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public Specialty() {

    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Specialty specialty = (Specialty) o;
        return code.equals(specialty.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
