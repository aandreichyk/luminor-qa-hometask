package com.luminor.hometask.domain.data;

import com.luminor.hometask.domain.model.Pet;
import com.luminor.hometask.domain.model.pet.Category;
import com.luminor.hometask.domain.model.pet.Tag;
import net.datafaker.Faker;

import java.util.List;

public class PetDataFactory {

    private static final Faker FAKER = new Faker();

    private PetDataFactory() {
    }

    public static Pet randomPet() {
        return new Pet(
                FAKER.number().numberBetween(100_000L, 999_999_999L),
                new Category(FAKER.number().numberBetween(100L, 999L), FAKER.animal().species()),
                FAKER.animal().name(),
                List.of(FAKER.internet().url()),
                List.of(new Tag(FAKER.number().numberBetween(100L, 999L), FAKER.commerce().department())),
                "available"
        );
    }

    public static long randomNonExistentId() {
        return FAKER.number().numberBetween(1_000_000_000L, 9_000_000_000L);
    }
}