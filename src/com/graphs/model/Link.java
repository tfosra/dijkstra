package com.graphs.model;

import java.util.HashMap;

import com.graphs.model.interfaces.ILink;
import com.graphs.model.interfaces.INode;

public class Link implements ILink {
	
	private INode startNode;
	
	private INode endNode;
	
	private HashMap<String, Object> properties;
	
	public Link(INode startNode, INode endNode) {
		this.startNode = startNode;
		this.endNode = endNode;
		properties = new HashMap<>();
	}
	
	public Link(INode startNode, INode endNode, long k) {
		this.startNode = startNode;
		this.endNode = endNode;
		properties = new HashMap<>();
		properties.put("k", k + "");
	}

	public INode getStartNode() {
		return startNode;
	}

	public INode getEndNode() {
		return endNode;
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
	
	

}
