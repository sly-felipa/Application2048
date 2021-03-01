package com.example.application2048.utils;

public final class StringUtils {

    /**
     * Agrega ceros a la izquierda de los minutos o los segundos si en caso fuera necesario.
     * @param str
     * @param length
     * @param character
     * @return
     */
    public static String padLeft(String str, int length, char character){
        String newStr = str;
        while(newStr.length()!=length){
            newStr = character+newStr;
        }
        return newStr;
    }
}
