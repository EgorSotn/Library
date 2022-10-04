package org.example.home.repository.author;

import lombok.val;
import org.example.home.domain.Author;
import org.example.home.repository.book.BookRepository;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)

class AuthorRepositoryCustomImplTest {
    private static final long FIRST_AUTHOR_ID = 1l;

    @Autowired
    private TestEntityManager em;
    @Autowired
    private AuthorRepository authorRepository;

    @DisplayName("должен добавлять в бд автора или возвращать по его ")
    @Test
    void getByNameOrCreate() {


        val firstAuthor = em.find(Author.class, FIRST_AUTHOR_ID);
        val actualAuthor =  authorRepository.getByNameOrCreate(firstAuthor).get();

        assertThat(actualAuthor).isEqualTo(firstAuthor);


        val newAuthor = new Author(2,"aaaaa", "1993-05-11");
        val actualAuthorElse = authorRepository.getByNameOrCreate(newAuthor).get();
        assertThat(actualAuthorElse).isEqualTo(newAuthor);
   //     assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERIES_COUNT);
    }
}