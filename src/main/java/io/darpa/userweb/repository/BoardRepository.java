package io.darpa.userweb.repository;

import io.darpa.userweb.domain.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAllByDeleteYn(char deleteYn, Pageable pageable);

    Page<Board> findAllByDeleteYnAndTitleContainingIgnoreCase(char deleteYn, String keyword, Pageable pageable);

    Page<Board> findAllByDeleteYnAndWriterContainingIgnoreCase(char deleteYn, String keyword, Pageable pageable);

    Page<Board> findAllByDeleteYnAndContentContainingIgnoreCase(char deleteYn, String keyword, Pageable pageable);
}
