package com.quizGpt.formManagement.Account.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizGpt.formManagement.Account.Dto.LoginRequestDto;
import com.quizGpt.formManagement.Account.Dto.LoginResponseDto;
import com.quizGpt.formManagement.Account.Dto.SignUpRequestDto;
import com.quizGpt.formManagement.Account.Entity.MqResponse;
import com.quizGpt.formManagement.Account.Exception.CorrelationIdNotFound;
import com.quizGpt.formManagement.Account.Service.AccountService;
import com.quizGpt.formManagement.Account.Service.AuthMqService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;


@RestController
@RequestMapping("/someApiurl")
public class AccountController {
    private AccountService accountService;
    private AuthMqService authMqService;
    private HttpSession session; // we'll use this to set the session period to around one week 
    private ObjectMapper objectMapper;

    public AccountController(AuthMqService authMqService, AccountService accountService, HttpSession session, ObjectMapper objectMapper ) {
        this.accountService = accountService;
        this.authMqService = authMqService;
        this.session = session;
        this.objectMapper = objectMapper;
    }

    // Spring takes care of mapping the JSON (or other standard data formats) into Java object (however, needs to be a valid one, otherwise, error)
    @PostMapping("/account/login")
    public @NotNull ResponseEntity LoginUser(@RequestBody LoginRequestDto request) throws JsonProcessingException, CorrelationIdNotFound, InterruptedException, ExecutionException, TimeoutException {
        // calls another microservices API that is responsible for user authentication 
        authMqService.SendLoginRequestDto(request); // this will result in the message's response being saved in db, wait and obtain it

        Future<String> response = GetResponseOrWait(request.getUsername());
        String trueResponse = response.get();

        LoginResponseDto loginDto = null;
        if (trueResponse != null) {

            loginDto = objectMapper.readValue(trueResponse, LoginResponseDto.class);

            session.setAttribute("username", loginDto.getUsername());
            session.setAttribute("roles", loginDto.getRoles());
        }
        return ResponseEntity.ok(loginDto);
    }

    @PostMapping("/account/signup")
    public @NotNull ResponseEntity SignUpUser(@RequestBody SignUpRequestDto request) throws JsonProcessingException, TimeoutException, CorrelationIdNotFound, InterruptedException, ExecutionException {
        authMqService.SendSignUpRequestDto(request); 

        Future<String> response = GetResponseOrWait(request.getUsername());
        String trueResponse = response.get();

        SignUpRequestDto loginDto = null;
        if (trueResponse != null) {

            loginDto = objectMapper.readValue(trueResponse, SignUpRequestDto.class);
        }
        return ResponseEntity.ok(loginDto);
    }

    @GetMapping("/account/logout")
    public void Logout() {
        session.invalidate();
    }

    private Future<String> GetResponseOrWait(String correlationIdOrUsername) throws TimeoutException, CorrelationIdNotFound {
        long threadSleepTime = 1000;
        int maxTimeAlloted = 10;
        int waitTime = 0;
        CompletableFuture<String> futureResponse = new CompletableFuture<>();

        while (waitTime < maxTimeAlloted) {

            MqResponse response;
            // check if we were passed an ID (numeric) or a username (word)
            try {
                Double.parseDouble(correlationIdOrUsername);
                response = accountService.FindMqResponseById(correlationIdOrUsername);
            } catch (NumberFormatException e) {
                response = accountService.FindFirstMqResponse(correlationIdOrUsername);
            }

            if(response != null) {
                futureResponse.complete(response.getResponse().replace("response=", ""));
                accountService.MqDelete(response);
                return futureResponse;
            } else {
                try {
                    Thread.sleep(threadSleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                waitTime++;
            }
        }
        throw  new TimeoutException("Timeout: Took too long to fetch " + correlationIdOrUsername);
    }
}
