package com.kankan.merchant.module.param;

import io.swagger.annotations.ApiModelProperty;

public class CollectLikeParam {

    @ApiModelProperty(value = "内容ID", notes = "", required = true)
    private String targetId;

    @ApiModelProperty(value = "操作类型1取消2加入", notes = "", required = true)
    private Integer type;

    @ApiModelProperty(value = "用户ID", notes = "", required = true)
    private Integer userId;

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTargetId() {
        return targetId;
    }
    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }
}
