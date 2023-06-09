package com.quizGpt.formManagement.Quiz.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.quizGpt.formManagement.Quiz.Entity.QuizAttempt;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    
}
