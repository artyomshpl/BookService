package com.shep.services;

import com.shep.entities.Book;
import com.shep.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Book createBook(Book book) {
//        if(TokenValidator.tokenValidCheck(token)){
            Book savedBook = bookRepository.save(book);
            // Отправка запроса в LibraryService
            //restTemplate.postForEntity("http://localhost:8083/api/library", savedBook, FreeBookDTO.class);
            return savedBook;
//        } else {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
//        }

    }

    public Book updateBook(Long id, Book bookDetails) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            book.setIsbn(bookDetails.getIsbn());
            book.setTitle(bookDetails.getTitle());
            book.setGenre(bookDetails.getGenre());
            book.setDescription(bookDetails.getDescription());
            book.setAuthor(bookDetails.getAuthor());
            return bookRepository.save(book);
        }
        return null;
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
