package com.pcp.funeralsvc.data.repository;

import com.pcp.funeralsvc.data.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByTitle(String title);

    Optional<Board> findByBoardId(Long boardId);
}
