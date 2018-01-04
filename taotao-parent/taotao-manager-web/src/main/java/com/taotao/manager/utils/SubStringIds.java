package com.taotao.manager.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 截取商品IDs
 */
public class SubStringIds {
    /**
     * 将商品ID切割，并封装入List
     * @param ids 商品ID（通过逗号隔开）
     * @return list
     */
    public static List<Long> getIdList(String ids){
        int beginIndex = 0;
        int endIndex = ids.length();
        List<Long> idList = new ArrayList<>();

        while (true){
            //如果字符串没有逗号了，那么剩余部分全部都是ID
            if(ids.indexOf(",", beginIndex) == -1 ){
                String str = ids.substring(beginIndex,ids.length());
                idList.add(Long.parseLong(str));
                break;
            }
            endIndex = ids.indexOf(",",beginIndex);
            //截取从beginIndex到endIndex的字符串
            String str = ids.substring(beginIndex,endIndex);
            idList.add(Long.parseLong(str));
            beginIndex = endIndex+1;
        }

        return idList;
    }
}
