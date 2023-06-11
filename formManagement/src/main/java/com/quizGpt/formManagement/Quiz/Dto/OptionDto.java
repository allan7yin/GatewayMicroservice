package com.quizGpt.formManagement.Quiz.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OptionDto {
    private Long optionId;
    private Long questionId;
    private String text;
}
