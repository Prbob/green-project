package com.brogrammers.brogrammers.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

    @GetMapping("/menu/social")
    public String socialMenu(){ return "/menu/social";
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
