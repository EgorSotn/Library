package org.example.home.repository.book;

import org.example.home.domain.Book;
import org.hibernate.jpa.QueryHints;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class BookRepositoryCustomImpl implements BookRepositoryCustom{
    @PersistenceContext
    EntityManager em;
    @Override
    public List<Book> getAllWithComment() {
        List<Book> books = em.createQuery("SELECT DISTINCT b FROM Book b " +
                                "LEFT JOIN FETCH b.comments bc "
                        , Book.class)
                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                .getResultList();

        books =  em.createQuery("SELECT DISTINCT b from Book b " +
                        "LEFT JOIN FETCH b.genres bg "+
                        "LEFT JOIN FETCH b.author ba " +
                        "WHERE b in :books", Book.class)
                .setParameter("books", books)
                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                .getResultList();

        return books;
    }

    @Override
    public boolean isExistBook(Book book) {
        TypedQuery<Book> query = em.createQuery("SELECT DISTINCT b FROM Book b " +
                "LEFT JOIN FETCH b.genres "+
                "LEFT JOIN FETCH b.author " +
                "WHERE b.name = :name_book AND b.author.nameAuthor = :name_author", Book.class);
        query.setParameter("name_author",book.getAuthor().getNameAuthor());
        query.setParameter("name_book",book.getName());
        List<Book> books = query.getResultList();

//        List<Book> books = em.createQuery("SELECT DISTINCT b FROM Book b " +
//                                "LEFT JOIN FETCH b.comments bc "
//                        , Book.class)
//                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
//                .getResultList();
//
//        books =  em.createQuery("SELECT DISTINCT b from Book b " +
//                        "LEFT JOIN FETCH b.genres bg "+
//                        "LEFT JOIN FETCH b.author ba " +
//                        "WHERE b in :books AND bg.author.nameAuthor = :name_author AND b.name_book = :name_book", Book.class)
//                .setParameter("books", books)
//                .setParameter("name_author",book.getAuthor().getNameAuthor())
//                .setParameter("name_book",book.getName())
//                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
//                .getResultList();

        return !books.isEmpty();
    }

    @Override
    public Book update(Book book) {
        em.flush();
        em.merge(book);
         return book;
    }
}
