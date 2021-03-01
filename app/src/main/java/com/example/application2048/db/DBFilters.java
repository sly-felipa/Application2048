package com.example.application2048.db;

public final class DBFilters {
    public static final int LessThan = 0;
    public static final int GreaterThan=1;
    public static final int Equals=2;

    /**
     * Indica el filtro de búsqueda que se usará.
     * @param filter
     * @return
     */
    public static String filterToString(int filter){
        String result = "";
        switch(filter){
            case LessThan:
                result = "<";
                break;
            case GreaterThan:
                result = ">";
                break;
            case Equals:
                result = "=";
                break;
            default:
                break;
        }
        return result;
    }
}
