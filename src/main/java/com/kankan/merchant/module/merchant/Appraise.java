package com.kankan.merchant.module.merchant;

import lombok.Data;

@Data
public class Appraise {

    private String id;
    private Integer userId;
    private String userName;
    private String text;
    private Integer likeNum;
    private Long time;
}
