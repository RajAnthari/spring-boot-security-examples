package com.rajanthari.springbootwithoutsecurity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rajanthari.springbootwithoutsecurity.books.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullCrudFlow() throws Exception {
        // 1. CREATE
        Book newBook = new Book("Integration Testing", "QA Guru", 2024, 40.0);
        String createdResponse = mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Book createdBook = objectMapper.readValue(createdResponse, Book.class);
        assertThat(createdBook.getId()).isNotNull();

        // 2. READ
        mockMvc.perform(get("/api/books/" + createdBook.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Integration Testing"));

        // 3. UPDATE
        createdBook.setPrice(50.0);
        mockMvc.perform(put("/api/books/" + createdBook.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createdBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(50.0));

        // 4. DELETE
        mockMvc.perform(delete("/api/books/" + createdBook.getId()))
                .andExpect(status().isNoContent());

        // 5. VERIFY DELETED
        mockMvc.perform(get("/api/books/" + createdBook.getId()))
                .andExpect(status().isNotFound());
    }
}
