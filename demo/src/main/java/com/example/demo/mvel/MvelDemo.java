package com.example.demo.mvel;

import org.mvel2.MVEL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MvelDemo {

    public static void main(String[] args) {

        String expression = "{'21807','21825','22214','29249','199087337','199083023','198099803'} contains ORG_ALARM_CODE";

        List<String> childern = new ArrayList<>();
        childern.add("hello");

        Map<String, Object> factMap = new HashMap<>();
        factMap.put("ORG_ALARM_CODE", "21807");
        factMap.put("children", new ArrayList<String>());

        Map<String, Object> factMap2 = new HashMap<>();
        factMap2.put("ORG_ALARM_CODE", "21825");

        Map<String, Object> factMap3 = new HashMap<>();
        factMap3.put("ORG_ALARM_CODE", "22214");
        factMap3.put("children", childern);

        System.out.println(MVEL.executeExpression(MVEL.compileExpression(expression), null, factMap, Boolean.class));
        try {
            System.out.println(MVEL.executeExpression(MVEL.compileExpression(expression), null, factMap2, Boolean.class));
        }
        catch (Exception e) {
            System.out.println("error happened");
        }
        System.out.println(MVEL.executeExpression(MVEL.compileExpression(expression), null, factMap3, Boolean.class));

    }
}
