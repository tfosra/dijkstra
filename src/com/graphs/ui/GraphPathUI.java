package com.graphs.ui;

import java.util.LinkedList;

import com.graphs.model.GraphPath;
import com.graphs.model.interfaces.ILink;
import com.graphs.model.interfaces.INode;

public class GraphPathUI {

	private GraphPath path;
	private GraphUI graph;
	
	public GraphPathUI(GraphUI graph, GraphPath path) {
		this.graph = graph;
		this.path = path;
	}
	
	public LinkedList<NodeUI> getPathNodes() {
		LinkedList<NodeUI> res = new LinkedList<>();
		for (INode nd : path.getPathNodes()) {
			res.add(graph.getNode(nd.getNumber()));
		}
		return res;
	}
	
	public LinkedList<LinkUI> getPathLinks() {
		LinkedList<LinkUI> res = new LinkedList<>();
		for (ILink lnk : path.getPathLinks()) {
			res.add(graph.getLink(graph.getNode(lnk.getStartNode().getNumber()), graph.getNode(lnk.getEndNode().getNumber())));
		}
		return res;
	}
	
}
