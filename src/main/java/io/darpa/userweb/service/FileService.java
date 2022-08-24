package io.darpa.userweb.service;

import io.darpa.userweb.controller.dto.FileRequestDto;
import io.darpa.userweb.controller.dto.FileResponseDto;
import io.darpa.userweb.domain.entity.BoardFile;
import io.darpa.userweb.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    public Long save(FileRequestDto dto) {
        BoardFile entity = fileRepository.save(dto.toEntity());
        return entity.getId();
    }

    public List<FileResponseDto> findByBoardId(Long boardId) {
        List<BoardFile> list = fileRepository.findAllByBoardId(boardId);
        return list.stream().map(FileResponseDto::new).collect(Collectors.toList());
    }

    public FileResponseDto findById(Long id) {
        BoardFile entity = fileRepository.findById(id).orElseThrow(()-> new EntityNotFoundException());
        return new FileResponseDto(entity);
    }
}
