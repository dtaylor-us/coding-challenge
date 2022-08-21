package com.example.codingchallenge;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@Slf4j
public class API {

    private final UserService userService;

    public API(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("api/v1/execute")
    public String executeWorkflow(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken) {

        // Retrieve page 3 of the list of all users.
        List<User> users = userService.getUsersByPageNumber(3);

        // Sort the retrieved user list by name.
        User lastUser = userService.getLastUser(users);

        // Update that user's name to a new value and use the correct http method to save it.
        lastUser.setName("Jane Doe");
        User updatedUser;
        try {
            updatedUser = userService.updateUser(accessToken, lastUser);
        } catch (HttpClientErrorException ex) {
            log.info(ex.getMessage());
            return "Encountered client error trying to update user, do you have a valid token in the header?";
        }

        // delete user
        try {
            userService.deleteUser(accessToken, updatedUser);
        } catch (HttpClientErrorException ex) {
            log.info(ex.getMessage());
            return "Encountered client error trying to delete user, do you have a valid token in the header?";
        }

        // Attempt to retrieve a nonexistent user with ID 5555. Log the resulting http response code.
        userService.getUserById(5555);

        return "Successfully completed workflow";
    }
}
