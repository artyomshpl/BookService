package com.shep.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO {
    private String isbn;
    private String title;
    private String genre;
    private String description;
    private String author;
}