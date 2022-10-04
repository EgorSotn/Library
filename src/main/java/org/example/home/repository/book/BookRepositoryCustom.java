package org.example.home.repository.book;

import org.example.home.domain.Book;

import java.util.List;

public interface BookRepositoryCustom {
    List<Book> getAllWithComment();
    boolean isExistBook(Book book);
    Book update(Book book);
}
