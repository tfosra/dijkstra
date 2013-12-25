package com.graphs.model.solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import com.graphs.model.GraphPath;
import com.graphs.model.interfaces.IGraph;
import com.graphs.model.interfaces.ILink;
import com.graphs.model.interfaces.INode;

public class GraphSolver {

	public enum SolverMethod {
		A_STAR_METHOD, KRUSKAL_METHOD
	}
	
	public final static long INFINITY = Long.MAX_VALUE;
	
	public static GraphPath solvePath(IGraph graph, SolverMethod method, Object...params) {
		switch (method) {
		case A_STAR_METHOD : 
			return aStarShortestPath(graph, (INode)params[0], (INode)params[1]);
		case KRUSKAL_METHOD:
			return kruskalMinimalPath(graph);
		default:
			break;
		}
		return null;
	}
	
	/**
	 * Determines the shortest path from a node to another in a given graph
	 * @param graph The graph that contains all the nodes
	 * @param n1 The start node
	 * @param n2 The end node
	 * @return The path which is supposed to be the shortest or null if it doesn't exist
	 */
	private static GraphPath aStarShortestPath(final IGraph graph, final INode n1, final INode n2) {
		ArrayList<INode> p = new ArrayList<>();
		ArrayList<INode> q = new ArrayList<>();
		INode[] childs;
		Long[] f = new Long[graph.getSize()];
		Long[] g = new Long[graph.getSize()];
		int[] parents = new int[graph.getSize()];
		//Initialisation de g à l'infinie pour tous les noeuds		
		for (int i = 0; i < g.length; i++) {
			g[i] = INFINITY;
		}
		g[n1.getNumber()] = 0L;
		INode u = n1;
		
		p.add(n1);
		int nu, nv;
		long kuv;
		while (!p.isEmpty() && !u.equals(n2)) {
			u = p.remove(0);
			q.add(u);
			nu = u.getNumber();
			childs = graph.getNextElements(u);
			if (childs != null) {
				for (int i = 0; i < childs.length; i++) {
					nv = childs[i].getNumber();
					kuv = (Long)graph.getLink(u, childs[i]).getProperty("k");
					if ((p.contains(childs[i]) || q.contains(childs[i])) && (g[nv] <= g[nu] + kuv)) continue;
					g[nv] = g[nu] + kuv;
					f[nv] = g[nv] + (Long)childs[i].getProperty("h");
					parents[nv] = nu;
					if (!p.contains(childs[i])) p.add(childs[i]);
				}
			}
			Collections.sort(p, new AStarComparator(f, g));
		}
		if (!u.equals(n2)) return null;		
		LinkedList<ILink> res = new LinkedList<>();
		int parent;//, nbr = u.getNumber();
//		res.addFirst(u);
		INode old = u;
		while (!u.equals(n1)) {
			parent = parents[u.getNumber()];
			u = graph.getNode(parent);
//			res.addFirst(u);
			res.addFirst(graph.getLink(u, old));
			old = u;
		}
		return new GraphPath(res);
	}
	
	/**
	 * Determines the minimum spanning tree (MST) of the given graph using the Kruskal algorithm
	 * @param graph The graph that contains all the nodes
	 * @return The path which is supposed to be the minimum spanning tree of the graph or null if it doesn't exist
	 */
	public static GraphPath kruskalMinimalPath(IGraph graph) {
		LinkedList<ILink> res = new LinkedList<>();
		
		LinkedList<ILink> links = new LinkedList<>(graph.getLinks());
		Collections.sort(links, new KruskalLinkComparator());				// Sorts all the links in a non-decreasing order
		ArrayList<HashSet<Integer>> sets = new ArrayList<>();				// The list of all the sets		
		HashMap<Integer, HashSet<Integer>> nodeSetMap = new HashMap<>();	// Maps each node with the subset containing it (kruskal algorithm)
		
		// Initialisation phase
		HashSet<Integer> set;
		for (INode nd : graph.getNodes()) {
			set = new HashSet<>();
			set.add(nd.getNumber());
			sets.add(set);
			nodeSetMap.put(nd.getNumber(), set);							// Each node is in its own set first
		}
		
		ILink lnk;
		INode n1, n2;
		HashSet<Integer> set1, set2;
		while (res.size() < graph.getSize() - 1) {							// We end when whe have already selected n - 1 links
			if ((lnk = links.pollFirst()) == null) break;					// We peek the first link
			n1 = lnk.getStartNode();
			n2 = lnk.getEndNode();
			set1 = nodeSetMap.get(n1.getNumber());
			set2 = nodeSetMap.get(n2.getNumber());
			
			if (!intersect(set1, set2)) {										// If the two nodes are not in the same subset
				res.add(lnk);												// Adding the current link in the result set.
				set1.addAll(set2);											// Merges the two subsets
				sets.remove(set2);
				for (int nbr : set2) {
					nodeSetMap.put(nbr, set1);
				}
			}
			
		}
		
		return new GraphPath(res);
	}
	
	@SuppressWarnings("unchecked")
	private static<T> boolean intersect(HashSet<T> set1, HashSet<T> set2) {
		HashSet<T> s = (HashSet<T>) set1.clone();
		s.retainAll(set2);
		return !s.isEmpty();
	}
	
}
