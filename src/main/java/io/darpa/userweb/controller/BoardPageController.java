package io.darpa.userweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardPageController {
    /* 리스트 페이지 */
    @GetMapping("/list")
    public String BoardList() {
        return "board/list";
    }
    /* 작성 페이지 */
    @GetMapping("/write")
    public String BoardWrite() {
        return "board/write";
    }
    /* 상세 페이지 */
    @GetMapping("/view/{id}")
    public String goView(@PathVariable Long id, Model model) {
        model.addAttribute("id",id);
        return "board/detail";
    }
}
