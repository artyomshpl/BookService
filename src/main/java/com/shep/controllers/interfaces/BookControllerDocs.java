package com.shep.controllers.interfaces;


import com.shep.dataTransferObjects.BookDTO;
import com.shep.entity.Book;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface BookControllerDocs {

    @Operation(summary = "Get all books with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))}),
            @ApiResponse(responseCode = "404", description = "Books not found", content = @Content)
    })
    @GetMapping
    Page<Book> getAllBooks(Pageable pageable);

    @Operation(summary = "Get a book by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)
    })
    @GetMapping("/{id}")
    ResponseEntity<Book> getBookById(@PathVariable Long id);

    @Operation(summary = "Get a book by ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)
    })
    @GetMapping("/isbn/{isbn}")
    ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn);

    @Operation(summary = "Create a new book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    BookDTO createBook(@RequestHeader("Authorization") String token, @RequestBody BookDTO bookDTO);

    @Operation(summary = "Update a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)
    })
    @PutMapping("/{id}")
    ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDetails);

    @Operation(summary = "Delete a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteBook(@RequestHeader("Authorization") String token, @PathVariable Long id);
}