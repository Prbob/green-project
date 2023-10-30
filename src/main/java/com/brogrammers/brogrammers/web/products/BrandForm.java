package com.brogrammers.brogrammers.web.products;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class BrandForm {
    private Long id;
    @NotEmpty(message = "기입은 필수입니다.")
    private String name;

    @Builder
    public BrandForm(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
