package com.shep.controllers.impl;

import com.shep.controllers.interfaces.BookControllerDocs;
import com.shep.dataTransferObjects.BookDTO;
import com.shep.entity.Book;
import com.shep.services.interfaces.BookServiceInterface;
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

    private final BookServiceInterface bookServiceInterface;

    @Override
    public Page<Book> getAllBooks(Pageable pageable) {
        return bookServiceInterface.getAllBooks(pageable);
    }

    @Override
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookServiceInterface.getBookById(id);
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
        Optional<Book> book = bookServiceInterface.getBookByIsbn(isbn);
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public BookDTO createBook(@RequestHeader("Authorization") String token, @RequestBody BookDTO bookDTO) {
        return bookServiceInterface.createBook(bookDTO, token);
    }

    @Override
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDetails) {
        BookDTO updatedBook = bookServiceInterface.updateBook(id, bookDetails);
        return ResponseEntity.ok(updatedBook);
    }

    @Override
    public ResponseEntity<Void> deleteBook(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        bookServiceInterface.deleteBook(id, token);
        return ResponseEntity.noContent().build();
    }
}
