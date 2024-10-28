package com.pcp.funeralsvc.data.dto.response;

import com.pcp.funeralsvc.data.entity.Board;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
public class BoardResponseDto {

    private Long boardId;
    private String title;;

    private String publishDate;

    public BoardResponseDto(Board board) {
        this.boardId = board.getBoardId();
        this.title = board.getTitle();
        this.publishDate = board.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"));
    }
}
