package com.graphs.model;

import java.util.HashMap;

import com.graphs.model.interfaces.INode;

public class Node implements INode, Comparable<INode> {
	
	private int number;
	
	private boolean isEndNode;
	
	private HashMap<String, Object> properties;
	
	public Node(int number, boolean isEndNode) {
		this.number = number;
		this.isEndNode = isEndNode;
		properties = new HashMap<>();
	}

	public int getNumber() {
		return number;
	}
	
	public boolean isEndNode() {
		return isEndNode;
	}
	
	public void setEndNode(boolean isEndNode) {
		this.isEndNode = isEndNode;
	}
	
	public HashMap<String, Object> getProperties() {
		return properties;
	}

	public Object getProperty(String key) {
		return properties.get(key);
	}

	public void setProperty(String key, Object value) {
		properties.put(key, value);
	}

	public int compareTo(INode nd) {
		if (getNumber() < nd.getNumber()) return -1;
		if (getNumber() > nd.getNumber()) return 1;		
		return 0;
	}
	
	public String toString() {		
		return getNumber() + "";// + "(f = " + getProperty("f") + ", g = " + getProperty("g") + ", h = " + getProperty("h") + ")";
	}

}
