package com.graphs.model;

import java.util.LinkedList;

import com.graphs.model.interfaces.IGraph;
import com.graphs.model.interfaces.ILink;
import com.graphs.model.interfaces.INode;

public class GraphPath {

//	INode[] nodes;
	LinkedList<INode> nodes;
	
	private long length;

	private IGraph graph;
	
	public GraphPath(IGraph graph, LinkedList<INode> nodes) {
		this.graph = graph;
		this.nodes = nodes;
		this.length = pathLength();
	}
	
	/**
	 * Computes the total length of the path
	 * @return the length or -1 if the path is not correct
	 */
	public long pathLength() {
		long res = 0;
		INode n1 = nodes.getFirst(), n2;
		for (int i = 1; i < nodes.size(); i++) {
			n2 = nodes.get(i);
			Long lng = (Long) graph.getLink(n1, n2).getProperty("k");
			if (lng == null) return -1;
			res += lng;
			n1 = n2;
		}
		return res;
	}
	
	public LinkedList<INode> getPathNodes() {
		return this.nodes;
	}
	
	public LinkedList<ILink> getPathLinks() {
		LinkedList<ILink> res = new LinkedList<>();
		INode n1 = nodes.getFirst(), n2;
		for (int i = 1; i < nodes.size(); i++) {
			n2 = nodes.get(i);
			ILink lnk = graph.getLink(n1, n2);
			if (lnk == null) return null;
			res.add(lnk);
			n1 = n2;
		}
		return res;
	}
	
	public String toString() {
		String str = "Shortest path from " + nodes.getFirst() + " to " + nodes.getLast() + "\n"; 
		str += "GraphPath : ";
		str += nodes.getFirst();
		for (INode nd : nodes) str += " - " + nd;
		str += "\nLength : " + length;
		str += "\n";
		return str;
	}
	
}
