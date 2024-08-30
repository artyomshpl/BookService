package com.shep.services.interfaces;

public interface LibraryServiceClientInterface {
    void createFreeBook(Long bookId, String token);
    void deleteFreeBook(Long bookId, String token);
}