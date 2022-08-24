package io.darpa.userweb.repository;

import io.darpa.userweb.domain.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<BoardFile, Long> {
    List<BoardFile> findAllByBoardId(Long boardId);
}
