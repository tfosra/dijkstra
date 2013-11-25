package com.graphs.model.interfaces;

import java.util.ArrayList;

public interface IGraph {

	public INode[] getNodes();
	
	public INode[] getNextElements(INode n);
	
	public INode[] getPreviousElements(INode n);
	
	public boolean areLinked(INode n1, INode n2);
	
	public void addNode(INode n);
	
	public void removeNode(INode n);
	
	public ILink getLink(INode n1, INode n2);
	
	public ArrayList<ILink> getRelatedLinks(INode n);
	
	public void addLink(INode n1, INode n2);
	
	public void removeLink(ILink lnk);
	
	public int getSize();
	
	public INode getNode(int nbr);
	
	public void addLink(ILink lnk);
	
	public INode[] getFinalNodes();
	
}
