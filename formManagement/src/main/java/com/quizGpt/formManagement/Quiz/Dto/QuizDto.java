package com.quizGpt.formManagement.Quiz.Dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QuizDto {
    @JsonProperty("conversationId")
    private String id;
    @JsonProperty("number")
    int number;
    @JsonProperty("results")
    private List<QAPairDto> results;
}

