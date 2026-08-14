package com.luminor.hometask.domain.api;

import com.luminor.hometask.domain.model.Pet;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

public class PetApiClient {

    private static final Logger log = LoggerFactory.getLogger(PetApiClient.class);
    private static final String BASE_URL = System.getProperty("apiBaseUrl", "https://petstore.swagger.io/v2");

    private final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri(BASE_URL)
            .setContentType(ContentType.JSON)
            .addFilter(new AllureRestAssured())
            .build();

    private RequestSpecification request() {
        return given().spec(requestSpec);
    }

    @Step("Create pet")
    public Response createPet(Pet pet) {
        log.info("POST /pet - creating pet '{}'", pet.name());
        return request()
                .body(pet)
                .post("/pet");
    }

    @Step("Get pet by id {petId}")
    public Response getPetById(long petId) {
        return getPetById(String.valueOf(petId));
    }

    @Step("Get pet by id {petId}")
    public Response getPetById(String petId) {
        log.info("GET /pet/{}", petId);
        return request()
                .pathParam("petId", petId)
                .get("/pet/{petId}");
    }

    @Step("Update pet")
    public Response updatePet(Pet pet) {
        log.info("PUT /pet - updating pet {} to status '{}'", pet.id(), pet.status());
        return request()
                .body(pet)
                .put("/pet");
    }

    @Step("Delete pet by id {petId}")
    public Response deletePet(long petId) {
        log.info("DELETE /pet/{}", petId);
        return request()
                .pathParam("petId", petId)
                .delete("/pet/{petId}");
    }
}
