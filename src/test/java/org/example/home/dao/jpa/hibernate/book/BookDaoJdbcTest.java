package org.example.home.dao.jpa.hibernate.book;


import lombok.val;
import org.example.home.domain.Author;
import org.example.home.domain.Book;
import org.example.home.domain.Comment;
import org.example.home.domain.Genre;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;


import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Test Book dao")
@DataJpaTest
@Import({BookDaoJpa.class})
class BookDaoJdbcTest {
    private static final long FIRST_BOOK_ID = 1L;
    private static final String BOOK_NAME = "Вася";
    private static final long EXPECTED_QUERIES_COUNT = 3;

    @Autowired
    private BookDaoJpa bookDaoJdbc;
    @Autowired
    private TestEntityManager em;
    @DisplayName("должен корректно сохранять всю информацию о книге")
    @Test
    void shouldSaveBook() {
//        Genre genre = new Genre(1,"drama");
//        Author author = new Author(1, "Pushkin","1993-05-11");
//        Book expectedBook = new Book(3,"evan", "1812-06-06",genre, author );
//        bookDaoJdbc.insert(expectedBook);
//
//        Book actualBook = bookDaoJdbc.getById(expectedBook.getIdBook());
//        assertThat(actualBook).isEqualTo(expectedBook);
        val author = new Author(1, "Pushkin", "1993-05-11");
        val genre = new Genre(3, "abcd");
        val comment = new Comment(2, "dsfvvvxcxv");
        val book = new Book(3,"book", "2000-08-22", genre ,author, comment);
        bookDaoJdbc.save(book);

        assertThat(book.getIdBook()).isGreaterThan(0);

        val actualComment = em.find(Book.class, book.getIdBook());
        assertThat(actualComment).isNotNull().matches(b -> !b.getName().equals(""))
                .matches(b -> b.getComments() != null && b.getComments().size() > 0 && b.getComments().get(0).getId_comment() > 0)
                .matches(b -> b.getAuthor() != null)
                .matches(b -> b.getGenres() != null && b.getGenres().size() > 0)
                .matches(b-> !b.getYear().equals(""));
    }
    @DisplayName("должен загружать информацию о нужном книге по его id")
    @Test
    void shouldReturnExpectedBookById(){
//        Book actualBook = bookDaoJdbc.getById(2);
//        Genre genre = new Genre(1,"drama");
//        Author author = new Author(1, "Pushkin", "1993-05-11");
//        assertThat(actualBook).hasFieldOrPropertyWithValue("idBook",2l)
//                .hasFieldOrPropertyWithValue("name", "eva")
//                .hasFieldOrPropertyWithValue("year",  "1983-05-10")
//                .hasFieldOrPropertyWithValue("genre", genre)
//                .hasFieldOrPropertyWithValue("author", author);
        val optionalActualBook = bookDaoJdbc.getById(FIRST_BOOK_ID);
        val expectedBook = em.find(Book.class, FIRST_BOOK_ID);
        assertThat(optionalActualBook).isPresent().get()
                .isEqualTo(expectedBook);
    }
    @DisplayName("должен удалять заданного книгу по его id")
    @Test
    void shouldDeleteInBookSchema() {
//        Genre genre = new Genre(1,"drama");
//        Author author = new Author(1, "Pushkin","1993-05-11");
//        Book expectedBook = new Book(3,"evan", "1812-06-06",genre, author );
//        bookDaoJdbc.deleteById(expectedBook);
//        Book nullBook = bookDaoJdbc.getById(3);
//        assertNull(nullBook);
        val firstBook = em.find(Book.class, FIRST_BOOK_ID);
        assertThat(firstBook).isNotNull();
        em.detach(firstBook);

        bookDaoJdbc.deleteById(FIRST_BOOK_ID);
        val deletedBook = em.find(Book.class, FIRST_BOOK_ID);

        assertThat(deletedBook).isNull();
    }
//
    @DisplayName("должен изменять имя заданного книги по его id")
    @Test
    void shouldUpdateNameBook() {
//        Genre genre = new Genre(1,"drama");
//        Author author = new Author(1, "Pushkin","1993-05-11");
//        Book expectedBook = new Book(2,"evan", "1812-06-06",genre, author );
//        String expectedStr = "abcd";
//        bookDaoJdbc.update(expectedBook,expectedStr);
//
//        String actualStr = bookDaoJdbc.getById(2).getName();
//        assertEquals(expectedStr, actualStr);
        val firstBook = em.find(Book.class, FIRST_BOOK_ID);
        String oldName = firstBook.getName();
        em.detach(firstBook);

        bookDaoJdbc.updateNamedById(FIRST_BOOK_ID, BOOK_NAME);
        val updatedStudent = em.find(Book.class, FIRST_BOOK_ID);

        assertThat(updatedStudent.getName()).isNotEqualTo(oldName).isEqualTo(BOOK_NAME);
    }
//
    @DisplayName("должен загружать список всех книг с полной информацией о них")
    @Test
    void shouldGetAllBook() {
//        Genre genre = new Genre(1,"drama");
//        Author author = new Author(1, "Pushkin", "1993-05-11");
//        Book book1 = new Book(1, "evangelion",  "1999-05-11", genre, author );
//        Book book2 = new Book(2, "eva", "1983-05-10", genre, author);
//        List<Book> bookList = new ArrayList<>();
//        bookList.add(book1);
//        bookList.add(book2);
//        assertThat(Arrays.equals(new List[]{bookList}, new List[]{bookDaoJdbc.getAllNotProb()})).isTrue();
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        val books  = bookDaoJdbc.getAll();
        assertThat(books.size()).isEqualTo(2);

        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERIES_COUNT);
    }
    @Test
    void shouldExistBookToReturnBool(){
        val genre = Collections.singletonList( new Genre("prosa"));
        val author = new Author("Lermonov", "1812-01-01");
        val book = new Book("War and Peace", "1834-01-01", genre, author, null);
        val isNotExist = bookDaoJdbc.isExistBook(book);
        assertFalse(isNotExist);
        val saveBook = em.merge(book);

        val isExist = bookDaoJdbc.isExistBook(saveBook);
        assertTrue(isExist);
    }
}