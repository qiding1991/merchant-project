package com.kankan.merchant.common;

public enum AreaEnum {

    ONE("1","市中心","DowntownToronto"),
    TWO("2","士嘉堡","Scarborough"),
    THREE("3","北约克","NorthYork"),
    FOUR("4","东约克","EastYork"),
    FIVE("5","怡陶碧谷","Etobicoke"),
    SIX("6","万锦","Markham"),
    SEVEN("7","列治文山","RichmondHill"),
    EIGHT("8","密西沙加","Mississauga"),
    NINE("9","旺市","Vaughan"),
    TEN("10","奥罗拉","Aurora"),
    ELEVEN("11","纽马克特","Newmarket");

    private String code;
    private String name;
    private String alis;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getAlis() {
        return alis;
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
    AreaEnum(String code, String name,String alis) {
        this.code = code;
        this.name = name;
        this.alis = alis;
    }
}
