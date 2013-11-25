package com.graphs.model.solver;

import java.util.Comparator;

import com.graphs.model.interfaces.INode;

public class AStarComparator implements Comparator<INode> {

	Long[] f;
	Long[] g;
	
	public AStarComparator(Long[] f, Long[] g) {
		this.f = f;
		this.g = g;
	}
	
	public int compare(INode n1, INode n2) {
		long f1, f2, g1, g2;
//		f1 = Long.valueOf(n1.getProperty("f"));
//		f2 = Long.valueOf(n2.getProperty("f"));
//		g1 = Long.valueOf(n1.getProperty("g"));
//		g2 = Long.valueOf(n2.getProperty("g"));
		f1 = f[n1.getNumber()];
		f2 = f[n2.getNumber()];
		g1 = g[n1.getNumber()];
		g2 = g[n2.getNumber()];
		if (f1 < f2) return -1;
		if (f1 > f2) return 1;
		if (g1 > g2) return -1;
		if (g1 < g2) return 1;		
		return 0;
	}

}
