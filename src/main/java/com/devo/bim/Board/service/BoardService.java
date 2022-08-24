package com.devo.bim.Board.service;

import com.devo.bim.Board.controller.dto.BoardRequestDto;
import com.devo.bim.Board.controller.dto.BoardResponseDto;
import com.devo.bim.Board.domain.entity.Board;
import com.devo.bim.Board.repository.BoardRepository;
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
