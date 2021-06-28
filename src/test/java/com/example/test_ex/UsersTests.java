package com.example.test_ex;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Properties;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UsersTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnError() throws Exception {
        mockMvc.perform(get("/api/users.addNew")
                .param("first_name", "ads")
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().string("{\"error\":100,\"message\":\"last_name is required\"}"));

    }

    @Test
    public void addNewUser() throws Exception {
        mockMvc.perform(get("/api/users.addNew")
                .param("first_name", "ads")
                .param("last_name", "asd")
                .param("age", "30")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("\"response\"")))
                .andExpect(content().string(Matchers.containsString("\"first_name\":\"ads\",\"last_name\":\"asd\",\"age\":30")));

    }
}
