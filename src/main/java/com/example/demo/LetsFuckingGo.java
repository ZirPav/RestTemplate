package com.example.demo;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.List;

public class LetsFuckingGo {

    static final String URL_USERS = "http://91.241.64.178:7081/api/users";
    static RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        String cookie = getAllUsers();
        creatUser(cookie);
        updateUser(cookie);
        deleteUser(cookie);
    }

    private static String getAllUsers(){

        ResponseEntity<List<User>> response = restTemplate
                .exchange(URL_USERS, HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>(){});
        List<User> users = response.getBody();
        for (User u: users) {
            System.out.println(u);
        }
        System.out.println(response.getHeaders());
        String cookie = response.getHeaders().getFirst("Set-Cookie");
        return cookie;
    }

    private static void creatUser(String cookie) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add(HttpHeaders.COOKIE, cookie);

        User user = new User(3L, "James", "Brown", (byte) 27);

        HttpEntity<User> request = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.exchange(URL_USERS, HttpMethod.POST, request, String.class);

        System.out.println(response.getHeaders());
        System.out.println(response.getBody());

    }

    private static void updateUser(String cookie) {

        User userEdit = new User(3L, "Thomas", "Shelby", (byte) 27);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add(HttpHeaders.COOKIE, cookie);

        HttpEntity<User> requestBody = new HttpEntity<>(userEdit, headers);

        System.out.println(restTemplate.exchange(URL_USERS, HttpMethod.PUT, requestBody, String.class).getBody());
    }

    private static void deleteUser(String cookie) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add(HttpHeaders.COOKIE, cookie);

        String url = "http://91.241.64.178:7081/api/users/3";

        HttpEntity<HttpHeaders> requestBody = new HttpEntity<>(headers);

        System.out.println(restTemplate.exchange(url, HttpMethod.DELETE, requestBody, String.class));
    }
}
