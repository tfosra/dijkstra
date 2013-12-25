package com.graphs.model;

import java.util.HashMap;

import com.graphs.model.interfaces.ILink;
import com.graphs.model.interfaces.INode;

public class Link implements ILink {
	
	private INode startNode;
	
	private INode endNode;
	
	private HashMap<String, Object> properties;

	private boolean oriented;
	
	public Link(INode startNode, INode endNode) {
		this(startNode, endNode, 0L);
	}
	
	public Link(INode startNode, INode endNode, long k) {
		this(startNode, endNode, k, true);
	}
	
	public Link(INode startNode, INode endNode, long k, boolean oriented) {
		this.startNode = startNode;
		this.endNode = endNode;
		this.oriented = oriented;
		properties = new HashMap<>();
		properties.put("k", k + "");
	}
	
	public Link(INode startNode, INode endNode, boolean oriented, HashMap<String, Object> properties) {
		this.startNode = startNode;
		this.endNode = endNode;
		this.oriented = oriented;
		this.properties = properties;
		if (this.properties == null) this.properties = new HashMap<>();
	}

	public INode getStartNode() {
		return startNode;
	}

	public INode getEndNode() {
		return endNode;
	}
	
	@Override
	public boolean isOriented() {
		return oriented;
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
	
	public void setProperties(HashMap<String, Object> properties) {
		this.properties = properties;	
	}

}
