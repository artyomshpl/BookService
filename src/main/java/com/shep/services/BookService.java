package com.shep.services;

import com.shep.dto.BookDTO;
import com.shep.entities.Book;
import com.shep.exceptions.DuplicateIsbnException;
import com.shep.exceptions.NotFoundException;
import com.shep.mapper.BookMapper;
import com.shep.repositories.BookRepository;
import com.shep.services.impl.LibraryServiceClientImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final LibraryServiceClientImpl libraryServiceClientImpl;

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
        Optional<Book> existingBook = bookRepository.findByIsbn(bookDTO.getIsbn());
        if (existingBook.isPresent()) {
            throw new DuplicateIsbnException("Book with ISBN " + bookDTO.getIsbn() + " already exists");
        }

        Book book = BookMapper.INSTANCE.toEntity(bookDTO);
        Book savedBook = bookRepository.save(book);

        libraryServiceClientImpl.createFreeBook(savedBook.getId(), token);

        return BookMapper.INSTANCE.toDto(savedBook);
    }


    public BookDTO updateBook(Long id, BookDTO bookDetails) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Book not found with id " + id));
        book.setIsbn(bookDetails.getIsbn());
        book.setTitle(bookDetails.getTitle());
        book.setGenre(bookDetails.getGenre());
        book.setDescription(bookDetails.getDescription());
        book.setAuthor(bookDetails.getAuthor());
        Book savedBook = bookRepository.save(book);
        return BookMapper.INSTANCE.toDto(savedBook);
    }

    public void deleteBook(Long id, String token) {
        if (!bookRepository.existsById(id)) {
            throw new NotFoundException("Book not found with id " + id);
        }
        bookRepository.deleteById(id);
        libraryServiceClientImpl.deleteFreeBook(id, token);
    }
}
