package com.shep.controllers;

import com.shep.controllers.impl.BookControllerImpl;
import com.shep.dto.BookDTO;
import com.shep.entities.Book;
import com.shep.services.BookService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


public class BookControllersImplTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookControllerImpl bookControllerImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");
        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book 2");
        Page<Book> bookPage = new PageImpl<>(Arrays.asList(book1, book2), pageable, 2);

        when(bookService.getAllBooks(pageable)).thenReturn(bookPage);

        Page<Book> result = bookControllerImpl.getAllBooks(pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals("Book 1", result.getContent().get(0).getTitle());
        assertEquals("Book 2", result.getContent().get(1).getTitle());
    }

    @Test
    public void testGetBookById() {
        Long id = 1L;
        Book book = new Book();
        book.setId(id);
        book.setTitle("Book 1");

        when(bookService.getBookById(id)).thenReturn(Optional.of(book));

        ResponseEntity<Book> response = bookControllerImpl.getBookById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Book 1", response.getBody().getTitle());
    }

    @Test
    public void testGetBookByIdNotFound() {
        Long id = 1L;

        when(bookService.getBookById(id)).thenReturn(Optional.empty());

        ResponseEntity<Book> response = bookControllerImpl.getBookById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetBookByIsbn() {
        String isbn = "1234567890";
        Book book = new Book();
        book.setIsbn(isbn);
        book.setTitle("Book 1");

        when(bookService.getBookByIsbn(isbn)).thenReturn(Optional.of(book));

        ResponseEntity<Book> response = bookControllerImpl.getBookByIsbn(isbn);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Book 1", response.getBody().getTitle());
    }

    @Test
    public void testGetBookByIsbnNotFound() {
        String isbn = "1234567890";

        when(bookService.getBookByIsbn(isbn)).thenReturn(Optional.empty());

        ResponseEntity<Book> response = bookControllerImpl.getBookByIsbn(isbn);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateBook() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("New Book");
        String token = "Bearer token";

        BookDTO createdBookDTO = new BookDTO();
        createdBookDTO.setTitle("New Book");

        when(bookService.createBook(bookDTO, token)).thenReturn(createdBookDTO);

        BookDTO result = bookControllerImpl.createBook(token, bookDTO);

        assertEquals("New Book", result.getTitle());
    }

    @Test
    public void testUpdateBook() {
        Long id = 1L;
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Updated Book");

        BookDTO updatedBookDTO = new BookDTO();
        updatedBookDTO.setTitle("Updated Book");

        when(bookService.updateBook(id, bookDTO)).thenReturn(updatedBookDTO);

        ResponseEntity<BookDTO> response = bookControllerImpl.updateBook(id, bookDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Book", response.getBody().getTitle());
    }

    @Test
    public void testDeleteBook() {
        Long id = 1L;
        String token = "Bearer token";

        doNothing().when(bookService).deleteBook(id, token);

        ResponseEntity<Void> response = bookControllerImpl.deleteBook(token, id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}