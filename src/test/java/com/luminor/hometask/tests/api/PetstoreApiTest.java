package com.luminor.hometask.tests.api;

import com.luminor.hometask.domain.api.PetApiClient;
import com.luminor.hometask.domain.data.PetDataFactory;
import com.luminor.hometask.domain.model.Pet;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Petstore API")
@Feature("Pet CRUD")
@Tag("api")
class PetstoreApiTest {

    private static final Logger log = LoggerFactory.getLogger(PetstoreApiTest.class);

    private final PetApiClient petApiClient = new PetApiClient();
    private final List<Long> createdPetIds = new CopyOnWriteArrayList<>();

    @AfterEach
    void deleteCreatedPets() {
        createdPetIds.forEach(id -> {
            try {
                petApiClient.deletePet(id);
            } catch (Exception e) {
                log.warn("Failed to delete pet with id {}: {}", id, e.getMessage());
            }
        });
        createdPetIds.clear();
    }

    @Test
    @DisplayName("Creating a pet returns a response matching the Pet JSON schema")
    void createPetReturnsValidJsonSchema() {
        Response response = petApiClient.createPet(PetDataFactory.randomPet());

        response.then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/pet-schema.json"));

        createdPetIds.add(response.as(Pet.class).id());
    }

    @Test
    @DisplayName("Creating a pet preserves the submitted data")
    void createdPetPreservesData() {
        Pet newPet = PetDataFactory.randomPet();

        Pet created = petApiClient.createPet(newPet).as(Pet.class);
        createdPetIds.add(created.id());

        assertThat(created).usingRecursiveComparison().ignoringFields("id").isEqualTo(newPet);
    }

    @Test
    @DisplayName("Getting a pet by id returns the previously created pet")
    void getPetByIdReturnsPreviouslyCreatedPet() {
        Pet created = petApiClient.createPet(PetDataFactory.randomPet()).as(Pet.class);
        createdPetIds.add(created.id());

        Response response = petApiClient.getPetById(created.id());

        response.then().statusCode(200);
        Pet fetched = response.as(Pet.class);
        assertThat(fetched).isEqualTo(created);
    }

    @Test
    @DisplayName("Updating a pet changes its status")
    void updatePetChangesStatus() {
        Pet created = petApiClient.createPet(PetDataFactory.randomPet()).as(Pet.class);
        createdPetIds.add(created.id());

        Pet updated = created.withStatus("sold");
        Response response = petApiClient.updatePet(updated);

        response.then().statusCode(200);
        assertThat(response.as(Pet.class)).isEqualTo(updated);
    }

    @Test
    @DisplayName("Deleting a pet removes it")
    void deletePetRemovesIt() {
        Pet created = petApiClient.createPet(PetDataFactory.randomPet()).as(Pet.class);
        createdPetIds.add(created.id());

        petApiClient.deletePet(created.id()).then().statusCode(200);
        createdPetIds.remove(created.id());

        petApiClient.getPetById(created.id()).then().statusCode(404);
    }

    @Test
    @DisplayName("Getting a pet by a non-existent id returns 404")
    void getPetByNonExistentIdReturns404() {
        petApiClient.getPetById(PetDataFactory.randomNonExistentId()).then().statusCode(404);
    }

    @Test
    @DisplayName("Getting a pet by an invalid (non-numeric) id returns 404")
    void getPetByInvalidIdReturns404() {
        petApiClient.getPetById("invalid-pet-id").then().statusCode(404);
    }
}
