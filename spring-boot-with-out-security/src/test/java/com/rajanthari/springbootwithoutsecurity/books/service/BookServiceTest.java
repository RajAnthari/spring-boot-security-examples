package com.rajanthari.springbootwithoutsecurity.books.service;

import com.rajanthari.springbootwithoutsecurity.books.model.Book;
import com.rajanthari.springbootwithoutsecurity.books.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book sampleBook;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleBook = new Book("Clean Code", "Robert C. Martin", 2008, 42.0);
        sampleBook.setId(1L);
    }

    @Test
    void testFindAllPagedWithoutKeyword() {
        Page<Book> page = new PageImpl<>(List.of(sampleBook));
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(page);

        var result = bookService.findAllPaged(null, 0, 10);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Clean Code");
        verify(bookRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testFindById() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        Book found = bookService.findById(1L);
        assertThat(found).isNotNull();
        assertThat(found.getAuthor()).isEqualTo("Robert C. Martin");
    }

    @Test
    void testSave() {
        when(bookRepository.save(sampleBook)).thenReturn(sampleBook);
        Book saved = bookService.save(sampleBook);
        assertThat(saved.getTitle()).isEqualTo("Clean Code");
    }

    @Test
    void testDeleteById() {
        doNothing().when(bookRepository).deleteById(1L);
        bookService.deleteById(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }
}
