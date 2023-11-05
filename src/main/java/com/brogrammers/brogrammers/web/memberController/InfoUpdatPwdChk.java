package com.brogrammers.brogrammers.web.memberController;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class InfoUpdatPwdChk {
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String pwd;
}
