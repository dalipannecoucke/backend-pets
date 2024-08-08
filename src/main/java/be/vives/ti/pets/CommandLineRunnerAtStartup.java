package be.vives.ti.pets;


import be.vives.ti.pets.model.*;
import be.vives.ti.pets.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CommandLineRunnerAtStartup implements CommandLineRunner {

    private final OwnerRepository ownerRepository;
    private final PetRepository petRepository;
    private final PetTypeRepository petTypeRepository;
    private final SpecialtyRepository specialtyRepository;
    private final VetRepository vetRepository;

    public CommandLineRunnerAtStartup(OwnerRepository ownerRepository, PetRepository petRepository, PetTypeRepository petTypeRepository, SpecialtyRepository specialtyRepository, VetRepository vetRepository) {
        this.ownerRepository = ownerRepository;
        this.petRepository = petRepository;
        this.petTypeRepository = petTypeRepository;
        this.specialtyRepository = specialtyRepository;
        this.vetRepository = vetRepository;
    }

    @Override
    public void run(String... args) throws Exception {
       Specialty rad = specialtyRepository.save(new Specialty("RAD", "radiology"));
       Specialty sur = specialtyRepository.save(new Specialty("SUR", "surgery"));
       Specialty den = specialtyRepository.save(new Specialty("DEN", "dentistry"));

       Vet james = vetRepository.save(new Vet("James", "Carter"));
       Vet hellen = vetRepository.save(new Vet("Hellen", "Leary"));
       Vet linda = vetRepository.save(new Vet("Linda", "Douglas"));
       Vet rafael = vetRepository.save(new Vet("Rafael", "Ortega"));

       james.addSpecialty(rad);
       james.addSpecialty(sur);
       vetRepository.save(james);

       hellen.addSpecialty(rad);
       vetRepository.save(hellen);

       linda.addSpecialty(rad);
       linda.addSpecialty(sur);
       linda.addSpecialty(den);
       vetRepository.save(linda);

       rafael.addSpecialty(sur);
        vetRepository.save(rafael);

        Vet henry = new Vet("Henry", "Stevens");
       henry.addSpecialty(sur);
       henry.addSpecialty(den);
       henry = vetRepository.save(henry);

        Vet sharon = new Vet("Sharon", "Jenkins");
        sharon.addSpecialty(rad);
        sharon.addSpecialty(den);
         sharon = vetRepository.save(sharon);

        petTypeRepository.save(new PetType("cat"));
        petTypeRepository.save(new PetType("dog"));
        petTypeRepository.save(new PetType("lizard"));
        petTypeRepository.save(new PetType("snake"));
        petTypeRepository.save(new PetType("bird"));
        petTypeRepository.save(new PetType("hamster"));

        Pet frank = new Pet("Frank", LocalDate.now().minusMonths(12), petTypeRepository.findByName("hamster").get());
        Pet wally = new Pet("Wally", LocalDate.now().minusMonths(7), petTypeRepository.findByName("snake").get());
        Pet fred = new Pet("Fred", LocalDate.now().minusMonths(4), petTypeRepository.findByName("cat").get());
        Pet pablo = new Pet("Pablo", LocalDate.now().minusMonths(4), petTypeRepository.findByName("dog").get());
        Pet basil = new Pet("Basil", LocalDate.now().minusMonths(2), petTypeRepository.findByName("dog").get());
        Pet max = new Pet("Max", LocalDate.now().minusMonths(19), petTypeRepository.findByName("dog").get());

        Owner george = new Owner("George", "Franklin", "110 W. Liberty St.", "Madison", "6085551023");
        Owner betty = new Owner("Betty", "Davis", "638 Cardinal Ave.", "Sun Prairie", "6085551749");
        Owner eduardo = new Owner("Eduardo", "Rodriquez", "2693 Commerce St.", "McFarland", "6085558763");

        george= ownerRepository.save(george);
        george.addPet(frank);
        george.addPet(wally);
        george.addPet(fred);
        george= ownerRepository.save(george);

        betty=ownerRepository.save(betty);
        betty.addPet(pablo);
        betty.addPet(basil);
        betty=ownerRepository.save(betty);

        eduardo=ownerRepository.save(eduardo);
        eduardo.addPet(max);
        eduardo=ownerRepository.save(eduardo);

        Owner harold = new Owner("Harold", "Davis", "563 Friendly St.", "Windsor", "6085553198");

        Pet lucky = new Pet("Lucky", LocalDate.now().minusMonths(18), petTypeRepository.findByName("hamster").get(), harold);
        Pet samantha = new Pet("Samantha", LocalDate.now().minusMonths(62), petTypeRepository.findByName("cat").get(), harold);
        harold.addPet(lucky);
        harold.addPet(samantha);
        harold = ownerRepository.save(harold);

        Owner peter = new Owner("Peter", "McTavish", "2387 S. Fair Way", "Madison", "6085552765");
        Pet sly = new Pet("Sly", LocalDate.now().minusMonths(8), petTypeRepository.findByName("lizard").get(), peter);
        peter.addPet(sly);
        peter = ownerRepository.save(peter);

        Owner maria = new Owner("Maria", "Escobito", "345 Maple St.", "Madison", "6085557683");
        Pet iggy = new Pet("Iggy", LocalDate.now().minusMonths(36), petTypeRepository.findByName("dog").get());
        Pet joe = new Pet("Joe", LocalDate.now().minusMonths(4), petTypeRepository.findByName("cat").get());

        maria.addPet(iggy);
        maria.addPet(joe);
        maria = ownerRepository.save(maria);


    }
}
