package com.quizGpt.formManagement.Quiz.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.quizGpt.formManagement.Quiz.Entity.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long>{
}
