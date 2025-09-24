package com.rajanthari.springbootwithoutsecurity.books.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rajanthari.springbootwithoutsecurity.books.model.Book;
import com.rajanthari.springbootwithoutsecurity.books.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookRestController.class)
class BookRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllBooks() throws Exception {
        when(bookService.findAll()).thenReturn(List.of(new Book("Clean Code", "Robert C. Martin", 2008, 42.0)));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Clean Code"));

        verify(bookService, times(1)).findAll();
    }

    @Test
    void testCreateBook() throws Exception {
        Book newBook = new Book("DDD", "Eric Evans", 2003, 60.5);
        when(bookService.save(any(Book.class))).thenReturn(newBook);

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("DDD"));
    }
}
