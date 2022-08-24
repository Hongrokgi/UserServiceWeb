package com.devo.bim.Board.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String title;

    private String content;

    private String writer;

    private int hits;

    private char deleteYn;

    private LocalDateTime createdDate = LocalDateTime.now();

    private LocalDateTime modifiedDate;


    @Builder
    public Board(String title, String content, String writer, int hits, char deleteYn) {
        this.title=title;
        this.content=content;
        this.writer=writer;
        this.hits=hits;
        this.deleteYn=deleteYn;
    }

    public void increaseHits() {
        this.hits++;
    }

    public void update(String title, String content) {
        this.title=title;
        this.content=content;
        this.modifiedDate=LocalDateTime.now();
    }

    public void delete() {
        this.deleteYn='Y';
    }
}
