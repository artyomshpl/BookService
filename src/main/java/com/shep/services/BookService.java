package com.shep.services;

import com.shep.dto.BookDTO;
import com.shep.entities.Book;
import com.shep.mapper.BookMapper;
import com.shep.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final LibraryServiceClient libraryServiceClient;

    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Optional<Book> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public BookDTO createBook(BookDTO bookDTO, String token) {
        Book book = BookMapper.INSTANCE.toEntity(bookDTO);
        Book savedBook = bookRepository.save(book);

        libraryServiceClient.createFreeBook(savedBook.getId(), token);

        return BookMapper.INSTANCE.toDto(savedBook);
    }

    public BookDTO updateBook(Long id, BookDTO bookDetails) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            book.setIsbn(bookDetails.getIsbn());
            book.setTitle(bookDetails.getTitle());
            book.setGenre(bookDetails.getGenre());
            book.setDescription(bookDetails.getDescription());
            book.setAuthor(bookDetails.getAuthor());
            Book savedBook = bookRepository.save(book);
            return BookMapper.INSTANCE.toDto(savedBook);
        }
        return null;
    }

    public void deleteBook(Long id, String token) {
        bookRepository.deleteById(id);
        libraryServiceClient.deleteFreeBook(id, token);
    }
}