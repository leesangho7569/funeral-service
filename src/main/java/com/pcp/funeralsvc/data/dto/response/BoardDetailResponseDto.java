package com.pcp.funeralsvc.data.dto.response;


import com.pcp.funeralsvc.data.entity.Board;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardDetailResponseDto extends BoardResponseDto{

    private String content;

    public BoardDetailResponseDto(Board board) {

        super(board);
        this.content = board.getContent();
    }
}
