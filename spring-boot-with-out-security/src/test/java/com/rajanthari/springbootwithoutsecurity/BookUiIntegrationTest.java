package com.rajanthari.springbootwithoutsecurity;

import com.rajanthari.springbootwithoutsecurity.books.model.Book;
import com.rajanthari.springbootwithoutsecurity.books.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookUiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setup() {
        bookRepository.deleteAll();
        bookRepository.save(new Book("UI Test Book 1", "Author A", 2020, 40.0));
        bookRepository.save(new Book("UI Test Book 2", "Author B", 2021, 45.0));
    }

    @Test
    void testListPageRendersBooks() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("UI Test Book 1")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("UI Test Book 2")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Add New Book")));
    }

    @Test
    void testSearchFilterWorks() throws Exception {
        mockMvc.perform(get("/books").param("keyword", "Book 1"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("UI Test Book 1")))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("UI Test Book 2"))));
    }

    @Test
    void testCreateBookThroughUiForm() throws Exception {
        mockMvc.perform(post("/books")
                .param("title", "Created via UI")
                .param("author", "UI Author")
                .param("publishedYear", "2024")
                .param("price", "50.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        assertThat(bookRepository.findAll())
                .anyMatch(book -> book.getTitle().equals("Created via UI"));
    }

    @Test
    void testFlashMessageDisplayedAfterCreate() throws Exception {
        mockMvc.perform(post("/books")
                .param("title", "Flash Message Book")
                .param("author", "UI Tester")
                .param("publishedYear", "2023")
                .param("price", "60.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("successMessage"));
    }
}
