package com.cw;

public enum EnumMultiNumbers {
    FIRST(12, "Jack", "ShangHai"),
    SECOND(13, "KiKi", "GuanZhou"),
    THIRD(14, "Peter", "ShangXi");

    private Integer intNum;
    private String stringName;
    private String stringHomeAddress;

    EnumMultiNumbers(int num, String name, String address) {
        intNum = num;
        stringName = name;
        stringHomeAddress = address;
    }

    public static EnumMultiNumbers getEnumbyNum(int num) {
        EnumMultiNumbers[] itemArray = EnumMultiNumbers.values();
        for (EnumMultiNumbers element:
             itemArray) {
            if (element.intNum == num) {
                return element;
            }
        }

        return null;
    }

    public static void main(String[] args) {
        EnumMultiNumbers enumMultiNumbers = FIRST;
        System.out.printf(enumMultiNumbers.intNum + ", " + enumMultiNumbers.stringName + ", " + enumMultiNumbers.stringHomeAddress + "\n");

        EnumMultiNumbers response = EnumMultiNumbers.getEnumbyNum(13);
        System.out.printf(response.intNum + ", " + response.stringName + ", " + response.stringHomeAddress + "\n");

    }
};