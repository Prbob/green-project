package com.brogrammers.brogrammers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PrivacyFormTest {

    @GetMapping("/aaa")
    public String toss() {
        return "privacyFormTest";
    }

}
