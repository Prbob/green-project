package com.brogrammers.brogrammers.web.memberController;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class MemberForm {
    private Long id;
    @NotEmpty(message = "이메일 기입은 필수 입니다.")
    private String email;
    @NotEmpty(message = "회원 이름은 필수 입니다.")
    private String name;
    @NotEmpty(message = "비밀번호 입력은 필수 입니다.")
    private String pwd;
    @NotEmpty(message = "비밀번호 확인 입력란은 필수 입니다.")
    private String pwdChk;
    private String postal_code;
    private String middle_address;
    private String detailed_address;
    private String phone;
    private String grade;
    private Long totalOrderPrice;
    @Builder
    public MemberForm(Long id, String email, String name, String pwd, String pwdChk, String postal_code, String middle_address, String detailed_address,String phone,String grade,Long totalOrderPrice) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.pwd = pwd;
        this.pwdChk = pwdChk;
        this.postal_code = postal_code;
        this.middle_address = middle_address;
        this.detailed_address = detailed_address;
        this.phone = phone;
        this.grade = grade;
        this.totalOrderPrice = totalOrderPrice;
    }
}
