package com.kankan.merchant.module.merchant;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Appraise {

    private String id;

    @ApiModelProperty(value = "用户ID", notes = "", required = true)
    private String userId;
    @ApiModelProperty(value = "用户名", notes = "", required = true)
    private String userName;
    @ApiModelProperty(value = "评价内容", notes = "", required = true)
    private String text;
    @ApiModelProperty(value = "点赞数", required = true)
    private Integer likeNum;
    @ApiModelProperty(value = "评价时间", notes = "", required = true)
    private String time;
}
