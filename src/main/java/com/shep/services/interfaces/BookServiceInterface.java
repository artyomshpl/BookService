package com.shep.services.interfaces;

import com.shep.dataTransferObjects.BookDTO;
import com.shep.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookServiceInterface {
    Page<Book> getAllBooks(Pageable pageable);
    Optional<Book> getBookById(Long id);
    Optional<Book> getBookByIsbn(String isbn);
    BookDTO createBook(BookDTO bookDTO, String token);
    BookDTO updateBook(Long id, BookDTO bookDetails);
    void deleteBook(Long id, String token);
}