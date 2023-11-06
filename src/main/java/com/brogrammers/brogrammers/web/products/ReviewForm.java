package com.brogrammers.brogrammers.web.products;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ReviewForm {
    @NotNull
    private String content;
    private String productName;
}
