package com.commons.utils

/**
 * Created by zy on 17/7/12.
 */
class PageUtils {
    static int getOffset(Object offsetObj){
        if(offsetObj instanceof Number){
            return ((Number)offsetObj).intValue()
        }

        if(offsetObj != null){
            try{
                String string = offsetObj.toString()
                if(string!=null){
                    return Integer.parseInt(string)
                }
            } catch (NumberFormatException e){}
        }

        return 0
    }

    static int getMax(Object maxObj,int defaultVal,int maxVal){
        if(maxObj instanceof Number){
            return Math.min(((Number)maxObj).intValue(),maxVal)
        }

        if(maxObj != null){
            try{
                String string = maxObj.toString()
                if(string!=null){
                    return Math.min(Integer.parseInt(string),maxVal)
                }
            } catch (NumberFormatException e){}
        }

        return Math.min(defaultVal,maxVal)
    }
}
