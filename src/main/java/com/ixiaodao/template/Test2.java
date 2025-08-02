package com.ixiaodao.template;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONWriter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 1
 * @author jinwenbiao
 * @since 2025/6/14 16:38
 */
public class Test2 {
    public static void main(String[] args) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("btStart", "19,468");
        map.put("btEnd", "80,590");
        map.put("sdStart", "0,0");
        map.put("sdEnd", "1920,1080");
        map.put("wDelay", "");
        map.put("sDelay", "");
        System.out.println(JSON.toJSONString(map));
    }
}
