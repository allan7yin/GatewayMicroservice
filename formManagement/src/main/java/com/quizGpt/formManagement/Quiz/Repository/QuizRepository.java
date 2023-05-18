package com.quizGpt.formManagement.Quiz.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.quizGpt.formManagement.Quiz.Entity.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long>{
    // we will define the database structure as follows:

    // Will have a Users table -> managed by the Users microservice 
    // each User will have many Quizes, a ONE TO MANY relationship 
    // each Quiz will have a many question answer pairs, a ONE TO MANY relationship 
}
