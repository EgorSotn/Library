package org.example.home.dao.jpa.hibernate.book;


import org.example.home.dao.BookDao;
import org.example.home.domain.Book;


import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.*;

import java.util.List;
import java.util.Optional;



@Repository
public class BookDaoJpa implements BookDao {
    @PersistenceContext
    EntityManager em;


    @Override
    public Book save(Book book) {
//        namedParameterJdbcOperations.update("INSERT INTO book (id_book, name,year,id_genre,id_author) " +
//                "values(:id_book, :name, :year, :id_genre, :id_author)", Map.of("id_book", book.getIdBook(),
//                "name", book.getName(), "year", book.getYear(), "id_genre", book.getGenre().getIdGenre(),
//                "id_author", book.getAuthor().getIdAuthor()));
        if(book.getIdBook() <= 0){
            em.persist(book);
            return book;
        }
        else {
            return em.merge(book);
        }
    }

    @Override
    public void deleteById(long id) {
//        namedParameterJdbcOperations.update("DELETE FROM book WHERE id_book = :id_book",
//                Map.of("id_book", book.getIdBook()));
        Query query = em.createQuery("delete from Book b where b.idBook = :id ");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void updateNamedById(long id, String  name) {
//        namedParameterJdbcOperations.update("UPDATE book SET name = :name WHERE id_book = :id",
//                Map.of("name", name, "id", book.getIdBook()));
        Query query = em.createQuery("update Book b " +
                "set b.name =:name " +
                "WHERE b.idBook =:id");
        query.setParameter("name", name);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public Optional<Book> getById(long id) {

        return  Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public boolean isExistBook(Book book) {
        TypedQuery<Book> query = em.createQuery("SELECT DISTINCT b FROM Book b " +
                "LEFT JOIN FETCH b.genres BG "+
                "LEFT JOIN FETCH b.author BA " +
                "WHERE b.author.nameAuthor = :name_author AND b.name = :name_book", Book.class);
        query.setParameter("name_author",book.getAuthor().getNameAuthor());
        query.setParameter("name_book",book.getName());
        List<Book> books = query.getResultList();

        return !books.isEmpty();
    }

//    @Override
//    public List<Book> getAllNotProb() {
//        List<Book> books =  namedParameterJdbcOperations.query("SELECT b.id_book id_book, b.name, b.year," +
//                "a.id_author id_author, a.name authorName, " +
//                "a.year authorYear, g.id_genre id_genre, g.name genreName " +
//                "from (book b left join author a on " +
//                "b.id_author = a.id_author) left join genre g on b.id_genre = g.id_genre", new BookResultSetExtractor());
//        return books;
//        return null;
//    }

    @Override
    public List<Book> getAll() {

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



}