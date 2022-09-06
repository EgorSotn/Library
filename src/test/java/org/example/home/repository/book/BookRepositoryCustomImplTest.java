package org.example.home.repository.book;

import lombok.val;
import org.example.home.domain.Author;
import org.example.home.domain.Book;
import org.example.home.domain.Genre;
import org.example.home.repository.author.AuthorRepository;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class BookRepositoryCustomImplTest {
    private static final long EXPECTED_QUERIES_COUNT = 3;
    private static final long FIRST_BOOK_ID = 1l;
    private static final String BOOK_NAME = "sffdsfs";
    @Autowired
    private TestEntityManager em;
    @Autowired
    private BookRepository bookRepository;
    @DisplayName("должен возвращать список всех книг")
    @Test
    void shouldGetAllBookWithComment(){
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        val books  = bookRepository.getAllWithComment();
        assertThat(books.size()).isEqualTo(2);
    }
    @DisplayName("должен обновлять имя книги")
    @Test
    void shouldUpdateNameById(){
        val firstBook = em.find(Book.class, FIRST_BOOK_ID);
        String oldName = firstBook.getName();
        em.detach(firstBook);

        bookRepository.updateNameById(BOOK_NAME, FIRST_BOOK_ID);
        val updatedStudent = em.find(Book.class, FIRST_BOOK_ID);

        assertThat(updatedStudent.getName()).isNotEqualTo(oldName).isEqualTo(BOOK_NAME);

    }
    @Test
    void shouldExistBookToReturnBool(){
        val genre = Collections.singletonList( new Genre("prosa"));
        val author = new Author("Lermonov", "1812-01-01");
        val book = new Book("War and Peace", "1834-01-01", genre, author, null);
        val isNotExist = bookRepository.isExistBook(book);
        assertFalse(isNotExist);
        val saveBook = em.merge(book);

        val isExist = bookRepository.isExistBook(saveBook);
        assertTrue(isExist);
    }
}