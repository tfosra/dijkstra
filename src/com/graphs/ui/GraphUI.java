package com.graphs.ui;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

import com.graphs.model.Graph;
import com.graphs.model.GraphPath;
import com.graphs.model.interfaces.IGraph;
import com.graphs.model.interfaces.ILink;
import com.graphs.model.interfaces.INode;
import com.graphs.model.solver.GraphSolver;
import com.graphs.model.solver.GraphSolver.SolverMethod;
import com.graphs.ui.FormUI.Etats;

public class GraphUI implements Cloneable {

	/**
	 * Devra contenir les nodes du graphe où l'entier représente le numéro du noeud (identifiant) dans le graphe
	 */
	private HashMap<Integer, NodeUI> nodes;

	private HashMap<String, LinkUI> linksMap;
	
	private IGraph graph;
	
	public GraphUI(boolean isOriented) {
		nodes = new HashMap<>();
		linksMap = new HashMap<>();
		graph = new Graph(isOriented);
	}
	
	public NodeUI getNode(int number) {
		return nodes.get(number);
	}
	
	public ArrayList<NodeUI> getNodes() {
		return new ArrayList<NodeUI>(nodes.values());
	}
	
	/**
	 * Returns all the next elements of a given node
	 * @param node The node for which we want the next elements
	 * @return The array of all the childs of the node
	 */
	public ArrayList<NodeUI> getNextElements(NodeUI node) {
		INode[] nextElements = graph.getNextElements(node.getNode());
		ArrayList<NodeUI> nd = new ArrayList<>();
		for (int i = 0; i < nextElements.length; i++) {
			nd.add(getNode(i));
		}
		return nd;
	}

	/**
	 * Returns all the previous elements of a given node
	 * @param node The node for which we want the previous elements
	 * @return The array of all the parents of the node
	 */
	public ArrayList<NodeUI> getPreviousElements(NodeUI node) {
		INode[] nextElements = graph.getPreviousElements(node.getNode());
		ArrayList<NodeUI> nd = new ArrayList<>();
		for (int i = 0; i < nextElements.length; i++) {
			nd.add(getNode(i));
		}
		return nd;
	}

	/**
	 * Checks whether two nodes are linked together.</br>
	 * Note that the type of links here are oriented links
	 * @param n1 The start node
	 * @param n2 The end node
	 * @return true if n1 is the parent of n2, false if not
	 */
	public boolean areLinked(NodeUI n1, NodeUI n2) {
		return graph.areLinked(n1.getNode(), n2.getNode());
	}

	/**
	 * Adds a node in the current graph
	 * @param node The node to be added in the graph
	 */
	public void addNode(NodeUI node) {
		graph.addNode(node.getNode());
		nodes.put(node.getNode().getNumber(), node);
	}

	/**
	 * Removes a node from the current graph
	 * @param node The node to be removed from the graph
	 */
	public void removeNode(NodeUI node) {
		int n = node.getNode().getNumber();
		nodes.remove(n);
		ArrayList<ILink> relatedLinks = graph.getRelatedLinks(node.getNode());
		for (ILink lnk : relatedLinks) {
			linksMap.remove(lnk.getStartNode().getNumber() + "#" + lnk.getEndNode().getNumber());
			linksMap.remove(lnk.getEndNode().getNumber() + "#" + lnk.getStartNode().getNumber());
		}
		graph.removeNode(node.getNode());
	}
	
	public ArrayList<LinkUI> getRelatedLinks(NodeUI node) {
		ArrayList<LinkUI> res = new ArrayList<>();
		ArrayList<ILink> relatedLinks = graph.getRelatedLinks(node.getNode());
		for (ILink lnk : relatedLinks) {
			res.add(getLink(getNode(lnk.getStartNode().getNumber()), getNode(lnk.getEndNode().getNumber())));
		}
		return res;
	}
	
	/**
	 * Create a new link in the graph
	 * @param noeud1 The start node
	 * @param noeud2 The end node
	 */
	public void addLink(NodeUI noeud1, NodeUI noeud2) {
		addLink(noeud1, noeud2, null);
	}
	
	public void addLink(NodeUI noeud1, NodeUI noeud2, HashMap<String, Object> properties) {
		int n1 = noeud1.getNode().getNumber();
		int n2 = noeud2.getNode().getNumber();
		if (!nodes.containsKey(n1)) addNode(noeud1);
		if (!nodes.containsKey(n2)) addNode(noeud2);
		graph.addLink(noeud1.getNode(), noeud2.getNode(), properties);
		LinkUI lnk = new LinkUI(noeud1, noeud2, graph.getLink(noeud1.getNode(), noeud2.getNode()));
		if (properties == null) lnk.setState(Etats.EDITING);
		linksMap.put(n1 + "#" + n2, lnk);
		if (!isOriented()) {
			linksMap.put(n2 + "#" + n1, lnk);
		}
	}

	/**
	 * Retrieves a given link from the graph
	 * @param n1 The start node
	 * @param n2 The end node
	 * @return The link between n1 and n2 or null if it doesn't exist
	 */
	public LinkUI getLink(NodeUI n1, NodeUI n2) {
		return linksMap.get(n1.getNode().getNumber() + "#" + n2.getNode().getNumber());
	}

	/**
	 * Removes a given link from the graph
	 */
	public void removeLink(LinkUI lnk) {
		int n1 = lnk.getOriginNode().getNode().getNumber();
		int n2 = lnk.getEndNode().getNode().getNumber();
		graph.removeLink(lnk.getLink());
		linksMap.remove(n1 + "#" + n2);
		if (!isOriented()) {
			linksMap.remove(n2 + "#" + n1);
		}
	}
	
	public void removeForm(FormUI form) {
		if (form instanceof NodeUI) removeNode((NodeUI)form);
		if (form instanceof LinkUI) removeLink((LinkUI)form);
	}

	/**
	 * @return The size of the graph (The number of nodes)
	 */
	public int getSize() {
		return graph.getSize();
	}
	
	public void renderGraph(Graphics g) {
		for (LinkUI lnk : linksMap.values()) lnk.renderForm(g);
		for (NodeUI nd : nodes.values()) nd.renderForm(g);
	}
	
	public FormUI getPointedForm(Point p) {
		for (NodeUI nd : nodes.values()) {
			if (nd.isOverForm(p))
				return nd;
		}
		for (LinkUI lnk : linksMap.values()) {
			if (lnk.isOverForm(p))
				return lnk;
		}
		return null;
	}
	
	public ArrayList<FormUI> getSelectedForms(Rectangle rect) {
		ArrayList<FormUI> res = new ArrayList<>();
		for (NodeUI nd : nodes.values()) {
			if (rect.contains(nd.getRectange())) res.add(nd);
		}
		for (LinkUI lnk : linksMap.values()) {
			if (rect.contains(lnk.getRectange())) res.add(lnk);
		}
		return res;
	}
	
	public void drawPath(GraphPathUI pathUI) {
		for (FormUI f : nodes.values()) f.setState(Etats.NORMAL);
		for (FormUI f : linksMap.values()) f.setState(Etats.GRAYED);
		for (FormUI f : pathUI.getPathNodes()) f.setState(Etats.SELECTED);
		for (FormUI f : pathUI.getPathLinks()) f.setState(Etats.SELECTED);
	}
	
	public long solvePath(SolverMethod method, Object...params) {
		GraphPath path = GraphSolver.solvePath(graph, method, params);
		drawPath(new GraphPathUI(this, path));
		return path.pathLength();
	}
	
	public void clear() {
		linksMap.clear();
		nodes.clear();
		graph.clear();
	}
	
	public boolean isOriented() {
		return graph.isOriented();
	}
	
	public void setDisplayNodeLabels(boolean display) {
		for (FormUI f : nodes.values()) f.setDisplayLabel(display);
	}
	
	public void setDisplayLinkLabels(boolean display) {
		for (FormUI f : linksMap.values()) f.setDisplayLabel(display);
	}
	
}
