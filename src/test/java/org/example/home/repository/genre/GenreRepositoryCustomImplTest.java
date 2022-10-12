package org.example.home.repository.genre;

import lombok.val;

import org.example.home.domain.Genre;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class GenreRepositoryCustomImplTest {
    private static final long FIRST_GENRE_ID = 1l;
    @Autowired
    private TestEntityManager em;
    @Autowired
    private GenreRepository genreRepository;

    @DisplayName("должен добавлять в бд жанра или возвращать по его ")
    @Test
    void getByNameOrCreate() {
        val firstGenre = em.find(Genre.class,FIRST_GENRE_ID );
        val actualGenre =  genreRepository.getByNameOrCreate(firstGenre).get();

        assertThat(actualGenre).isEqualTo(firstGenre);


        val newGenre = new Genre(4,"aaaaaaa");
        val actualGenreElse = genreRepository.getByNameOrCreate(newGenre).get();
        assertThat(actualGenreElse).isEqualTo(newGenre);
    }
}