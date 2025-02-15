package com.mockApi.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Getter;


@Getter
@JsonDeserialize
public class ResponseMessageDto {
    private String message;

    @JsonCreator
    public ResponseMessageDto(){}

    public ResponseMessageDto(String message) {
        this.message = message;
    }
}
