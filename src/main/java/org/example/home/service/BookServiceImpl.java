package org.example.home.service;

import lombok.AllArgsConstructor;
import org.example.home.domain.Author;
import org.example.home.domain.Book;
import org.example.home.domain.Genre;
import org.example.home.exception.NotFoundException;
import org.example.home.repository.author.AuthorRepository;
import org.example.home.repository.book.BookRepository;
import org.example.home.repository.genre.GenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService{
    private BookRepository bookRepository;
    private GenreRepository genreRepository;
    private AuthorRepository authorRepository;
    @Override
    @Transactional
    public Book createBook(Book book) {
        List<Genre> genres = book.getGenres();
        List<Genre> addGenre = new ArrayList<>();
        for (Genre genre : genres) {
             addGenre.add(genreRepository.getByNameOrCreate(genre).get());
        }
//        genres.stream().map(genre -> genreRepository.getByNameOrCreate(genre).get()).collect(Collectors.toList());
        Author addAuthor = authorRepository.getByNameOrCreate(book.getAuthor()).get();
        if (bookRepository.isExistBook(book)) {
            return null;
        }
        book.setGenres(addGenre);
        book.setAuthor(addAuthor);
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) throws NotFoundException {
        Book deleteBook = bookRepository.findById(id).orElseThrow(()-> new NotFoundException(Long.toString(id)));
        bookRepository.delete(deleteBook);
    }
    @Transactional
    @Override
    public Book updateBook(Book updateBook)throws NotFoundException {
        Book book =  bookRepository.findById(updateBook.getIdBook())
                .orElseThrow(()->new NotFoundException("book "+updateBook.getIdBook()));

        book.setName(updateBook.getName());
        book.setYear(updateBook.getYear());
        book.setGenres(updateBook.getGenres()
                .stream().map(genre -> genreRepository.getByNameOrCreate(genre))
                .map(Optional::get).collect(Collectors.toList()));
        book.setAuthor(authorRepository.getByNameOrCreate(updateBook.getAuthor()).get());

        return bookRepository.save(book);

    }

    @Override
    public List<Book> getAllBook() {
        return bookRepository.findAll();
    }

    @Override
    public Book getById(Long id) throws NotFoundException{
        return bookRepository.findById(id).orElseThrow(()-> new NotFoundException( Long.toString(id)));
    }
}
