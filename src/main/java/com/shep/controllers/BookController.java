package com.shep.controllers;

import com.shep.dto.BookDTO;
import com.shep.entities.Book;
import com.shep.services.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @Operation(summary = "Get all books with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))}),
            @ApiResponse(responseCode = "404", description = "Books not found", content = @Content)
    })
    @GetMapping
    public Page<Book> getAllBooks(Pageable pageable) {
        return bookService.getAllBooks(pageable);
    }

    @Operation(summary = "Get a book by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get a book by ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)
    })
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
        Book book = bookService.getBookByIsbn(isbn);
        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public BookDTO createBook(@RequestHeader("Authorization") String token, @RequestBody BookDTO bookDTO) {
        return bookService.createBook(bookDTO, token);
    }

    @Operation(summary = "Update a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDetails) {
        BookDTO updatedBook = bookService.updateBook(id, bookDetails);
        if (updatedBook != null) {
            return ResponseEntity.ok(updatedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
