package yx.pay.common.utils;

import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public class ListUtil {

    public static boolean isBlank(List<?> list){
        if(list == null || list.isEmpty()){
            return true;
        }
        return false;
    }

    public static boolean isNotBlank(List<?> list){
        return !isBlank(list);
    }
}
