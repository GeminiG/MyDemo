package com.example.demo.dfa;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BuildMap {
    public static Map<String, String> dfaMap = new HashMap<>();

    public static void buildDFAMap(Set<String> keyWordSet) {
        String key = null;
        Map nowMap = null;
        Map newWordMap = null;
        Iterator<String> iterator = keyWordSet.iterator();
        while(iterator.hasNext()) {
            key = iterator.next();
            nowMap = dfaMap;
            for (int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i);
                Map wordMap = (Map) nowMap.get(keyChar);
                if (wordMap != null) {
                    nowMap = wordMap;
                }
                else {
                    newWordMap = new HashMap<String, String>();
                    newWordMap.put("isEnd", "0");
                    nowMap.put(keyChar, newWordMap);
                    nowMap = newWordMap;
                }

                if (i == key.length() -1) {
                    nowMap.put("isEnd", "1");
                }
                System.out.println("封装dfaMap过程： " + dfaMap);
            }

            System.out.println("查看dfa Map数据： " + dfaMap);
        }
    }
}
