package com.luminor.hometask.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.luminor.hometask.domain.model.pet.Category;
import com.luminor.hometask.domain.model.pet.Tag;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Pet(Long id, Category category, String name, List<String> photoUrls, List<Tag> tags, String status) {

    public Pet withStatus(String newStatus) {
        return new Pet(id(), category(), name(), photoUrls(), tags(), newStatus);
    }
}
