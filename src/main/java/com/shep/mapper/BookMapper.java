package com.shep.mapper;

import com.shep.dto.BookDTO;
import com.shep.entities.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    @Mapping(target = "id", ignore = true)
    Book toEntity(BookDTO bookDTO);

    BookDTO toDto(Book book);
}