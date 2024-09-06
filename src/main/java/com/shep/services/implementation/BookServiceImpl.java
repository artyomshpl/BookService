package com.shep.services.implementation;

import com.shep.dataTransferObjects.BookDTO;
import com.shep.entity.Book;
import com.shep.exceptions.DuplicateIsbnException;
import com.shep.exceptions.NotFoundException;
import com.shep.mapper.BookMapper;
import com.shep.repository.BookRepository;
import com.shep.services.interfaces.LibraryServiceClientInterface;
import com.shep.services.interfaces.BookServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookServiceInterface {

    private final BookRepository bookRepository;
    private final LibraryServiceClientInterface libraryServiceClientInterface;

    @Override
    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public Optional<Book> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Override
    public BookDTO createBook(BookDTO bookDTO, String token) {
        Optional<Book> existingBook = bookRepository.findByIsbn(bookDTO.getIsbn());
        if (existingBook.isPresent()) {
            throw new DuplicateIsbnException("Book with ISBN " + bookDTO.getIsbn() + " already exists");
        }

        Book book = BookMapper.INSTANCE.toEntity(bookDTO);
        Book savedBook = bookRepository.save(book);

        libraryServiceClientInterface.createFreeBook(savedBook.getId(), token);

        return BookMapper.INSTANCE.toDto(savedBook);
    }

    @Override
    public BookDTO updateBook(Long id, BookDTO bookDetails) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Book not found with id " + id));

        Optional<Book> existingBook = bookRepository.findByIsbnAndIdNot(bookDetails.getIsbn(), id);
        if (existingBook.isPresent()) {
            throw new DuplicateIsbnException("Book with ISBN " + bookDetails.getIsbn() + " already exists");
        }

        book.setIsbn(bookDetails.getIsbn());
        book.setTitle(bookDetails.getTitle());
        book.setGenre(bookDetails.getGenre());
        book.setDescription(bookDetails.getDescription());
        book.setAuthor(bookDetails.getAuthor());
        Book savedBook = bookRepository.save(book);
        return BookMapper.INSTANCE.toDto(savedBook);
    }

    @Override
    public void deleteBook(Long id, String token) {
        if (!bookRepository.existsById(id)) {
            throw new NotFoundException("Book not found with id " + id);
        }
        bookRepository.deleteById(id);
        libraryServiceClientInterface.deleteFreeBook(id, token);
    }
}
