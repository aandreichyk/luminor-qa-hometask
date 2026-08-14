package com.luminor.hometask.domain.model.pet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Category(Long id, String name) {
}
