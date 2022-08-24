package io.darpa.userweb.service;

import io.darpa.userweb.controller.dto.BoardRequestDto;
import io.darpa.userweb.controller.dto.BoardResponseDto;
import io.darpa.userweb.domain.entity.Board;
import io.darpa.userweb.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    @Transactional
    public Long save(BoardRequestDto requestDto) {
        Board savedEntity = boardRepository.save(requestDto.toEntity());
        return savedEntity.getId();
    }
    @Transactional
    public BoardResponseDto findById(Long id) {
        Board entity = boardRepository.findById(id).orElseThrow(()->new EntityNotFoundException());
        entity.increaseHits();
        return new BoardResponseDto(entity);
    }

    @Transactional
    public Long update(Long id, BoardRequestDto requestDto) {
        Board entity = boardRepository.findById(id).orElseThrow(()->new EntityNotFoundException());
        entity.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    @Transactional
    public Long delete(Long id) {
        Board entity = boardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        entity.delete();
        return id;
    }

    public Page<Board> list(int page, char deleteYn) {
        return boardRepository.findAllByDeleteYn(deleteYn, PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "id")));
    }

    public Page<Board> searchTitle(String keyword, char deleteYn, int page) {
        return boardRepository.findAllByDeleteYnAndTitleContainingIgnoreCase(deleteYn, keyword, PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC,"id")));
    }

    public Page<Board> searchWriter(String keyword, char deleteYn, int page) {
        return boardRepository.findAllByDeleteYnAndWriterContainingIgnoreCase(deleteYn, keyword,PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC,"id")));
    }

    public Page<Board> searchContent(String keyword, char deleteYn, int page) {
        return boardRepository.findAllByDeleteYnAndContentContainingIgnoreCase(deleteYn,keyword,PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC,"id")));
    }

}
