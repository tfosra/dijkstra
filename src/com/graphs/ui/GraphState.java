package com.graphs.ui;

import java.awt.Point;
import java.util.HashMap;

public class GraphState<EnumAction> {

	public enum enumAction{
		CREATE_NODE, EDIT_NODE, DELETE_NODE, MOVE_NODE, CREATE_LINK, EDIT_LINK, DELETE_LINK, MOVE_LINK
	}
	
	private HashMap<String,Object> properties;
	
	private enumAction typeAction;
	
	public GraphState(enumAction typeAction){
		this.typeAction = typeAction;
		properties = new HashMap<>();
	}
	
	public void setProperty(String key, Object values){
		properties.put(key, values);
		
	}
	
	public void ctrlZ(GraphUI graph){
		switch(typeAction){
		case CREATE_NODE :
			 ctrlZOfCreateNode(graph); //methode de suppresion du noeud;
			 break;
		case EDIT_NODE :
			 ctrlZOfEditNode(graph); //methode d'edition du noeud pour ramener les anciennes valeurs
			 break;
		case MOVE_NODE:
			 ctrlZOfMoveNode(graph); // methode de deplacement du noeud pour le ramener à son ancienne position
			 break;
		case DELETE_NODE:
			ctrlZOfDeleteNode(graph); // methode de redessin du noeud effacé
			 break;
		case CREATE_LINK :
			 ctrlZOfCreateLink(graph); 
			 break;
		case EDIT_LINK :
			 ctrlZOfEditLink(graph); 
			 break;
		case MOVE_LINK:
			 ctrlZOfMoveLink(graph); 
			 break;
		case DELETE_LINK:
			ctrlZOfDeleteLink(graph); 
			 break;
		default:
			 break;
			
		
		}
		
	}
	
	
	private void ctrlZOfDeleteLink(GraphUI graph) {
		// TODO Auto-generated method stub
		
	}

	private void ctrlZOfMoveLink(GraphUI graph) {
		// TODO Auto-generated method stub
		
	}

	private void ctrlZOfEditLink(GraphUI graph) {
		// TODO Auto-generated method stub
		
	}

	public void ctrlY(GraphUI graph){
		switch (typeAction) {
		case CREATE_NODE :
			 createNodeCtrlY(graph); //methode de suppresion du noeud;
				break;
		case EDIT_NODE :
			 ctrlYOfEditNode(graph); //methode d'edition du noeud pour ramener les anciennes valeurs
			 break;
		case MOVE_NODE:
			 ctrlYOfMoveNode(graph); // methode de deplacement du noeud pour le ramener à son ancienne position
			 break;
		case DELETE_NODE:
			ctrlYOfDeleteNode(graph); // methode de dessin du noeud normal
			break;
		case CREATE_LINK :
			 ctrlYOfCreateLink(graph); 
				break;
		case EDIT_LINK :
			 ctrlYOfEditLink(graph); 
			 break;
		case MOVE_LINK:
			 ctrlYOfMoveLink(graph); 
			 break;
		case DELETE_LINK:
			ctrlYOfDeleteLink(graph); 
			break;
		default:
			break;
		}
	}
	
	private void ctrlYOfDeleteLink(GraphUI graph) {
		// TODO Auto-generated method stub
		
	}

	private void ctrlYOfMoveLink(GraphUI graph) {
		// TODO Auto-generated method stub
		
	}

	private void ctrlYOfEditLink(GraphUI graph) {
		// TODO Auto-generated method stub
		
	}

	private void ctrlYOfCreateLink(GraphUI graph) {
		// TODO Auto-generated method stub
		
	}

	//methode de suppresion du noeud;
	private void ctrlZOfCreateNode(GraphUI graph){
		int n=(int)properties.get("num");
//		Point centre=(Point)properties.get("centre");
		graph.removeNode(graph.getNode(n));
		
	}
	
	//methode de suppresion du noeud;
	private void createNodeCtrlY(GraphUI graph){
		
	}
	
	//methode d'edition du noeud pour ramener les anciennes valeurs
	private void ctrlZOfEditNode(GraphUI graph){
		
		
		
//		int num = (int)properties.get("num");
//		Point centre = (Point)properties.get("centre");
////		int rayon = (int)properties.get("rayon");
//		HashMap<String, Object> properties = (HashMap<String, Object>)properties.get("props");
//		HashMap<String, HashMap<String, Object>> liens = (HashMap<String, HashMap<String, Object>>)properties.get("liens");
//		
//		graph.addNode(new NodeUI(centre, num));
//		if (liens != null) {
//			int n1, n2;
//			for (String key : liens.keySet()) {
//				n1 = Integer.valueOf(key.split("#")[0]);
//				n2 = Integer.valueOf(key.split("#")[1]);
//				graph.addLink(graph.getNode(n1), graph.getNode(n2), liens.get(key));
//		
		
		
		
	}
	
	//methode d'edition du noeud pour ramener les anciennes valeurs
	private void ctrlYOfEditNode(GraphUI graph){
		
		
	}

	// methode de deplacement du noeud pour le ramener � son ancienne position
	private void ctrlZOfMoveNode(GraphUI graph){
//		int num=(int)properties.get("num");
//		Point centre=(Point) properties.get("centre");
//		graph.removeNode(graph.getNode(num));
//		graph.addNode();
		
		
	}
	
	// methode de deplacement du noeud pour le ramener � son ancienne position
	private void ctrlYOfMoveNode(GraphUI graph){
		
	}
	
	// methode de redessin du noeud effac�, y compris les noeuds si c'�tait le cas
	@SuppressWarnings("unchecked")
	private void ctrlZOfDeleteNode(GraphUI graph){
		int num = (int)properties.get("num");
		Point centre = (Point)properties.get("centre");
//		int rayon = (int)properties.get("rayon");
		HashMap<String, Object> props = (HashMap<String, Object>)properties.get("props");
		HashMap<String, HashMap<String, Object>> liens = (HashMap<String, HashMap<String, Object>>)properties.get("liens");
		
		graph.addNode(graph.getNode(num));
		if (liens != null) {
			int n1, n2;
			for (String key : liens.keySet()) {
				n1 = Integer.valueOf(key.split("#")[0]);
				n2 = Integer.valueOf(key.split("#")[1]);
				graph.addLink(graph.getNode(n1), graph.getNode(n2), liens.get(key));
			}
		}
	}
	
	// methode de dessin du noeud normal
	private void ctrlYOfDeleteNode(GraphUI graph){
		
	}
	
	@SuppressWarnings("unchecked")
	private void ctrlZOfCreateLink(GraphUI graph){
		
		int n1, n2;
		HashMap<String, HashMap<String, Object>> liens = (HashMap<String, HashMap<String, Object>>)properties.get("liens");
		
		for (String key : liens.keySet()) {
			n1 = Integer.valueOf(key.split("#")[0]);
			n2 = Integer.valueOf(key.split("#")[1]);
			graph.removeLink(graph.getLink(graph.getNode(n1), graph.getNode(n2)));
		}
		
		
	}
	
	
	
}