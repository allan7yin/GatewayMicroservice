package com.quizGpt.formManagement.Quiz.Dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("id")
    private Long id;
    @JsonProperty("number")
    private Long number;
    @JsonProperty("results")
    private List<QAPairDto> results;
}

