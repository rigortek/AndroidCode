package com.cw;

public enum EnumMultiNumbers {
    FIRST(12, "Jack", "ShangHai"),
    SECOND(13, "KiKi", "GuanZhou"),
    THIRD(11, "Peter", "ShangXi");

    private Integer intNum;
    private String stringName;
    private String stringHomeAddress;

    EnumMultiNumbers(int num, String name, String address) {
        intNum = num;
        stringName = name;
        stringHomeAddress = address;
    }
};