package org.example.home.service;


import org.example.home.domain.Book;
import org.example.home.dto.BookDto;

public interface ConvertDto {
    Book fromBookDtoToBook(BookDto bookDto);
    BookDto bookConvertToDto(Book book);
}
