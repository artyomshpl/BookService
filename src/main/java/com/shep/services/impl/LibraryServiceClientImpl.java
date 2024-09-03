package com.shep.services.impl;

import com.shep.dto.FreeBookDTO;
import com.shep.services.interfaces.LibraryServiceClientInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class LibraryServiceClientImpl implements LibraryServiceClientInterface {

    private final RestTemplate restTemplate;

    @Override
    public void createFreeBook(Long bookId, String token) {
        FreeBookDTO freeBookDTO = new FreeBookDTO();
        freeBookDTO.setBookId(bookId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<FreeBookDTO> entity = new HttpEntity<>(freeBookDTO, headers);

        restTemplate.exchange(
                "http://library-service:8083/api/library",
                HttpMethod.POST,
                entity,
                FreeBookDTO.class
        );
    }

    @Override
    public void deleteFreeBook(Long bookId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://library-service:8083/api/library/book/" + bookId,
                HttpMethod.DELETE,
                entity,
                Void.class
        );
    }
}