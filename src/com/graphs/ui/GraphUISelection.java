package com.graphs.ui;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;

public class GraphUISelection {

	private HashSet<NodeUI> nodes;
	
	private Point originPoint;
	
	public GraphUISelection() {
		nodes = new HashSet<>();
		originPoint = new Point();
	}
	
	public void addNode(NodeUI n) {
		nodes.add(n);
	}
	
	public void removeNode(NodeUI n) {
		nodes.remove(n);
	}
	
	public ArrayList<NodeUI> getSelection() {
		return new ArrayList<>(nodes);
	}
	
	public void setSelection(ArrayList<NodeUI> nodes) {
		this.nodes.clear();
		this.nodes.addAll(nodes);
	}
	
	public void clearSelection() {
		nodes.clear();
	}
	
	public void setOrigin(Point origin) {
		this.originPoint = origin;
	}
	
	public Point getOrigin() {
		return this.originPoint;
	}
	
	public void moveTo(Point p) {
		int tx = p.x - originPoint.x;
		int ty = p.y - originPoint.y;
		translate(tx, ty);
	}
	
	public void translate(int tx, int ty) {
		for (NodeUI node : nodes) {
			node.translate(tx, ty);
		}
	}

}
