package org.example.home.repository.book;

import org.example.home.domain.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>, BookRepositoryCustom {

    @Override
    <S extends Book> S save(S entity);

    @EntityGraph(attributePaths = {"author","genres"})
    List<Book> findAll();

    @Modifying
    @Query("update Book b set b.name =:name WHERE b.idBook =:id")
    void updateNameById(@Param("name")String name,@Param("id") long id);
}