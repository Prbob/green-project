package com.brogrammers.brogrammers.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

    @GetMapping("/menu/social")
        public String socialMenu(Model model){
        // 헤더 home, style, shop중 style 글자 굵기를 위한
        model.addAttribute("style","style");
        return "menu/social";
    }
    @GetMapping("/menu/total")
    public String totalMenu(){
        return "menu/total";
    }
    @GetMapping("/menu/search")
    public String searchMenu(){
        return "menu/search";
    }


}
