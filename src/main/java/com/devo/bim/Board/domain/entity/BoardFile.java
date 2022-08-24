package com.devo.bim.Board.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardFile {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String originName;

    private String saveName;

    private int size;

    private char deleteYn;

    private LocalDateTime insertTime = LocalDateTime.now();

    private Long boardId;


    @Builder
    public BoardFile(String originName, String saveName, int size, char deleteYn, Long boardId) {
        this.originName = originName;
        this.saveName = saveName;
        this.size = size;
        this.deleteYn = deleteYn;
        this.boardId = boardId;
    }
}
