package com.graphs.model;

import java.util.HashSet;
import java.util.LinkedList;

import com.graphs.model.interfaces.ILink;
import com.graphs.model.interfaces.INode;

public class GraphPath {

	private LinkedList<ILink> links;
	
	private long length;
	
	public GraphPath(LinkedList<ILink> links) {
		this.links = links;
		this.length = pathLength();
	}
	
	/**
	 * Computes the total length of the path
	 * @return the length or -1 if the path is not correct
	 */
	public long pathLength() {
		long res = 0;
		for (ILink lnk : links) {
			Long lng = (Long) lnk.getProperty("k");
			if (lng == null) return -1;
			res += lng;
		}
		return res;
	}
	
	public LinkedList<INode> getPathNodes() {
		HashSet<INode> res = new HashSet<>();
		for (ILink lnk : links) {
			res.add(lnk.getStartNode());
			res.add(lnk.getEndNode());
		}
		return new LinkedList<>(res) ;
	}
	
	public LinkedList<ILink> getPathLinks() {
		return links;
	}
	
	public String toString() {
		String str = "Path from " + links.getFirst().getStartNode() + " to " + links.getLast().getEndNode() + "\n"; 
		str += "GraphPath : ";
		str += links.getFirst().getStartNode();
		for (ILink lnk : links) str += " - " + lnk.getEndNode();
		str += "\nLength : " + length;
		str += "\n";
		return str;
	}
	
}
