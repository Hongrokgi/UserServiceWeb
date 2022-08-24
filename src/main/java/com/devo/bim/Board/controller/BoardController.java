package com.devo.bim.Board.controller;

import com.devo.bim.Board.controller.dto.BoardRequestDto;
import com.devo.bim.Board.controller.dto.BoardResponseDto;
import com.devo.bim.Board.controller.dto.FileRequestDto;
import com.devo.bim.Board.controller.dto.FileResponseDto;
import com.devo.bim.Board.domain.entity.Board;
import com.devo.bim.Board.service.BoardService;
import com.devo.bim.Board.service.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    private final FileService fileService;

    private final String getRandomString() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    private final String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
    /* 업로드 경로 */
    private final String uploadPath = Paths.get("C:","BimBoard","upload", today).toString();

    /*게시글 등록*/
    @ResponseBody
    @PostMapping("/board/write")
    public Long save(@RequestParam(value = "uploadFile", required = false) MultipartFile[] files,
                     @RequestParam(value = "title") String title,
                     @RequestParam(value = "writer") String writer,
                     @RequestParam(value = "content") String content) {
        BoardRequestDto requestDto = new BoardRequestDto();
        requestDto.setTitle(title);
        requestDto.setWriter(writer);
        requestDto.setContent(content);
        var savedBoardId = boardService.save(requestDto); // 게시글 우선 등록받아 반환받은 아이디를 통해 외래키 매칭
        File dir = new File(uploadPath);
        if(dir.exists() == false) {
            dir.mkdirs();
        }
        if(files != null) {
            for (MultipartFile multipartFile : files) {
                try {
                    /* 파일 확장자 */
                    String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
                    /* 서버에 저장할 파일명 = 랜덤문자열 + 파일 확장자 */
                    String saveName = getRandomString()+"."+extension;
                    File target = new File(uploadPath, saveName);
                    multipartFile.transferTo(target);
                    int size = (int) multipartFile.getSize();
                    String originName = multipartFile.getOriginalFilename();
                    char deleteYn = 'N';
                    FileRequestDto fileInfo = new FileRequestDto();
                    fileInfo.setOriginName(originName);
                    fileInfo.setSaveName(saveName);
                    fileInfo.setSize(size);
                    fileInfo.setDeleteYn(deleteYn);
                    fileInfo.setBoardId(savedBoardId);
                    var successYn = fileService.save(fileInfo);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }
    /* 2. 게시글 목록(삭제되지 않은 게시글 & 페이징) */
    @ResponseBody
    @GetMapping("/board/getList")
    public ModelAndView findList(@RequestParam("page") int page) {
        var deleteYn = 'N';
        Page<Board> list = boardService.list(page, deleteYn);
        int totalPage = list.getTotalPages();
        var content = list.getContent();
        ModelAndView mv = new ModelAndView("jsonView");
        mv.addObject("board",content);
        mv.addObject("totalPage",totalPage);
        mv.addObject("pageNo",page);
        return mv;
    }
    @ResponseBody
    @GetMapping("/board/listPerPage")
    public ModelAndView boardPerPage(@RequestParam(name = "page") int page) {
        var deleteYn = 'N';
        Page<Board> list = boardService.list(page, deleteYn);
        var content = list.getContent();
        ModelAndView mv = new ModelAndView("jsonView");
        mv.addObject("board",content);
        return mv;
    }

    /* 3. 게시글 상세보기 */
    @ResponseBody
    @GetMapping("/board/detail")
    public BoardResponseDto findById(@RequestParam(value = "id") Long id) {
        return boardService.findById(id);
    }

    /* 3.5 게시글 상세보기 파일리스트 가져오기 */
    @ResponseBody
    @GetMapping("/file/list")
    public List<FileResponseDto> findFilesByBoardId(@RequestParam(value = "id") Long id) {
        var boardId = id;
        return fileService.findByBoardId(boardId);
    }
    /* 4. 게시글 수정하기 */
    @ResponseBody
    @PostMapping("/board/update")
    public Long update(@RequestParam(value = "id") Long id,
                       @RequestParam(value = "title") String title,
                       @RequestParam(value = "content") String content) {
        BoardRequestDto requestDto = new BoardRequestDto();
        requestDto.setTitle(title);
        requestDto.setContent(content);
        return boardService.update(id, requestDto);
    }
    /* 5. 게시글 삭제하기 */
    @ResponseBody
    @GetMapping("/board/delete")
    public Long delete(@RequestParam(value = "id") Long id) {
        return boardService.delete(id);
    }

    /* 6. 파일 다운로드 */
    @GetMapping("/boardFile/download/{id}")
    public void downloadFile(@PathVariable(required = false) Long id, HttpServletResponse response) {
        FileResponseDto fileResponseDto = fileService.findById(id);
        String uploadDate = fileResponseDto.getInsertTime().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String uploadPath = Paths.get("C:","BimBoard","upload", uploadDate).toString();
        String fileName = fileResponseDto.getOriginName();
        File file = new File(uploadPath, fileResponseDto.getSaveName());
        try {
            byte[] data = FileUtils.readFileToByteArray(file);
            response.setContentType("application/octet-stream"); //파일유형설정 http나 email상에서 application이 지정되지 않았거나 형식을 모를 때 application/octet-stream을 사용한다.
            response.setContentLength(data.length); //파일길이설정
            response.setHeader("Content-Transfer-Encoding","binary"); //내용물 인코딩하는 방식설정
            response.setHeader("Content-Disposition","attachment; filename=\""+ URLEncoder.encode(fileName,"UTF-8")+"\";"); //데이터형식/성향 설정
            response.getOutputStream().write(data); //버퍼의 출력스트림을 출력
            response.getOutputStream().flush();     //버퍼에 남아있는 출력스트림을 출력
            response.getOutputStream().close();     //출력스트림을 닫는다. close함수는 내부적으로 flush함수를 호출하기 때문에 flush함수를 따로 호출하지 않아도 괜찮다.
        } catch (IOException e) {
            throw new RuntimeException("파일다운로드에 실패");
        }
    }

    /* 7. 검색조건에 따른 게시글 가져오기 */
    @ResponseBody
    @GetMapping("/boards/ByOption")
    public ModelAndView boardsByOption(@RequestParam(value = "page") int page,
                                       @RequestParam(value = "type") String type,
                                       @RequestParam(value = "keyword") String keyword) {
        ModelAndView mv = new ModelAndView("jsonView");
        var deleteYn = 'N';
        if(type.equals("title")) {
            Page<Board> list = boardService.searchTitle(keyword, deleteYn, page);
            int totalPage = list.getTotalPages();
            var content = list.getContent();
            mv.addObject("board", content);
            mv.addObject("totalPage", totalPage);
        }else if(type.equals("writer")) {
            Page<Board> list = boardService.searchWriter(keyword, deleteYn, page);
            int totalPage = list.getTotalPages();
            var content = list.getContent();
            mv.addObject("board",content);
            mv.addObject("totalPage",totalPage);
        }else if(type.equals("content")) {
            Page<Board> list = boardService.searchContent(keyword,deleteYn,page);
            int totalPage = list.getTotalPages();
            var content = list.getContent();
            mv.addObject("board",content);
            mv.addObject("totalPage",totalPage);
        }
        return mv;
    }
    /* 8. 검색조건에 따른 게시글 가져오기(page 당) */
    @ResponseBody
    @GetMapping("/boards/perPage")
    public ModelAndView searchPerPage(@RequestParam(value = "page") int page,
                                      @RequestParam(value = "type") String type,
                                      @RequestParam(value = "keyword") String keyword) {
        ModelAndView mv = new ModelAndView("jsonView");
        var deleteYn = 'N';
        if(type.equals("title")) {
            Page<Board> list = boardService.searchTitle(keyword, deleteYn, page);
            int totalPage = list.getTotalPages();
            var content = list.getContent();
            mv.addObject("board", content);
            mv.addObject("totalPage", totalPage);
        }else if(type.equals("writer")) {
            Page<Board> list = boardService.searchWriter(keyword, deleteYn, page);
            int totalPage = list.getTotalPages();
            var content = list.getContent();
            mv.addObject("board",content);
            mv.addObject("totalPage",totalPage);
        }else if(type.equals("content")) {
            Page<Board> list = boardService.searchContent(keyword,deleteYn,page);
            int totalPage = list.getTotalPages();
            var content = list.getContent();
            mv.addObject("board",content);
            mv.addObject("totalPage",totalPage);
        }
        return mv;
    }
}
