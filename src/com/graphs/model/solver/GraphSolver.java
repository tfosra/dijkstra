package com.graphs.model.solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import com.graphs.model.GraphPath;
import com.graphs.model.interfaces.IGraph;
import com.graphs.model.interfaces.INode;

public class GraphSolver {

	public final static int A_STAR_METHOD = 11;
	
	public final static long INFINITY = Long.MAX_VALUE;
	
	public static GraphPath shortestPath(IGraph graph, INode n1, INode n2, int method) {
		switch (method) {
		case (A_STAR_METHOD) : return aStarShortestPath(graph, n1, n2);
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
			//graph.getNode(i).setProperty("g", INFINITY + "");
		}
		g[n1.getNumber()] = 0L;
		INode u = n1;
		//u.setProperty("g", 0 + "");
		
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
					kuv = Long.valueOf((String)graph.getLink(u, childs[i]).getProperty("k"));
					if ((p.contains(childs[i]) || q.contains(childs[i])) && (g[nv] <= g[nu] + kuv)) continue;
					g[nv] = g[nu] + kuv;
					f[nv] = g[nv] + Long.valueOf((String)childs[i].getProperty("h"));
					//childs[i].setProperty("parent", nu + "");
					//childs[i].setProperty("g", g[nv] + "");
					//childs[i].setProperty("f", f[nv] + "");
					parents[nv] = nu;
					if (!p.contains(childs[i])) p.add(childs[i]);
				}
			}
			Collections.sort(p, new AStarComparator(f, g));
		}
		if (!u.equals(n2)) return null;		
		LinkedList<INode> res = new LinkedList<>();
		int parent;//, nbr = u.getNumber();
		res.addFirst(u);
		while (!u.equals(n1)) {
			//parent = Integer.valueOf(u.getProperty("parent"));
			parent = parents[u.getNumber()];
			u = graph.getNode(parent);
			res.addFirst(u);
		}
//		return new GraphPath(res.toArray(childs), f[nbr]);
		return new GraphPath(graph, res);
	}
	
	
//	/**
//	 * Determines the shortest path from a node to another in a given graph
//	 * @param graph The graph that contains all the nodes
//	 * @param n1 The start node
//	 * @param n2 The end node
//	 * @return The path which is supposed to be the shortest or null if it doesn't exist
//	 */
//	private static GraphPath aStarShortestPath(final IGraph graph, final INode n1) {
//		ArrayList<INode> p = new ArrayList<>();
//		ArrayList<INode> q = new ArrayList<>();
//		INode[] childs;
//		Long[] f = new Long[graph.getSize()];
//		Long[] g = new Long[graph.getSize()];
//		int[] parents = new int[graph.getSize()];
//		//Initialisation de g à l'infinie pour tous les noeuds		
//		for (int i = 0; i < g.length; i++) {
//			g[i] = INFINITY;
//			//graph.getNode(i).setProperty("g", INFINITY + "");
//		}
//		g[n1.getNumber()] = 0L;
//		INode u = n1;
//		//u.setProperty("g", 0 + "");
//		
//		p.add(n1);
//		int nu, nv;
//		long kuv;
//		while (!p.isEmpty() && !u.isEndNode()) {
//			u = p.remove(0);
//			q.add(u);
//			nu = u.getNumber();
//			childs = graph.getNextElements(u);
//			if (childs != null) {
//				for (int i = 0; i < childs.length; i++) {
//					nv = childs[i].getNumber();
//					kuv = Long.valueOf(graph.getLink(u, childs[i]).getProperty("k"));
//					if ((p.contains(childs[i]) || q.contains(childs[i])) && (g[nv] <= g[nu] + kuv)) continue;
//					g[nv] = g[nu] + kuv;
//					f[nv] = g[nv] + Long.valueOf(childs[i].getProperty("h"));
//					//childs[i].setProperty("parent", nu + "");
//					//childs[i].setProperty("g", g[nv] + "");
//					//childs[i].setProperty("f", f[nv] + "");
//					parents[nv] = nu;					
//					p.add(childs[i]);
//				}
//			}
//			Collections.sort(p, new AStarComparator(f, g));
//		}
//		//if (p.isEmpty()) return null;		
//		LinkedList<INode> res = new LinkedList<>();
//		int parent, nbr = u.getNumber();
//		res.addFirst(u);
//		while (!u.equals(n1)) {
//			//parent = Integer.valueOf(u.getProperty("parent"));
//			parent = parents[u.getNumber()];
//			u = graph.getNode(parent);
//			res.addFirst(u);
//		}
//		childs = new INode[res.size()];
//		return new GraphPath(res.toArray(childs), f[nbr]);
//	}
	
}
