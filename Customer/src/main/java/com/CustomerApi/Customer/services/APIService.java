package com.CustomerApi.Customer.services;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import java.util.Collections;
import java.util.Arrays;

import com.CustomerApi.Customer.entities.Customer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class APIService {
    private static String baseUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/";
    private static String token = "";

    public String getAccessToken(String loginId, String password) {
        String apiUrl = "assignment_auth.jsp";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Request Body
            String jsonInputString = String.format("{\"login_id\": \"%s\", \"password\": \"%s\"}", loginId, password);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonInputString, headers);
            RestTemplate restTemplate = new RestTemplate();

            // Making POST request
            ResponseEntity<String> responseEntity = restTemplate.exchange(baseUrl + apiUrl, HttpMethod.POST,
                    requestEntity,
                    String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                // Extracting access_token from the Api response
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(responseEntity.getBody());
                JsonNode token = root.path("access_token");
                return token.toString();
            } else {
                System.out.println("Failed to get access token. HTTP error code: " + responseEntity.getStatusCode());
            }
        } catch (HttpServerErrorException.InternalServerError ex) {
            System.out.println("Failed to authenticate: " + ex.getMessage());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String addCustomer(Customer customer, String token) {

        String apiUrl = "assignment.jsp?cmd=create";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        System.out.println(token);

        HttpEntity<Customer> requestEntity = new HttpEntity<>(customer, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(baseUrl + apiUrl, HttpMethod.POST,
                    requestEntity, String.class);

            System.out.println("API Response: " + responseEntity.getBody());
            return "Successfull added";
        } catch (HttpClientErrorException e) {

            System.err.println("HTTP Error: " + e.getStatusCode());
            System.err.println("Response Body: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }

        return "Error adding customer";
    }

    public static List<Customer> getCustomerList(String token) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String apiUrl = baseUrl + "/assignment.jsp?cmd=get_customer_list";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            // Making API call to retrieve the list of customers
            ResponseEntity<Customer[]> response = restTemplate.exchange(apiUrl, HttpMethod.GET,
                    new HttpEntity<>(headers), Customer[].class);

            return Arrays.asList(response.getBody());
        } catch (HttpClientErrorException e) {
            // Handle HTTP client errors (4xx)
            System.err.println("HTTP error: " + e.getStatusCode() + " - " + e.getStatusText());
            return Collections.emptyList();
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void deleteCustomer(String uuid, String token) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String apiUrl = baseUrl + "assignment.jsp?cmd=delete&uuid=" + uuid;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            System.out.println(uuid);

            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, new HttpEntity<>(headers),
                    String.class);
            System.out.println(response.getBody());
            // getCustomerList(token);

        } catch (HttpClientErrorException e) {

            System.err.println("HTTP error: " + e.getStatusCode() + " - " + e.getStatusText());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String updateCustomer(Customer customer, String uuid, String token) {
        String apiUrl = "assignment.jsp?cmd=update&uuid=" + uuid;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        System.out.println(token);

        HttpEntity<Customer> requestEntity = new HttpEntity<>(customer, headers);

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(baseUrl + apiUrl, HttpMethod.POST,
                    requestEntity, String.class);

            System.out.println("API Response: " + responseEntity.getBody());
            return "Successfull updated";
        } catch (HttpClientErrorException e) {

            System.err.println("HTTP Error: " + e.getStatusCode());
            System.err.println("Response Body: " + e.getResponseBodyAsString());
        } catch (Exception e) {

            System.err.println("An error occurred: " + e.getMessage());
        }
        return "";

    }

    public Customer getCustomerByUUID(String uuid, String token) {
        Customer currCustomer = new Customer();
        List<Customer> customers = getCustomerList(token);
        for (Customer customer : customers) {
            if (customer.getUuid().equalsIgnoreCase(uuid)) {
                currCustomer = customer;
            }
        }
        return currCustomer;
    }
}