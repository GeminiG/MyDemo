package com.example.demo.spel;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SpelDemo {
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    static class RuleItem {
        /**
         * 左变量
         */
        private String left;

        /**
         * 比较表达式
         */
        private ComparelOpration comparelOpration;

        /**
         * 右变量或者常量
         */
        private String right;

        /**
         * 连接下一个表达式的逻辑运算符
         */
        private LogicalOpration logicalOpra;
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    static class RuleModel {
        /**
         * 规则列表
         */
        private List<RuleItem> ruleItems;

        /**
         * 左括号放在第几个Item之前
         */
        private List<Integer> leftParenthesesIndex;

        /**
         * 右括号放在第几个Item之后
         */
        private List<Integer> rightParenthesesIndex;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class SpelResult {
        private String express;
        private StandardEvaluationContext context;
    }

    enum ComparelOpration {
        In,
        NotIn,
        GreaterThan,
        LessThan,
        GreaterEqualThan,
        LessEqualThan,
        Equal,
        NotEqual;

        public static boolean isDecimalCompareLogicalOpration(ComparelOpration opration) {
            return opration.ordinal() == ComparelOpration.GreaterThan.ordinal()
                    || opration.ordinal() == ComparelOpration.GreaterEqualThan.ordinal()
                    || opration.ordinal() == ComparelOpration.LessEqualThan.ordinal()
                    || opration.ordinal() == ComparelOpration.LessThan.ordinal();
        }

        public static boolean isEqualLogicalOpration(ComparelOpration opration) {
            return opration.ordinal() == ComparelOpration.Equal.ordinal()
                    || opration.ordinal() == ComparelOpration.NotEqual.ordinal()
                    ;
        }
    }

    enum LogicalOpration {
        None,
        And,
        Or;

        static String toStr(LogicalOpration logicalOpration) {
            return logicalOpration.ordinal() == LogicalOpration.None.ordinal()
                    ? ""
                    : (logicalOpration.ordinal() == LogicalOpration.And.ordinal() ? "&&" : "||");
        }
    }

    static class SpelMatchFactory {
        private static final ExpressionParser parser = new SpelExpressionParser();

        static SpelResult toSpelExpress(RuleModel model, Map<String, String> userFeature) {
            List<RuleItem> ruleItemList = model.getRuleItems();
            StringBuilder sb = new StringBuilder();
            StandardEvaluationContext ctx = new StandardEvaluationContext();
            for (int i = 0; i < ruleItemList.size(); i++) {
                RuleItem item = ruleItemList.get(i);
                if (model.leftParenthesesIndex.contains(i)) {
                    sb.append("(");
                }

                String listKey = "list" + i;
                String valueKey = "item" + i;

                String subExpress = compute(item, listKey, valueKey);
                sb.append(subExpress);

                String leftValue = item.getLeft();
                if (leftValue.startsWith("{") && leftValue.endsWith("}")) {
                    leftValue = userFeature.get(leftValue.substring(1, leftValue.length() - 1));
                }

                String rightValue = item.getRight();
                if (rightValue.startsWith("{") && rightValue.endsWith("}")) {
                    rightValue = userFeature.get(rightValue.substring(1, rightValue.length() - 1));
                }

                if (ComparelOpration.isDecimalCompareLogicalOpration(item.comparelOpration)) {
                    ctx.setVariable(listKey, Integer.parseInt(rightValue));
                    ctx.setVariable(valueKey, Integer.parseInt(leftValue));
                } else if (ComparelOpration.isEqualLogicalOpration(item.comparelOpration)) {
                    ctx.setVariable(listKey, rightValue);
                    ctx.setVariable(valueKey, leftValue);
                } else {
                    ctx.setVariable(listKey, Arrays.asList(rightValue.split(",")));
                    ctx.setVariable(valueKey, leftValue);
                }

                if (model.rightParenthesesIndex.contains(i)) {
                    sb.append(")");
                }

                if (item.logicalOpra.ordinal() != LogicalOpration.None.ordinal()) {
                    sb.append(LogicalOpration.toStr(item.getLogicalOpra()));
                }
            }

            return new SpelResult(sb.toString(), ctx);
        }

        public static boolean compute(RuleModel model, Map<String, String> userFeature) {
            SpelResult spelExpressResult = SpelMatchFactory.toSpelExpress(model, userFeature);



            Boolean execResult = parser.parseExpression(spelExpressResult.getExpress()).getValue(
                    spelExpressResult.getContext(),
                    Boolean.class);
            return execResult;
        }

        private static String compute(RuleItem matchItem, String listKey, String valueKey) {
            if (matchItem.getComparelOpration().ordinal() == ComparelOpration.Equal.ordinal()) {
                return "#" + listKey + ".equals(#" + valueKey + ")";
            }

            if (matchItem.getComparelOpration().ordinal() == ComparelOpration.NotEqual.ordinal()) {
                return "!#" + listKey + ".equals(#" + valueKey + ")";
            }

            if (matchItem.getComparelOpration().ordinal() == ComparelOpration.In.ordinal()) {
                return "#" + listKey + ".contains(#" + valueKey + ")";
            }
            if (matchItem.getComparelOpration().ordinal() == ComparelOpration.NotIn.ordinal()) {
                return "!#" + listKey + ".contains(#" + valueKey + ")";
            }
            if (matchItem.getComparelOpration().ordinal() == ComparelOpration.GreaterEqualThan.ordinal()) {
                return "#" + valueKey + ">=" + "#" + listKey;
            }

            if (matchItem.getComparelOpration().ordinal() == ComparelOpration.LessEqualThan.ordinal()) {
                return "#" + valueKey + "<=" + "#" + listKey;
            }

            if (matchItem.getComparelOpration().ordinal() == ComparelOpration.GreaterThan.ordinal()) {
                return "#" + valueKey + ">" + "#" + listKey;
            }

            if (matchItem.getComparelOpration().ordinal() == ComparelOpration.LessThan.ordinal()) {
                return "#" + valueKey + "<" + "#" + listKey;
            }

            throw new IllegalArgumentException("不支持的逻辑运算类型");
        }
    }

    public static void main(String[] args) {
        List<RuleItem> ruleItems = new ArrayList<>();
        ruleItems.add(new RuleItem("{status}", ComparelOpration.In, "2,3", LogicalOpration.Or));
        ruleItems.add(new RuleItem("{level}", ComparelOpration.In, "1,2", LogicalOpration.And));
        ruleItems.add(new RuleItem("{hours}", ComparelOpration.GreaterEqualThan, "48", LogicalOpration.And));
        ruleItems.add(new RuleItem("{phone1}", ComparelOpration.Equal, "{phone2}", LogicalOpration.None));
        RuleModel model = new RuleModel();
        model.setRuleItems(ruleItems);

        //左括号在0的位置之前
        model.setLeftParenthesesIndex(Arrays.asList(0));
        //右括号在1的位置之后
        model.setRightParenthesesIndex(Arrays.asList(1));
        //以上表达式相当于 ({status} in '2,3' or {level} in '1,2') && {hours}>=48 && {phone1}=={phone2}

        //1. {phone1} != {phone2} ,结果为false
        Map<String, String> userFeature1 = ImmutableMap.of("status", "2", "level", "1", "phone1",
                "13900000000", "phone2", "13900000001", "hours", "66");
        boolean computeResult = SpelMatchFactory.compute(model, userFeature1);
        System.out.println("userFeature1的匹配结果:" + computeResult);


        //2.{hours} < 48 ,结果为false
        Map<String, String> userFeature2 = ImmutableMap.of("status", "2", "level", "1", "phone1",
                "13900000000", "phone2", "13900000001", "hours", "6");
        computeResult = SpelMatchFactory.compute(model, userFeature2);
        System.out.println("userFeature2的匹配结果:" + computeResult);


        //3. {status} 不在 2,3 中,但是 level 在 1,2中,结果为true
        Map<String, String> userFeature3 = ImmutableMap.of("status", "1", "level", "1", "phone1",
                "13900000000", "phone2", "13900000000", "hours", "66");
        computeResult = SpelMatchFactory.compute(model, userFeature3);
        System.out.println("userFeature3的匹配结果:" + computeResult);

        //4. {status} 不在 2,3 中,且 level 不在 1,2中,结果为false
        Map<String, String> userFeature4 = ImmutableMap.of("status", "1", "level", "3", "phone1",
                "13900000000", "phone2", "13900000000", "hours", "66");
        computeResult = SpelMatchFactory.compute(model, userFeature4);
        System.out.println("userFeature4的匹配结果:" + computeResult);

        //4.一切都匹配,返回true
        Map<String, String> userFeature5 = ImmutableMap.of("status", "2", "level", "1", "phone1",
                "13900000000", "phone2", "13900000000", "hours", "66");
        computeResult = SpelMatchFactory.compute(model, userFeature5);
        System.out.println("userFeature5的匹配结果:" + computeResult);
    }

}