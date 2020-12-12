package com.example.demo.dfa;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DFATest {

    public static int dfaTest(String txt, int beginIndex, int matchType) {
        boolean flag = false;

        int matchFlag = 0;
        char word = 0;
        Map nowMap = BuildMap.dfaMap;
        for (int i = beginIndex; i < txt.length(); i++) {
            word = txt.charAt(i);
            nowMap = (Map) nowMap.get(word);
            if (nowMap != null) {
                matchFlag++;
                //判断是否是敏感词结尾，如果是结尾字段判断是否继续检测
                if ("1".equals(nowMap.get("isEnd"))) {
                    flag = true;
                    //判断过滤类型，如果是小过滤则跳出循环，否则继续循环
                    if (1 == matchType) {
                        break;
                    }
                }
            }
            else {
                break;
            }
        }
        if (!flag) {
            matchFlag = 0;
        }
        return  matchFlag;
    }

    public static void main(String[] args) {
        Set<String> keyWord = new HashSet<>();
        keyWord.add("加微信");
        keyWord.add("加QQ");
        keyWord.add("斗地主");
        BuildMap.buildDFAMap(keyWord);

        String txt = "老少爷们们加微，加Q，升级搞起来";
        System.out.println(dfaTest(txt, 0, 2));
    }

}
