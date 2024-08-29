package com.shep.controllers.impl;

import com.shep.controllers.interfaces.BookControllerDocs;
import com.shep.dto.BookDTO;
import com.shep.entities.Book;
import com.shep.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookControllerImpl implements BookControllerDocs {

    private final BookService bookService;

    @Override
    public Page<Book> getAllBooks(Pageable pageable) {
        return bookService.getAllBooks(pageable);
    }

    @Override
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
        Optional<Book> book = bookService.getBookByIsbn(isbn);
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public BookDTO createBook(@RequestHeader("Authorization") String token, @RequestBody BookDTO bookDTO) {
        return bookService.createBook(bookDTO, token);
    }

    @Override
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDetails) {
        BookDTO updatedBook = bookService.updateBook(id, bookDetails);
        if (updatedBook != null) {
            return ResponseEntity.ok(updatedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteBook(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        bookService.deleteBook(id, token);
        return ResponseEntity.noContent().build();
    }
}