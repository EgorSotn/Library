package org.example.home.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

import java.util.Collections;
import java.util.List;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "book",uniqueConstraints = @UniqueConstraint(columnNames = {"name_book", "id_author" }))
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_book")
    private long idBook;
    @Column(name = "name_book")
    private String name;
    @Column(name = "year_book")
    private String year;

    @Fetch(FetchMode.SELECT)
    @ManyToMany(targetEntity = Genre.class, fetch = FetchType.LAZY, cascade = {CascadeType.ALL ,CascadeType.DETACH})
    @JoinTable(name = "book_genre", joinColumns = @JoinColumn(name = "id_book"),
            inverseJoinColumns = @JoinColumn(name = "id_genre"))
    private List<Genre> genres;

    @Fetch(FetchMode.SELECT)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_author")
    private Author author;

    @Fetch(FetchMode.SELECT)
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private List<Comment> comments;

    public Book(long idBook, String name, String year, Genre genre, Author author, Comment comments) {
        this.idBook = idBook;
        this.name = name;
        this.year = year;
        this.genres = Collections.singletonList(genre);
        this.author = author;
        this.comments = Collections.singletonList(comments);
    }

    public Book(long idBook, String name, String year, Genre genre, Author author) {
        this.idBook = idBook;
        this.name = name;
        this.year = year;
        this.genres = Collections.singletonList(genre);
        this.author = author;
    }

    public Book(String name, String year, List<Genre> genres, Author author, List<Comment> comments) {
        this.name = name;
        this.year = year;
        this.genres = genres;
        this.author = author;
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Book{" +
                "idBook=" + idBook +
                ", name='" + name + '\'' +
                ", year='" + year + '\'' +
                ", genres=" + genres +
                ", author=" + author +
                '}';
    }
}
