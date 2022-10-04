package org.example.home.repository.author;

import org.example.home.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface AuthorRepository extends JpaRepository<Author, Long>, AuthorRepositoryCustom {
}