package com.example.codingchallenge;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class API {

    private final UserService userService;

    public API(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("api/v1/execute")
    public String executeWorkflow(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken) {

        // Retrieve page 3 of the list of all users.
        List<User> users = userService.getUsersByPageNumber(3);


        return "Successfully completed workflow";
    }
}