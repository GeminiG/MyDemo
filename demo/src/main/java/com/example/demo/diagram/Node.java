package com.example.demo.diagram;

import java.util.ArrayList;
import java.util.List;

/**
 * 无向图中的节点类
 */
public class Node {
    private String deviceId = null;
    private String deviceType = null;
    private String brokenList = null;
    public List<Node> relationNodes = new ArrayList<Node>();

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String name) {
        this.deviceId = name;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getBrokenList() {
        return brokenList;
    }

    public void setBrokenList(String bokenList) {
        this.brokenList = bokenList;
    }

    public List<Node> getRelationNodes() {
        return relationNodes;
    }

    public void setRelationNodes(List<Node> relationNodes) {
        this.relationNodes = relationNodes;
    }
}
