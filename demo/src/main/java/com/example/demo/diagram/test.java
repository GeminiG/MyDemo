package com.example.demo.diagram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;


public class test {

    /* 临时保存路径节点的栈 */
    public static Stack<Node> stack = new Stack<>();

    /* 存储路径的集合 */
    public static List<Object[]> sers = new ArrayList<>();

    public static boolean findPathFlag = true;

    public static void main(String[] args) {
        /* 定义节点关系 */
        List<Map<String, String>> nodeRelation = new ArrayList<>();
        Map<String, String> node1 = new HashMap<String, String>() {{
            put("DEVICE_ID", "0");
            put("NEAR_DEVICE_ID", "1");
            put("DEVICE_TYPE", "A");
        }};
        nodeRelation.add(node1);

        Map<String, String> node2 = new HashMap<String, String>() {{
            put("DEVICE_ID", "1");
            put("NEAR_DEVICE_ID", "0,2,4");
            put("DEVICE_TYPE", "A");
        }};
        nodeRelation.add(node2);

        Map<String, String> node3 = new HashMap<String, String>() {{
            put("DEVICE_ID", "2");
            put("NEAR_DEVICE_ID", "1,3");
            put("DEVICE_TYPE", "A");
            put("BROKEN_STATE", "A");
        }};
        nodeRelation.add(node3);

        Map<String, String> node4 = new HashMap<String, String>() {{
            put("DEVICE_ID", "3");
            put("NEAR_DEVICE_ID", "2");
            put("DEVICE_TYPE", "B");
        }};
        nodeRelation.add(node4);

        Map<String, String> node5 = new HashMap<String, String>() {{
            put("DEVICE_ID", "4");
            put("NEAR_DEVICE_ID", "1,5");
            put("DEVICE_TYPE", "A");
        }};
        nodeRelation.add(node5);

        Map<String, String> node6 = new HashMap<String, String>() {{
            put("DEVICE_ID", "5");
            put("NEAR_DEVICE_ID", "4,6");
            put("DEVICE_TYPE", "A");
            put("BROKEN_STATE", "A");
        }};
        nodeRelation.add(node6);

        Map<String, String> node7 = new HashMap<String, String>() {{
            put("DEVICE_ID", "6");
            put("NEAR_DEVICE_ID", "5");
            put("DEVICE_TYPE", "B");
        }};
        nodeRelation.add(node7);

        /* 定义节点数组 */
        Map<String, Node> nodeMap = new HashMap<>();

        for (int l = 0; l < nodeRelation.size(); l++) {
            Map<String, String> map = nodeRelation.get(l);
            Node node = new Node();
            node.setDeviceId(map.get("DEVICE_ID"));
            node.setDeviceType(map.get("DEVICE_TYPE"));
            node.setBrokenList(map.get("BROKEN_STATE"));
            nodeMap.put(map.get("DEVICE_ID"), node);
        }

        /* 定义与节点相关联的节点集合 */
        for (int j = 0; j < nodeRelation.size(); j++) {

            List<Node> nodeList = new ArrayList<>();
            String[] deviceIds = nodeRelation.get(j).get("NEAR_DEVICE_ID").split(",");
            for (int i = 0; i < deviceIds.length; i++) {
                nodeList.add(nodeMap.get(deviceIds[i]));
            }
            nodeMap.get(nodeRelation.get(j).get("DEVICE_ID")).setRelationNodes(nodeList);
        }

        /* 开始搜索所有路径 */
        String start = "1";
        if (!getPaths(nodeMap.get(start), null, nodeMap.get(start))) {
            System.out.println("没有通路");
        }


    }

    /* 判断节点是否在栈中 */
    public static boolean isNodeInStack(Node node)
    {
        Iterator<Node> it = stack.iterator();
        while (it.hasNext()) {
            Node node1 = (Node) it.next();
            if (node == node1)
                return true;
        }
        return false;
    }

    /* 此时栈中的节点组成一条所求路径，转储并打印输出 */
    public static void showAndSavePath()
    {
        Object[] o = stack.toArray();
        for (int i = 0; i < o.length; i++) {
            Node nNode = (Node) o[i];

            if(i < (o.length - 1))
                System.out.print(nNode.getDeviceId() + "->");
            else
                System.out.print(nNode.getDeviceId());
        }
        sers.add(o); /* 转储 */
        System.out.println("\n");
    }

    /*
     * 寻找路径的方法
     * cNode: 当前的起始节点currentNode
     * pNode: 当前起始节点的上一节点previousNode
     * sNode: 最初的起始节点startNode
     * eNode: 终点endNode
     */
    public static boolean getPaths(Node cNode, Node pNode, Node sNode) {
        Node nNode = null;
        /* 如果符合条件判断说明出现环路，不能再顺着该路径继续寻路，返回false */
        if (cNode != null && pNode != null && cNode == pNode)
            return false;

        if (cNode != null) {
            int i = 0;
            /* 起始节点入栈 */
            stack.push(cNode);
            /* 如果该起始节点就是终点，说明找到一条路径 */
            if ("B".equals(cNode.getDeviceType()))
            {
                /* 转储并打印输出该路径，返回true */
                showAndSavePath();
                return true;
            }
            /* 如果不是,继续寻路 */
            else
            {
                /*
                 * 从与当前起始节点cNode有连接关系的节点集中按顺序遍历得到一个节点
                 * 作为下一次递归寻路时的起始节点
                 */
                nNode = cNode.getRelationNodes().get(i);
                while (nNode != null) {
                    /*
                     * 如果nNode是最初的起始节点或者nNode就是cNode的上一节点或者nNode已经在栈中 ，
                     * 说明产生环路 ，应重新在与当前起始节点有连接关系的节点集中寻找nNode
                     */
                    if ("A".equals(nNode.getBrokenList()) || (pNode != null
                            && (nNode == sNode || nNode == pNode || isNodeInStack(nNode)))) {
                        i++;
                        if (i >= cNode.getRelationNodes().size())
                            nNode = null;
                        else
                            nNode = cNode.getRelationNodes().get(i);
                        continue;
                    }
                    /* 以nNode为新的起始节点，当前起始节点cNode为上一节点，递归调用寻路方法 */
                    if (getPaths(nNode, cNode, sNode))/* 递归调用 */
                    {
                        /* 如果找到一条路径，则弹出栈顶节点 */
                        stack.pop();
                        return true;
                    }
                    /* 继续在与cNode有连接关系的节点集中测试nNode */
                    i++;
                    if (i >= cNode.getRelationNodes().size())
                        nNode = null;
                    else
                        nNode = cNode.getRelationNodes().get(i);
                }
                /*
                 * 当遍历完所有与cNode有连接关系的节点后，
                 * 说明在以cNode为起始节点到终点的路径已经全部找到
                 */
                stack.pop();
                return false;
            }
        } else
            return false;
    }
}