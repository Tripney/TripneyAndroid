package com.msaranu.tripney.utilities;

/**
 * Created by Saranu on 4/21/17.
 */

public class StringUtil {

    public static String stripNewlinesExtraSpaces(String sourceString){
        return(sourceString.trim().
                replaceAll("(?m)(^ *| +(?= |$))", "").replaceAll("(?m)^$([\r\n]+?)(^$[\r\n]+?^)+", "$1"));
    }
}
