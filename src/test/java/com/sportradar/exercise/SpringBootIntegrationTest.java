package com.sportradar.exercise;

import com.sportradar.exercise.dto.CreateMatchRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBootIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
        assertNotNull(restTemplate);
    }

    @Test
    public void testHealthEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/actuator/health", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testMatchSummaryEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/matches/summary", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testCreateMatchEndpoint() {
        CreateMatchRequest request = new CreateMatchRequest("Team A", "Team B", "FOOTBALL");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateMatchRequest> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/matches", entity, String.class);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testInvalidMatchCreation() {
        CreateMatchRequest request = new CreateMatchRequest("", "Team B", "INVALID");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateMatchRequest> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/matches", entity, String.class);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}