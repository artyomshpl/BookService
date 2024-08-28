package com.shep.services;

import com.shep.dto.BookDTO;
import com.shep.dto.FreeBookDTO;
import com.shep.entities.Book;
import com.shep.mapper.BookMapper;
import com.shep.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private RestTemplate restTemplate;



    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book getBookByIsbn(String isbn) {
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

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
