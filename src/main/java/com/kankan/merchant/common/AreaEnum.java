package com.kankan.merchant.common;

public enum AreaEnum {

    ONE("1","市中心"),
    TWO("2","士嘉堡"),
    THREE("3","北约克"),
    FOUR("4","东约克"),
    FIVE("5","怡陶碧谷"),
    SIX("6","万锦"),
    SEVEN("7","列治文山"),
    EIGHT("8","密西沙加"),
    NINE("9","旺市"),
    TEN("10","奥罗拉"),
    ELEVEN("11","纽马克特");

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getNameByCode (String code) {
        for (AreaEnum item : AreaEnum.values()) {
            if (item.getCode().equals(code)) {
                return item.getName();
            }
        }
        return null;
    }

    AreaEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
