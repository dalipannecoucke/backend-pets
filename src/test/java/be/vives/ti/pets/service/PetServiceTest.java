package be.vives.ti.pets.service;

import be.vives.ti.pets.model.Owner;
import be.vives.ti.pets.model.Pet;
import be.vives.ti.pets.model.PetType;
import be.vives.ti.pets.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetService petService;

    @Test
    void getAllPets() {
        PageRequest pageRequest = PageRequest.of(0,20);


        when(petRepository.findAll(pageRequest)).thenReturn(
                new PageImpl<>(
                List.of(new Pet("Frank", LocalDate.now().minusMonths(12), new PetType("hamster")),
                        new Pet("Wally", LocalDate.now().minusMonths(7), new PetType("snake")),
                        new Pet("Fred", LocalDate.now().minusMonths(7), new PetType("cat")),
                        new Pet("Basil", LocalDate.now().minusMonths(7), new PetType("dog")),
                        new Pet("Pablo", LocalDate.now().minusMonths(4), new PetType("dog")))
                , pageRequest, 5));


        Page<Pet> pets = petService.findAll(pageRequest);
        assertThat(pets.getTotalElements()).isEqualTo(5);
        assertThat(pets.getTotalPages()).isEqualTo(1);
        assertThat(pets.getSize()).isEqualTo(20);
        assertThat(pets.getContent()).hasSize(5);

        assertThat(pets.getContent()).extracting(Pet::getName)
                .containsExactlyInAnyOrder("Frank", "Wally", "Fred", "Basil", "Pablo");
    }

    @Test
    void findByIdFound() {
        when(petRepository.findById(14))
                .thenReturn(Optional.of(new Pet("Frank", LocalDate.now().minusMonths(12), new PetType("hamster"))));

        Optional<Pet> optFred = petService.findById(14);

        assertThat(optFred.isPresent()).isTrue();
        assertThat(optFred.get().getName()).isEqualTo("Frank");
        assertThat(optFred.get().getType().getName()).isEqualTo("hamster");
        assertThat(optFred.get().getOwner()).isNull();
    }

    @Test
    void findByIdNotFound() {
        when(petRepository.findById(14))
                .thenReturn(Optional.empty());

        Optional<Pet> optFred = petService.findById(14);

        assertThat(optFred.isPresent()).isFalse();
    }

    @Test
    void findByTypeNameFound() {
        when(petRepository.findByTypeName("dog"))
                .thenReturn(List.of(new Pet("Bart", LocalDate.now().minusMonths(12), new PetType("dog")),
                        new Pet("Basil", LocalDate.now().minusMonths(7), new PetType("dog")),
                        new Pet("Pablo", LocalDate.now().minusMonths(4), new PetType("dog"))));

        List<Pet> pets = petService.findByTypeName("dog");

        assertThat(pets).hasSize(3);
        for (Pet pet : pets) {
            assertThat(pet.getType().getName()).isEqualTo("dog");
        }
    }

    @Test
    void findByTypeNameNotFound() {
        when(petRepository.findByTypeName("dino"))
                .thenReturn(List.of());

        List<Pet> pets = petService.findByTypeName("dino");

        assertThat(pets.isEmpty()).isTrue();

    }

    @Test
    void findByfindByOwnerLastNameIgnoreCaseFound() {
        Owner george = new Owner("George", "Franklin", "110 W. Liberty St.", "Madison", "6085551023");


        when(petRepository.findByOwnerLastNameIgnoreCase("George"))
                .thenReturn(List.of(new Pet("Bart", LocalDate.now().minusMonths(12), new PetType("dog"), george),
                        new Pet("Pablo", LocalDate.now().minusMonths(4), new PetType("dog"), george)));

        List<Pet> pets = petService.findByOwnerLastNameIgnoreCase("George");

        assertThat(pets).hasSize(2);
        for (Pet pet : pets) {
            assertThat(pet.getOwner().getFirstName()).isEqualTo("George");
        }
    }

    @Test
    void findByfindByOwnerLastNameIgnoreCaseNotFound() {
        when(petRepository.findByOwnerLastNameIgnoreCase("Walter"))
                .thenReturn(List.of());

        List<Pet> pets = petService.findByOwnerLastNameIgnoreCase("Walter");

        assertThat(pets.isEmpty()).isTrue();

    }

    @Test
    void save(){
        Owner george = new Owner("George", "Franklin", "110 W. Liberty St.", "Madison", "6085551023");

        ArgumentCaptor<Pet> argument = ArgumentCaptor.forClass(Pet.class);

        petService.save(new Pet("Bart", LocalDate.now().minusMonths(12), new PetType("dog"), george));

        verify(petRepository).save(argument.capture());
        assertThat(argument.getValue().getName()).isEqualTo("Bart");
        assertThat(argument.getValue().getType().getName()).isEqualTo("dog");
        assertThat(argument.getValue().getOwner().getFirstName()).isEqualTo("George");
    }

    @Test
    void deleteById(){
        petService.deleteById(14);

        verify(petRepository).deleteById(14);
    }
}