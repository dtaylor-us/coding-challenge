package com.example.codingchallenge;


import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String BASE_URL = "https://gorest.co.in/public/v2/users/";
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    UserService userService;

    Logger userServiceLogger = (Logger) LoggerFactory.getLogger(UserService.class);

    @Test
    void getUsersByPageNumber() {
        List<User> users = TestUtil.getUserList();

        ResponseEntity<List<User>> usersEntity = new ResponseEntity<>(users, HttpStatus.ACCEPTED);

        when(restTemplate.exchange(
                Mockito.eq(BASE_URL + "?page=3"),
                Mockito.eq(HttpMethod.GET),
                Mockito.<HttpEntity<User>>any(),
                Mockito.<ParameterizedTypeReference<List<User>>>any())
        ).thenReturn(usersEntity);

        List<User> results = userService.getUsersByPageNumber(3);
        assertEquals(users, results);
    }

    @Test
    void getUsersByPageNumber_noUsersReturned() {
        ResponseEntity<List<User>> usersEntity = new ResponseEntity<>(Collections.emptyList(), HttpStatus.ACCEPTED);

        when(restTemplate.exchange(
                Mockito.eq(BASE_URL + "?page=3"),
                Mockito.eq(HttpMethod.GET),
                Mockito.<HttpEntity<User>>any(),
                Mockito.<ParameterizedTypeReference<List<User>>>any())
        ).thenReturn(usersEntity);

        List<User> results = userService.getUsersByPageNumber(3);
        assertEquals(Collections.emptyList(), results);

    }

}
