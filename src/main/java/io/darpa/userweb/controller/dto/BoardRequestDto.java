package io.darpa.userweb.controller.dto;

import io.darpa.userweb.domain.entity.Board;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardRequestDto {
    private String title;
    private String writer;
    private String content;
    private char deleteYn;

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .writer(writer)
                .content(content)
                .hits(0)
                .deleteYn('N')
                .build();
    }
}
