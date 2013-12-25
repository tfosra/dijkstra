package com.graphs.model.solver;

import java.util.Comparator;

import com.graphs.model.interfaces.ILink;

public class KruskalLinkComparator implements Comparator<ILink> {
	
	public int compare(ILink lnk1, ILink lnk2) {
		long k1, k2;
		k1 = (long) lnk1.getProperty("k");
		k2 = (long) lnk2.getProperty("k");
		if (k1 < k2) return -1;
		if (k1 > k2) return 1;		
		return 0;
	}

}
