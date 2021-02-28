package com.example.application2048.utils;

public final class StringUtils {
    public static String padLeft(String str, int length, char character){
        String newStr = str;
        while(newStr.length()!=length){
            newStr = character+newStr;
        }
        return newStr;
    }
}
