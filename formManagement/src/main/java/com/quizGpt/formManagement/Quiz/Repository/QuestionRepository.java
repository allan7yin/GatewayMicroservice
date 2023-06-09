package com.quizGpt.formManagement.Quiz.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizGpt.formManagement.Quiz.Entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    
}
