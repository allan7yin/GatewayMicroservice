package com.quizGpt.formManagement.Quiz.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GptResponseDto {
    @JsonProperty("conversationId")
    private Long ConversationId;
    @JsonProperty("number")
    private Long Number;
    @JsonProperty("results")
    private List<QAPairDto> Results;
}
