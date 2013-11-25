package com.graphs.model.interfaces;

import java.util.HashMap;

public interface INode {

	public int getNumber();
	
	public HashMap<String, Object> getProperties();
	
	public Object getProperty(String key);
	
	public void setProperty(String key, Object value);
	
	public boolean isEndNode();
	
	public void setEndNode(boolean isEndNode);
	
}
