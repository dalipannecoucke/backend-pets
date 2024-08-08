package be.vives.ti.pets.repository;

import be.vives.ti.pets.model.Owner;
import be.vives.ti.pets.model.Pet;
import be.vives.ti.pets.model.PetType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PetRepositoryTest {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetTypeRepository petTypeRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @BeforeEach
    void setUp() {
        petTypeRepository.save(new PetType("cat"));
        petTypeRepository.save(new PetType("dog"));
        petTypeRepository.save(new PetType("lizard"));
        petTypeRepository.save(new PetType("snake"));
        petTypeRepository.save(new PetType("bird"));
        petTypeRepository.save(new PetType("hamster"));
    }

    @Test
    void simpleCrud() {
        Pet frank = new Pet("Frank", LocalDate.now().minusMonths(12), petTypeRepository.findByName("hamster").get());
        frank = petRepository.save(frank);
        assertThat(frank.getId()).isNotNull();
        assertThat(petRepository.findAll().size()).isEqualTo(1);

        Pet wally = new Pet("Wally", LocalDate.now().minusMonths(7), petTypeRepository.findByName("snake").get());
        wally = petRepository.save(wally);
        assertThat(wally.getId()).isNotNull();
        assertThat(petRepository.findAll().size()).isEqualTo(2);

        frank.setType(petTypeRepository.findByName("dog").get());
        frank.setName("Franky");
        frank.setBirthDate(LocalDate.now().minusMonths(24));
        frank = petRepository.save(frank);

        assertThat(frank.getName()).isEqualTo("Franky");
        assertThat(frank.getType().getName()).isEqualTo("dog");
        assertThat(frank.getBirthDate()).isEqualTo(LocalDate.now().minusMonths(24));

        petRepository.delete(wally);
        assertThat(petRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void createPetAndOwner() {
        Pet frank = new Pet("Frank", LocalDate.now().minusMonths(12), petTypeRepository.findByName("hamster").get());
        Owner george = new Owner("George", "Franklin", "110 W. Liberty St.", "Madison", "6085551023");

        frank.setOwner(george);
        frank = petRepository.save(frank);

        assertThat(frank.getId()).isNotNull();
        assertThat(frank.getOwner()).isNotNull();
        assertThat(frank.getOwner().getId()).isNotNull();

        Integer ownerId = frank.getOwner().getId();
        Optional<Owner> optGeorge = ownerRepository.findById(ownerId);
        assertThat(optGeorge.isPresent()).isTrue();
        assertThat(optGeorge.get().getFirstName()).isEqualTo("George");
    }

    @Test
    void testFindByOwnerId() {
        Owner george = new Owner("George", "Franklin", "110 W. Liberty St.", "Madison", "6085551023");

        Pet frank = new Pet("Frank", LocalDate.now().minusMonths(12), petTypeRepository.findByName("hamster").get());
        frank.setOwner(george);
        frank = petRepository.save(frank);

        Pet wally = new Pet("Wally", LocalDate.now().minusMonths(7), petTypeRepository.findByName("snake").get());
        wally.setOwner(george);
        wally = petRepository.save(wally);

        Pet fred = new Pet("Fred", LocalDate.now().minusMonths(4), petTypeRepository.findByName("cat").get());
        petRepository.save(fred);

        assertThat(frank.getOwner().getId()).isNotNull();
        Integer ownerId = frank.getOwner().getId();

        List<Pet> petsOfGeorge = petRepository.findByOwnerId(ownerId);
        assertThat(petsOfGeorge.size()).isEqualTo(2);
        for (Pet pet : petsOfGeorge) {
            assertThat(pet.getOwner()).isNotNull();
            assertThat(pet.getOwner().getId()).isEqualTo(ownerId);
            assertThat(pet.getOwner().getFirstName()).isEqualTo("George");
        }

        List<Pet> franklinsPets = petRepository.findByOwnerLastNameIgnoreCase("Franklin");
        assertThat(franklinsPets.size()).isEqualTo(2);
        franklinsPets = petRepository.findByOwnerLastNameIgnoreCase("franklin");
        assertThat(franklinsPets.size()).isEqualTo(2);
    }

    @Test
    void findByPetTYpe() {
        Pet frank = petRepository.save(new Pet("Frank", LocalDate.now().minusMonths(12), petTypeRepository.findByName("hamster").get()));
        Pet wally = petRepository.save(new Pet("Wally", LocalDate.now().minusMonths(7), petTypeRepository.findByName("snake").get()));
        Pet fred = petRepository.save(new Pet("Fred", LocalDate.now().minusMonths(4), petTypeRepository.findByName("cat").get()));
        Pet pablo = petRepository.save(new Pet("Pablo", LocalDate.now().minusMonths(4), petTypeRepository.findByName("dog").get()));
        Pet basil = petRepository.save(new Pet("Basil", LocalDate.now().minusMonths(2), petTypeRepository.findByName("dog").get()));
        Pet max = petRepository.save(new Pet("Max", LocalDate.now().minusMonths(19), petTypeRepository.findByName("dog").get()));

        List<Pet> dogs = petRepository.findByTypeName("dog");
        assertThat(dogs.size()).isEqualTo(3);
        List<Pet> cats = petRepository.findByTypeName("cat");
        assertThat(cats.size()).isEqualTo(1);
        List<Pet> dinos = petRepository.findByTypeName("dinosaur");
        assertThat(dinos).isEmpty();

    }
}