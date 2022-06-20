package com.herbert.PixAPI.Pix;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(PixController.class)
class PixControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PixRepository pixRepository;
//    @MockBean
//    private PixService service;

    @Test
    public void test_inclusao_de_cadastro_pix() throws Exception {
        Pix pixRequest = new Pix("email",
                "herberthudson@gmail.com",
                "corrente",
                1234,
                23132343,
                'F',
                "Herbert",
                "Hudson");

        mockMvc.perform(post("/api/v1/pix")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pixRequest)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$", hasKey("id")));
    }

}