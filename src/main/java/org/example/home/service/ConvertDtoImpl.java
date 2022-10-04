package org.example.home.service;

import org.example.home.domain.Author;
import org.example.home.domain.Book;
import org.example.home.domain.Genre;
import org.example.home.dto.BookDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConvertDtoImpl implements ConvertDto{


    @Override
    public Book fromBookDtoToBook(BookDto bookDto) {
        Book book = new Book();
        if(bookDto.getIdBook() == null){

        }
        else  book.setIdBook(bookDto.getIdBook());

        book.setName(bookDto.getNameBook());
        book.setYear(bookDto.getYearBook());
        book.setAuthor(new Author(bookDto.getNameAuthor(), bookDto.getYearAuthor()));
        String[] names = bookDto.getNamesGenre().split(", ");
        List<Genre> genres = new ArrayList<>();
        for (String str: names) {
            genres.add(new Genre(str));
        }

        book.setGenres(genres);
        return book;
    }

    @Override
    public BookDto bookConvertToDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setIdBook(book.getIdBook());
        bookDto.setNameBook(book.getName());
        bookDto.setYearBook(book.getYear());
        bookDto.setNameAuthor(book.getAuthor().getNameAuthor());
        bookDto.setYearAuthor(book.getAuthor().getYear());


        if(book.getGenres().isEmpty()){
            bookDto.setNamesGenre(null);
        }
        else bookDto.setNamesGenre(book.getGenres().stream().map(Genre::getNameGenre).collect(Collectors.joining(", ")));

        return bookDto;
    }
}
