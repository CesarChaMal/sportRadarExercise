package com.sportradar.exercise.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportradar.exercise.dto.CreateMatchRequest;
import com.sportradar.exercise.dto.UpdateScoreRequest;
import com.sportradar.exercise.service.ScoreboardService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(MatchController.class)
public class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScoreboardService scoreboardService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateMatch() throws Exception {
        CreateMatchRequest request = new CreateMatchRequest("Team A", "Team B", "FOOTBALL");
        
        mockMvc.perform(post("/api/matches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetSummary() throws Exception {
        when(scoreboardService.getSummary()).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/matches/summary"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testUpdateScore() throws Exception {
        UpdateScoreRequest request = new UpdateScoreRequest(2, 1);
        
        mockMvc.perform(put("/api/matches/1/score")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void testFinishMatch() throws Exception {
        mockMvc.perform(delete("/api/matches/1"))
                .andExpect(status().isNoContent());
    }
}