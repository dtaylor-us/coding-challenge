package com.example.codingchallenge;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private static final String BASE_URL = "https://gorest.co.in/public/v2/users/";


    private final RestTemplate restTemplate;

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<User> getUsersByPageNumber(final int pageNumber) {
        User user = new User();
        String url = BASE_URL + "?page=" + pageNumber;
        ParameterizedTypeReference<List<User>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<User>> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(user), responseType);

        Optional<List<User>> usersOptional = Optional.ofNullable(response.getBody());
        List<User> users = usersOptional.orElseGet(ArrayList::new);

        // Using a logger, log the total number of pages from the previous request.
        logTotalNumberOfPages(response);
        return users;
    }

    private void logTotalNumberOfPages(ResponseEntity<List<User>> response) {
        HttpHeaders responseHeaders = response.getHeaders();
        Optional<List<String>> headersOptional = Optional.ofNullable(responseHeaders.get("X-Pagination-Pages"));
        String totalNumberOfPages = headersOptional.orElseGet(ArrayList::new)
                .stream()
                .findFirst()
                .orElse("0");
        log.info("Total # of pages: " + totalNumberOfPages);
    }

    public User getLastUser(List<User> users) {
        List<User> sortedUsers = users.stream()
                .sorted(Comparator.comparing(User::getName))
                .toList();

        // After sorting, log the name of the last user.
        User lastUser = sortedUsers.get(sortedUsers.size() - 1);
        log.info("Last users name: " + lastUser.getName());
        return lastUser;
    }

    public User updateUser(String accessToken, User lastUser) {
        final HttpEntity<User> requestEntity = createUserRequestEntity(accessToken, lastUser);

        String userUrl = getUserUrl(lastUser.getId());

        ResponseEntity<User> responseEntity = restTemplate.exchange(userUrl, HttpMethod.PATCH, requestEntity, User.class);
        Optional<User> userOptional = Optional.ofNullable(responseEntity.getBody());
        return userOptional.orElseGet(User::new);
    }

    private HttpEntity<User> createUserRequestEntity(String accessToken, User entity) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", accessToken);
        return new HttpEntity<>(entity, requestHeaders);
    }

    private String getUserUrl(long userId) {
        return BASE_URL + userId;
    }
}
