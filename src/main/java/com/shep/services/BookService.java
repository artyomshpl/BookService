package com.shep.services;

import com.shep.dto.BookDTO;
import com.shep.dto.FreeBookDTO;
import com.shep.entities.Book;
import com.shep.mapper.BookMapper;
import com.shep.repositories.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final RestTemplate restTemplate;

    public BookService(BookRepository bookRepository, RestTemplate restTemplate) {
        this.bookRepository = bookRepository;
        this.restTemplate = restTemplate;
    }

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

        FreeBookDTO freeBookDTO = new FreeBookDTO();
        freeBookDTO.setBookId(savedBook.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<FreeBookDTO> entity = new HttpEntity<>(freeBookDTO, headers);

        restTemplate.exchange(
                "http://localhost:8083/api/library",
                HttpMethod.POST,
                entity,
                FreeBookDTO.class
        );

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
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:8083/api/library/book/" + id,
                HttpMethod.DELETE,
                entity,
                Void.class
        );
    }
}
