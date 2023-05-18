package com.quizGpt.formManagement.Gpt.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.quizGpt.formManagement.Gpt.Entity.Quiz;

public interface GptRepository extends JpaRepository<Quiz, Long>{
}
