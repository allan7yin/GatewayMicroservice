package com.quizGpt.formManagement.Quiz.Dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

// AKA gptResponseDto
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QuizDto {
    @NotBlank
    @JsonProperty("id")
    private Long id;

    @NotBlank
    @JsonProperty("number")
    private Long number;

    @NotBlank
    @JsonProperty("results")
    private List<QAPairDto> results;
}

