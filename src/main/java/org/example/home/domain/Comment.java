package org.example.home.domain;

import lombok.*;


import javax.persistence.*;

@Entity
@Table(name = "comments")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "id_comment")
    private long id_comment;

    @ManyToOne
    @JoinColumn(name = "id_book")
    private Book book;

    public Comment(long id_comment, String text) {
        this.id_comment = id_comment;
        this.text = text;
    }

    public Comment(String text) {
        this.text = text;
    }

    @Column(name = "text", nullable = false)
    private String text;


    @Override
    public String toString() {
        return "Comment{" +
                "idComment=" + id_comment +
                ", text='" + text + '\'' +
                '}';
    }

}
