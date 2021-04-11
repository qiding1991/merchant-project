package com.kankan.merchant.module.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

@Data
public class MerchantApplyParam {

    @ApiModelProperty(value = "商家申请ID",notes = "取值是商家申请服务成功后返回记录中id",required = true)
    private String applyId;
    @ApiModelProperty(value = "商家申请状态",notes = "初始化为1未审核，已通过2，已拒绝3",required = true)
    private String newStatus;
    @ApiModelProperty(value = "商家申请成功后的权限列表",notes = "当且仅当newStatus是2已通过时为必传")
    private List<String> newApplyUserPrivilege;
}
