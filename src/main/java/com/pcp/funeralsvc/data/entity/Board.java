package com.pcp.funeralsvc.data.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@Table(name = "board")
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "content")
    private String content;

//    @Column(name = "is_customer")
//    private Boolean is_customer;
//
//    @Column(name = "is_fileAttached")
//    private Boolean is_fileAttached;
}
