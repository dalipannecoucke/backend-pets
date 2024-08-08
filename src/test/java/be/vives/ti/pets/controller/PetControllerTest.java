package be.vives.ti.pets.controller;

import be.vives.ti.pets.model.Owner;
import be.vives.ti.pets.model.Pet;
import be.vives.ti.pets.model.PetType;
import be.vives.ti.pets.repository.OwnerRepository;
import be.vives.ti.pets.repository.PetTypeRepository;
import be.vives.ti.pets.request.PetCreateRequest;
import be.vives.ti.pets.request.PetUpdateRequest;
import be.vives.ti.pets.service.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetController.class)
class PetControllerTest {

    private final String baseUrl = "/api/pets";

    @MockBean
    private PetService petService;
    @MockBean
    private OwnerRepository ownerRepository;
    @MockBean
    private PetTypeRepository petTypeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    private Pet frank;
    private Pet wally;
    private Pet fred;
    private Pet pablo;
    private Pet basil;

    private Owner george;
    private Owner betty;

    @BeforeEach
    void setUp() {
        when(petTypeRepository.findByName("hamster")).thenReturn(Optional.of(new PetType("Hamster")));
        when(petTypeRepository.findByName("snake")).thenReturn(Optional.of(new PetType("Snake")));
        when(petTypeRepository.findByName("cat")).thenReturn(Optional.of(new PetType("Cat")));
        when(petTypeRepository.findByName("dog")).thenReturn(Optional.of(new PetType("Dog")));

        frank = new Pet("Frank", LocalDate.now().minusMonths(12), petTypeRepository.findByName("hamster").orElseThrow());
        wally = new Pet("Wally", LocalDate.now().minusMonths(7), petTypeRepository.findByName("snake").orElseThrow());
        fred = new Pet("Fred", LocalDate.now().minusMonths(4), petTypeRepository.findByName("dog").orElseThrow());
        pablo = new Pet("Pablito", LocalDate.now().minusMonths(4), petTypeRepository.findByName("dog").orElseThrow());
        basil = new Pet("Basiel", LocalDate.now().minusMonths(2), petTypeRepository.findByName("dog").orElseThrow());

        george = new Owner("George", "Franklin", "110 W. Liberty St.", "Madison", "6085551023");
        betty = new Owner("Betty", "Davis", "638 Cardinal Ave.", "Sun Prairie", "6085551749");

    }

    @Test
    void getAllPets() throws Exception {
        Page page = new PageImpl(Arrays.asList(frank, wally, fred, pablo, basil));
        PageRequest of = PageRequest.of(0, 20);
        when(petService.findAll(of)).thenReturn(page);

        mvc.perform(get(baseUrl))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(5)))
                .andExpect(jsonPath("$.content[0].name", equalTo("Frank")))
                .andExpect(jsonPath("$.content[1].name", equalTo("Wally")))
                .andExpect(jsonPath("$.content[2].name", equalTo("Fred")))
                .andExpect(jsonPath("$.content[3].name", equalTo("Pablito")))
                .andExpect(jsonPath("$.content[4].name", equalTo("Basiel")));
    }

    @Test
    void getPetsOfType_Dog() throws Exception {
        String petType = "dog";

        when(petService.findByTypeName(petType)).thenReturn(Arrays.asList(pablo, basil, fred));

        mvc.perform(get(baseUrl).queryParam("type", petType))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", equalTo("Pablito")))
                .andExpect(jsonPath("$[0].type", equalTo("Dog")))
                .andExpect(jsonPath("$[1].name", equalTo("Basiel")))
                .andExpect(jsonPath("$[1].type", equalTo("Dog")))
                .andExpect(jsonPath("$[2].name", equalTo("Fred")))
                .andExpect(jsonPath("$[2].type", equalTo("Dog")));
    }

    @Test
    void getPetsOfType_Dinosaur() throws Exception {
        String petType = "dinosaur";

        when(petService.findByTypeName(petType)).thenReturn(new ArrayList<>());

        mvc.perform(get(baseUrl).queryParam("type", petType))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", empty()));
    }

    @Test
    void getPetById() throws Exception {
        when(petService.findById(45)).thenReturn(Optional.of(pablo));
        mvc.perform(get(baseUrl+"/45"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Pablito")))
                .andExpect(jsonPath("$.type", equalTo("Dog")));
    }

    @Test
    void getPetByIdNotFound() throws Exception {
        when(petService.findById(245)).thenReturn(Optional.empty());
        mvc.perform(get(baseUrl+"/245"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getPetByOwnerId() throws Exception {
        String ownerLastName = "Rodriquez";
        when(petService.findByOwnerLastNameIgnoreCase(ownerLastName)).thenReturn(Arrays.asList(frank, fred));
        mvc.perform(get(baseUrl+"/owner/"+ownerLastName))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", equalTo("Frank")))
                .andExpect(jsonPath("$[1].name", equalTo("Fred")));
    }

    @Test
    void getPetByOwnerIdNotFound() throws Exception {
        String ownerLastName = "Unknown";
        when(petService.findByOwnerLastNameIgnoreCase(ownerLastName)).thenReturn(new ArrayList<>());
        mvc.perform(get(baseUrl+"/owner/"+ownerLastName))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", empty()));
    }

    @Test
    void createPetWithExistingOwner() throws Exception {
        Integer ownerId = 114;

        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(george));
        Pet bob = new Pet();
        bob.setId(9);
        when(petService.save(any(Pet.class))).thenReturn(bob);

        PetCreateRequest request = new PetCreateRequest();
        request.setName("Bob");
        request.setType("dog");
        request.setBirthDate(LocalDate.now().minusDays(10));

        mvc.perform(post(baseUrl+"/owner/"+ownerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "http://localhost/api/pets/9"));
    }

    @Test
    void createPetWithExistingOwner_OwnerNotFound() throws Exception {
        Integer ownerId = 114;

        when(ownerRepository.findById(ownerId)).thenReturn(Optional.empty());

        PetCreateRequest request = new PetCreateRequest();
        request.setName("Bob");
        request.setType("dog");
        request.setBirthDate(LocalDate.now().minusDays(10));

        mvc.perform(post(baseUrl+"/owner/"+ownerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void createPetWithExistingOwner_ValidationError() throws Exception {
        Integer ownerId = 114;

        when(ownerRepository.findById(ownerId)).thenReturn(Optional.empty());

        PetCreateRequest request = new PetCreateRequest();

        mvc.perform(post(baseUrl+"/owner/"+ownerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPetWithExistingOwner_InvalidPetType() throws Exception {
        Integer ownerId = 114;

        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(george));
        Pet bob = new Pet();
        bob.setId(9);
        when(petService.save(any(Pet.class))).thenReturn(bob);

        PetCreateRequest request = new PetCreateRequest();
        request.setName("Bob");
        request.setType("dinosaur");
        request.setBirthDate(LocalDate.now().minusDays(10));

        mvc.perform(post(baseUrl+"/owner/"+ownerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updatePet() throws Exception {
        PetUpdateRequest request = new PetUpdateRequest();
        request.setName("Franky");
        request.setBirthDate(LocalDate.now().minusMonths(14));

        int petId = 547;
        when(petService.findById(547)).thenReturn(Optional.of(frank));
        Pet updatedFranky = new Pet("Franky", LocalDate.now().minusMonths(14), petTypeRepository.findByName("hamster").orElseThrow());
        when(petService.save(any())).thenReturn(updatedFranky);

        mvc.perform(put(baseUrl+"/"+petId)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Franky")));
    }

    @Test
    void updatePet_PetNotFound() throws Exception {
        PetUpdateRequest request = new PetUpdateRequest();
        request.setName("Franky");
        request.setBirthDate(LocalDate.now().minusMonths(14));

        int petId = 547;
        when(petService.findById(547)).thenReturn(Optional.empty());
        verifyNoMoreInteractions(petService);

        mvc.perform(put(baseUrl+"/"+petId)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void updatePet_ValidationError() throws Exception {
        PetUpdateRequest request = new PetUpdateRequest();
        request.setBirthDate(LocalDate.now().plusMonths(14));

        int petId = 547;
        when(petService.findById(547)).thenReturn(Optional.of(frank));
        verifyNoMoreInteractions(petService);

        mvc.perform(put(baseUrl+"/"+petId)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

}