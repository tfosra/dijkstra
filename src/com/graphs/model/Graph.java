package com.graphs.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.graphs.model.interfaces.IGraph;
import com.graphs.model.interfaces.ILink;
import com.graphs.model.interfaces.INode;

public class Graph implements IGraph {

	/**
	 * Devra contenir les nodes du graphe où l'entier représente le numéro du noeud dans le graphe
	 */
	private HashMap<Integer, INode> nodes;
	
	/**
	 * Devra contenir tous les suivants d'un noeud particulier, l'entier représente 
	 * le numéro du noeud parent et la liste représente ses fils
	 */
	private HashMap<Integer, HashSet<Integer>> nextsMap;
	
	/**
	 * Pareil que le nextsMap, la liste représente tous les précédents d'un noeud
	 */
	private HashMap<Integer, HashSet<Integer>> previousMap;
	
	private HashMap<String, ILink> linksMap;
	
	private boolean isOriented;
	
	public Graph(boolean isOriented) {
		this.isOriented = isOriented;
		nodes = new HashMap<>();
		nextsMap = new HashMap<>();
		previousMap = new HashMap<>();
		linksMap = new HashMap<>();
	}
	
	/**
	 * Returns all the nodes contained in the graph
	 */
	public INode[] getNodes() {
		INode[] nd = new INode[nodes.size()];
		nodes.values().toArray(nd);
		Arrays.sort(nd);
		return nd;
	}
	
	/**
	 * Returns all the links contained in the graph
	 */
	public ArrayList<ILink> getLinks() {
		HashSet<ILink> hs = new HashSet<>();
		for (ILink lnk : linksMap.values()) {
			hs.add(lnk);
		}
		return new ArrayList<>(hs);
	}

	/**
 	* Retrieves a specific node from the graph
 	* @param The number of the node to retrieve
 	* @return The nbr'th node or null if it doesn't exist 
 	*/
	public INode getNode(int nbr) {
		return nodes.get(nbr);
	}
	
	/**
	 * Returns all the next elements of a given node
	 * @param node The node for which we want the next elements
	 * @return The array of all the childs of the node
	 */
	public INode[] getNextElements(INode node) {
		int n = node.getNumber();
		if (!nextsMap.containsKey(n)) return null;
		INode[] nd = new Node[nextsMap.get(n).size()];
		Integer[] tmp = nextsMap.get(n).toArray(new Integer[nd.length]);
		for (int i = 0; i < nd.length; i++) {
			nd[i] = getNode(tmp[i]);
		}
		return nd;		
	}

	/**
	 * Returns all the previous elements of a given node
	 * @param node The node for which we want the previous elements
	 * @return The array of all the parents of the node
	 */
	public INode[] getPreviousElements(INode node) {
		int n = node.getNumber();
		if (!previousMap.containsKey(n)) return null;
		INode[] nd = new Node[previousMap.get(n).size()];
		Integer[] tmp = previousMap.get(n).toArray(new Integer[nd.length]);
		for (int i = 0; i < nd.length; i++) {
			nd[i] = getNode(tmp[i]);
		}
		return nd;
	}

	/**
	 * Checks whether two nodes are linked together.</br>
	 * Note that the type of links here can be oriented or not
	 * @param n1 The start node
	 * @param n2 The end node
	 * @return true if n1 is the parent of n2 in case of an oriented link or n1 and n2 are connected, false if not
	 */
	public boolean areLinked(INode n1, INode n2) {
		return 
			   linksMap.containsKey(n1.getNumber() + "#" + n2.getNumber())
				||
			   linksMap.containsKey(n2.getNumber() + "#" + n1.getNumber());
	}

	/**
	 * Adds a node in the current graph
	 * @param node The node to be added in the graph
	 */
	public void addNode(INode node) {
		nodes.put(node.getNumber(), node);
	}

	/**
	 * Removes a node from the current graph
	 * @param node The node to be removed from the graph
	 */
	public void removeNode(INode node) {
		int n = node.getNumber();
		nodes.remove(n);
		HashSet<Integer> set;
		if (nextsMap.containsKey(n)) {
			int nd;
			for (Iterator<Integer> it = nextsMap.get(n).iterator(); it.hasNext();) {
				nd = it.next();
				linksMap.remove(n + "#" + nd);
				set = previousMap.get(nd);
				if (set != null) set.remove(n);
			}
			nextsMap.remove(n);
		}
		if (previousMap.containsKey(n)) {
			int nd;
			for (Iterator<Integer> it = previousMap.get(n).iterator(); it.hasNext();) {
				nd = it.next();
				linksMap.remove(nd + "#" + n);
				set = nextsMap.get(nd);
				if (set != null) set.remove(n);
			}
			previousMap.remove(n);
		}
	}
	
	/**
	 * Create a new link in the graph
	 * @param noeud1 The start node
	 * @param noeud2 The end node
	 */
	public void addLink(INode noeud1, INode noeud2) {		
		addLink(noeud1, noeud2, null);
	}
	
	public void addLink(INode noeud1, INode noeud2, HashMap<String, Object> properties) {
		int n1 = noeud1.getNumber();
		int n2 = noeud2.getNumber();
		if (!nodes.containsKey(n1)) addNode(noeud1);;
		if (!nodes.containsKey(n2)) addNode(noeud2);;
		ILink lnk = new Link(noeud1, noeud2, isOriented, properties);
		linksMap.put(n1 + "#" + n2, lnk);
		if (!isOriented) linksMap.put(n2 + "#" + n1, lnk);
		HashSet<Integer> set;
		if ((set = nextsMap.get(n1)) == null) set = new HashSet<>();
		set.add(n2);
		nextsMap.put(n1, set);
		if ((set = previousMap.get(n2)) == null) set = new HashSet<>();
		set.add(n1);
		previousMap.put(n2, set);
		if (!isOriented) {
			if ((set = nextsMap.get(n2)) == null) set = new HashSet<>();
			set.add(n1);
			nextsMap.put(n2, set);
			if ((set = previousMap.get(n1)) == null) set = new HashSet<>();
			set.add(n2);
			previousMap.put(n1, set);
		}
	}

	/**
	 * Retrieves a given link from the graph
	 * @param n1 The start node
	 * @param n2 The end node
	 * @return The link between n1 and n2 or null if it doesn't exist
	 */
	public ILink getLink(INode n1, INode n2) {
		return linksMap.get(n1.getNumber() + "#" + n2.getNumber());
	}

	/**
	 * Removes a given link from the graph
	 */
	public void removeLink(ILink lnk) {
		int n1 = lnk.getStartNode().getNumber();
		int n2 = lnk.getEndNode().getNumber();
		if (linksMap.containsKey(n1 + "#" + n2)) {
			linksMap.remove(n1 + "#" + n2);
			nextsMap.get(n1).remove(n2);
			previousMap.get(n2).remove(n1);
		}
		if (!isOriented && linksMap.containsKey(n2 + "#" + n1)) {
			linksMap.remove(n2 + "#" + n1);
			nextsMap.get(n2).remove(n1);
			previousMap.get(n1).remove(n2);
		}
	}
	
	public void addLink(ILink lnk) {
		int n1 = lnk.getStartNode().getNumber();
		int n2 = lnk.getEndNode().getNumber();
		linksMap.put(n1 + "#" + n2, lnk);
		HashSet<Integer> set;
		if ((set = nextsMap.get(n1)) == null) set = new HashSet<>();
		set.add(n2);
		nextsMap.put(n1, set);
		if ((set = previousMap.get(n2)) == null) set = new HashSet<>();
		set.add(n1);
		previousMap.put(n2, set);
		if (!isOriented) {
			if ((set = nextsMap.get(n2)) == null) set = new HashSet<>();
			set.add(n1);
			nextsMap.put(n2, set);
			if ((set = previousMap.get(n1)) == null) set = new HashSet<>();
			set.add(n2);
			previousMap.put(n1, set);
		}
	}

	/**
	 * @return The size of the graph (The number of nodes)
	 */
	public int getSize() {
		return nodes.size();
	}
	
	public String toString() {
		String str = "     ";
		Long k;
		for (int i = 0; i < getSize(); i++) {
			if (i < 10) str += " " + i + "  ";
			else str += i + "  ";
		}
		str += "\n\n";
		for (int i = 0; i < getSize(); i++) {
			if (i < 10) str += " " + i + "   ";
			else str += i + "   ";
			for (int j = 0; j < getSize(); j++) {
				if (linksMap.containsKey(i + "#" + j)) {
					k = Long.valueOf((String)linksMap.get(i + "#" + j).getProperty("k"));
					if (k < 10) str += " " + k + "  ";
					else if (k < 100) str += k + "  ";
					else str +=  k + " ";
				} else {
					str += " *  ";
				}
			}
			str += "\n";
		}
		return str;
	}
	
	public INode[] getFinalNodes() {
		INode n;
		ArrayList<INode> tmp = new ArrayList<>();
		for (Iterator<INode> it = nodes.values().iterator(); it.hasNext(); ) {
			n = it.next();
			if (n.isEndNode()) tmp.add(n);
		}
		INode[] nod = new INode[tmp.size()];
		return tmp.toArray(nod);
	}
	
	@Override
	public ArrayList<ILink> getRelatedLinks(INode node) {
		HashSet<ILink> res = new HashSet<>();
		int n = node.getNumber();
		int nd;
		if (nextsMap.containsKey(n)) {
			for (Iterator<Integer> it = nextsMap.get(n).iterator(); it.hasNext();) {
				nd = it.next();
				res.add(linksMap.get(n + "#" + nd));
			}
		}
		if (previousMap.containsKey(n)) {
			for (Iterator<Integer> it = previousMap.get(n).iterator(); it.hasNext();) {
				nd = it.next();
				res.add(linksMap.get(nd + "#" + n));
			}
		}
		return new ArrayList<>(res);
	}
	
	@Override
	public void clear() {
		nodes.clear();
		nextsMap.clear();
		previousMap.clear();
		linksMap.clear();
	}

	@Override
	public boolean isOriented() {
		return this.isOriented;
	}
	
}
