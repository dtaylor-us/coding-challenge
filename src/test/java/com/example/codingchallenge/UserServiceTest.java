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

    @Test
    void getLastUser() {

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        userServiceLogger.addAppender(listAppender);

        final User expectedUser = TestUtil.getUserList().get(1); // second user in llist should be returned

        final User result = userService.getLastUser(TestUtil.getUserList());


        // JUnit assertions
        List<ILoggingEvent> logsList = listAppender.list;
        assertEquals("Last users name: " + result.getName(), logsList.get(0).getMessage());

        assertEquals(expectedUser.getName(), result.getName());
        assertEquals(expectedUser.getId(), result.getId());
        assertEquals(expectedUser.getEmail(), result.getEmail());
        assertEquals(expectedUser.getGender(), result.getGender());
        assertEquals(expectedUser.getStatus(), result.getStatus());

    }

    @Test
    void updateUser() {
        User user = TestUtil.getUserList().get(0);

        ResponseEntity<User> usersEntity = new ResponseEntity<>(user, HttpStatus.ACCEPTED);

        when(restTemplate.exchange(
                Mockito.eq(BASE_URL + 3721),
                Mockito.eq(HttpMethod.PATCH),
                Mockito.<HttpEntity<User>>any(),
                Mockito.eq(User.class))
        ).thenReturn(usersEntity);

        User results = userService.updateUser("xxxx", user);

        assertEquals(user, results);
    }

    @Test
    void deleteUser() {
        User user = TestUtil.getUserList().get(0);

        ResponseEntity<User> usersEntity = new ResponseEntity<>(user, HttpStatus.ACCEPTED);

        userService.deleteUser("xxxx", user);

        verify(restTemplate).exchange(Mockito.eq(BASE_URL + 3721),
                Mockito.eq(HttpMethod.DELETE),
                Mockito.<HttpEntity<User>>any(),
                Mockito.eq(Void.class)
        );
    }

    @Test
    void getUserById_userNotFound() {
        int userId = 5555;

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();

        // add the appender to the logger
        userServiceLogger.addAppender(listAppender);
        when(restTemplate.getForEntity(BASE_URL + userId, User.class)).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        userService.getUserById(userId);

        List<ILoggingEvent> logsList = listAppender.list;
        assertEquals("404 NOT_FOUND", logsList.get(0).getMessage());
    }

    @Test
    void getUserById() {
        int userId = 5555;

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();

        // add the appender to the logger
        userServiceLogger.addAppender(listAppender);
        User user = new User();
        ResponseEntity<User> response = new ResponseEntity<>(user, HttpStatus.ACCEPTED);

        when(restTemplate.getForEntity(BASE_URL + userId, User.class)).thenReturn(response);

        User result = userService.getUserById(userId);

        assertEquals(user, result);
    }


}
