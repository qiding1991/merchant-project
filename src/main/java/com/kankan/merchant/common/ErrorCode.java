package com.kankan.merchant.common;

public enum ErrorCode {
  SUCCESS(0, "success"),
  SMS_CODE_ERROR(10000, "activeCode错误"),
  PASSWORD_USE_NAME_ERROR(10001, "用户名或者密码错误"),
  EMAIL_NOT_AVAILABLE_ERROR(10002, "邮箱不可用"),
  PARAM_CHECK_ERROR(1003, "参数校验不通过"),
  VERIFY_TOKEN_CHECK_ERROR(1004, "verifyToken错误"),
  USER_TOKEN_CHECK_ERROR(1005, "userToken错误"),
  //申请信息
  USER_APPLY_REPEATED(2001, "用户申请信息重复"),
  USER_APPLY_FORBIDDEN(2002, "审核已经通过，不需要重新申请"),
  USER_APPLY_REPEATED_COMPANY(2003, "已经申请公司看看号"),
  USER_APPLY_REPEATED_PERSON(2004, "已经申请个人看看号"),
  UN_KNOW_ERROR(-1, "服务器未知异常");


  private int code;
  private String message;

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  ErrorCode(int code, String message) {
    this.code = code;
    this.message = message;
  }
}
