package com.quizGpt.formManagement.Account.Service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.quizGpt.formManagement.Account.Entity.MqResponse;
import com.quizGpt.formManagement.Account.Exception.CorrelationIdNotFound;
import com.quizGpt.formManagement.Account.Repository.MqResponseRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AccountService {
    private MqResponseRepository mqResponseRepository;

    public MqResponse FindMqResponseById(String correlationId) throws CorrelationIdNotFound {
        // like to use <Optional> when the thing being returned may not be present. Often used when querying database
        Optional<MqResponse> response = mqResponseRepository.findById(correlationId);

        if (response.isPresent()) {
            return response.get(); // we use the .get() method to obtain the value stored within the optional 
        } else {
            throw new CorrelationIdNotFound("error: mq with id" + correlationId + " not found");
        }
    }

    public MqResponse FindFirstMqResponse(String correlationIdOrUsername) throws CorrelationIdNotFound {
        Optional<MqResponse> response = Optional.ofNullable(mqResponseRepository.findFirstResponse(correlationIdOrUsername));
        
        if (response.isPresent()) {
            return response.get(); // we use the .get() method to obtain the value stored within the optional 
        } else {
            throw new CorrelationIdNotFound("error: mq with id" + correlationIdOrUsername + " not found");
        }
    }

    public void MqDelete(MqResponse response) {
        mqResponseRepository.delete(response);
    }
}
