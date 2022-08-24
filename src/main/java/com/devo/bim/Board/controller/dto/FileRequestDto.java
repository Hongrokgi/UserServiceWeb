package com.devo.bim.Board.controller.dto;

import com.devo.bim.Board.domain.entity.BoardFile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileRequestDto {
    private String originName;
    private String saveName;
    private int size;
    private char deleteYn;
    private Long boardId;

    public BoardFile toEntity() {
        return BoardFile.builder()
                .originName(originName)
                .saveName(saveName)
                .size(size)
                .deleteYn(deleteYn)
                .boardId(boardId)
                .build();
    }
}
