package com.shep.services;

import com.shep.dataTransferObjects.BookDTO;
import com.shep.entity.Book;
import com.shep.exceptions.NotFoundException;
import com.shep.mapper.BookMapper;
import com.shep.repository.BookRepository;
import com.shep.services.implementation.BookServiceImpl;
import com.shep.services.implementation.LibraryServiceClientImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private LibraryServiceClientImpl libraryServiceClientImpl;

    @InjectMocks
    private BookServiceImpl bookServiceImpl;

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

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        Page<Book> result = bookServiceImpl.getAllBooks(pageable);

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

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        Optional<Book> result = bookServiceImpl.getBookById(id);

        assertEquals(id, result.get().getId());
        assertEquals("Book 1", result.get().getTitle());
    }

    @Test
    public void testGetBookByIdNotFound() {
        Long id = 1L;

        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Book> result = bookServiceImpl.getBookById(id);

        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testGetBookByIsbn() {
        String isbn = "1234567890";
        Book book = new Book();
        book.setIsbn(isbn);
        book.setTitle("Book 1");

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));

        Optional<Book> result = bookServiceImpl.getBookByIsbn(isbn);

        assertEquals(isbn, result.get().getIsbn());
        assertEquals("Book 1", result.get().getTitle());
    }

    @Test
    public void testGetBookByIsbnNotFound() {
        String isbn = "1234567890";

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());

        Optional<Book> result = bookServiceImpl.getBookByIsbn(isbn);

        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testCreateBook() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("New Book");
        String token = "Bearer token";

        Book book = BookMapper.INSTANCE.toEntity(bookDTO);
        book.setId(1L);

        when(bookRepository.save(any(Book.class))).thenReturn(book);
        doNothing().when(libraryServiceClientImpl).createFreeBook(anyLong(), anyString());

        BookDTO result = bookServiceImpl.createBook(bookDTO, token);

        assertEquals("New Book", result.getTitle());
    }

    @Test
    public void testUpdateBook() {
        Long id = 1L;
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Updated Book");

        Book existingBook = new Book();
        existingBook.setId(id);
        existingBook.setTitle("Old Book");

        Book updatedBook = new Book();
        updatedBook.setId(id);
        updatedBook.setTitle("Updated Book");

        when(bookRepository.findById(id)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        BookDTO result = bookServiceImpl.updateBook(id, bookDTO);

        assertEquals("Updated Book", result.getTitle());
    }

    @Test
    public void testUpdateBookNotFound() {
        Long id = 1L;
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Updated Book");

        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookServiceImpl.updateBook(id, bookDTO));
    }

    @Test
    public void testDeleteBook() {
        Long id = 1L;
        String token = "Bearer token";

        when(bookRepository.existsById(id)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(id);
        doNothing().when(libraryServiceClientImpl).deleteFreeBook(anyLong(), anyString());

        bookServiceImpl.deleteBook(id, token);

        verify(bookRepository).deleteById(id);
        verify(libraryServiceClientImpl).deleteFreeBook(id, token);
    }

    @Test
    public void testDeleteBookNotFound() {
        Long id = 1L;
        String token = "Bearer token";

        when(bookRepository.existsById(id)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> bookServiceImpl.deleteBook(id, token));
    }
}