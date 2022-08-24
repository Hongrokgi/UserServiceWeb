package com.devo.bim.Board.repository;

import com.devo.bim.Board.domain.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<BoardFile, Long> {
    List<BoardFile> findAllByBoardId(Long boardId);
}
