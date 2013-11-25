package com.graphs.model.interfaces;

import java.util.HashMap;

public interface ILink {

	public INode getStartNode();
	
	public INode getEndNode();
	
	public HashMap<String, Object> getProperties();
	
	public Object getProperty(String key);
	
	public void setProperty(String key, Object value);
	
}
