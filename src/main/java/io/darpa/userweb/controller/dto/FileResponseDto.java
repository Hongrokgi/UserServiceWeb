package io.darpa.userweb.controller.dto;

import io.darpa.userweb.domain.entity.BoardFile;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FileResponseDto {
    private Long id;
    private String originName;
    private String saveName;
    private int size;
    private char deleteYn;
    private LocalDateTime insertTime;
    private Long boardId;

    public FileResponseDto(BoardFile entity) {
        this.id=entity.getId();
        this.originName=entity.getOriginName();
        this.saveName=entity.getSaveName();
        this.size=entity.getSize();
        this.deleteYn=entity.getDeleteYn();
        this.insertTime=entity.getInsertTime();
        this.boardId=entity.getBoardId();
    }
}
