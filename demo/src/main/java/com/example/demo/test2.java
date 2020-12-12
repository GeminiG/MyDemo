package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class test2  {

    public static void main(String[] args) {
        HashMap map = new HashMap();
        List<HashMap> list = new ArrayList<>();
        list.add(map);

        testMap(list);
    }

    public static void testMap(List<? extends Map> param) {
        System.out.println(param);
    }
}
