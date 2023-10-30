package com.brogrammers.brogrammers.web.memberController;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class LoginForm {
    @NotEmpty(message = "이메일 기입은 필수 입니다.")
    private String memberEmail;
    @NotBlank
    private String password;
}
