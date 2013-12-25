package com.graphs.ui;

import java.awt.Point;
import java.util.HashMap;

/**
 * Classe représentant un état du graphe. Cela permettra d'effectuer les actions ctrl+Z et ctrl+Y
 * @author Guedem & Souleyman
 *
 */
public class GraphState {

	public enum EnumAction{
		CREATE_NODE, EDIT_NODE, DELETE_NODE, MOVE_NODE, CREATE_LINK, EDIT_LINK, DELETE_LINK, CLEAR_GRAPH
	}
	
	private HashMap<String,Object> properties;
	
	private EnumAction typeAction;
	
	public GraphState(EnumAction typeAction){
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
		case DELETE_LINK:
			ctrlZOfDeleteLink(graph); 
			 break;
//		case CLEAR_GRAPH:
//			ctrlZOfClearGraph(graph);
//			break;
		default:
			 break;
		}
		
	}
	
	public void ctrlY(GraphUI graph){
		switch (typeAction) {
		case CREATE_NODE :
			 ctrlYOfCreateNode(graph); //methode de suppresion du noeud;
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
		case DELETE_LINK:
			ctrlYOfDeleteLink(graph); 
			break;
//		case CLEAR_GRAPH:
//			ctrlYOfClearGraph(graph);
//			break;
		default:
			break;
		}
	}
	
	/**
	 * Fonction permettant d'annuler la suppression d'un lien.
	 * Dans ce cas, on recrée le lien à nouveau.</br>
	 * Le map des propriétés devra contenir :
	 * <ul>
	 * <li>le numéro du noeud de départ (clé = num_origin) : int</li>
	 * <li>le numéro du noeud de départ (clé = num_end) : int</li>
	 * <li>les propriétés du lien supprimé (clé = props) : HashMap'<'String, Object'>'</li>
	 * </ul>
	 * @param graph
	 */
	@SuppressWarnings("unchecked")
	private void ctrlZOfDeleteLink(GraphUI graph) {
		int n1 = (int)properties.get("num_origin");
		int n2 = (int)properties.get("num_end");
		HashMap<String, Object> props = (HashMap<String, Object>)properties.get("props");
		graph.addLink(graph.getNode(n1), graph.getNode(n2), (HashMap<String, Object>) props.clone());
	}

	/**
	 * Fonction permettant d'annuler la modification des propriétés d'un lien.
	 * Dans ce cas, on recupère les anciennes propriétés du lien et on les lui réattribu.</br>
	 * Le map des propriétés devra contenir :
	 * <ul>
	 * <li>le numéro du noeud de départ (clé = num_origin) : int</li>
	 * <li>le numéro du noeud de départ (clé = num_end) : int</li>
	 * <li>les anciennes propriétés du lien (clé = old_props) : HashMap'<'String, Object'>'</li>
	 * </ul>
	 * @param graph
	 */
	@SuppressWarnings("unchecked")
	private void ctrlZOfEditLink(GraphUI graph) {
		int n1 = (int)properties.get("num_origin");
		int n2 = (int)properties.get("num_end");
		HashMap<String, Object> props = (HashMap<String, Object>)properties.get("old_props");
		LinkUI lnk = graph.getLink(graph.getNode(n1), graph.getNode(n2));
		lnk.getLink().setProperties((HashMap<String, Object>) props.clone());
		lnk.refreshProperties();
	}

	/**
	 * Fonction permettant d'annuler la création d'un noeud.
	 * Dans ce cas, on supprime le noeud à nouveau.</br>
	 * Le map des propriétés devra contenir :
	 * <ul>
	 * <li>le numéro du noeud (clé = num)</li>
	 * </ul>
	 * @param graph
	 */
	private void ctrlZOfCreateNode(GraphUI graph){
		int n = (int)properties.get("num");
		graph.removeNode(graph.getNode(n));
	}
	
	/**
	 * Fonction permettant d'annuler l'édition d'un noeud.
	 * Dans ce cas, on recupère les anciennes valeurs dans le map des propriétés et on les attribue au noeud.</br>
	 * Le map des propriétés devra contenir :
	 * <ul>
	 * <li>le numéro du noeud (clé = num) : int</li>
	 * <li>le map des anciennes propriétés (clé = old_props) : HashMap'<'String, Object'>'</li>
	 * </ul>
	 * @param graph
	 */
	@SuppressWarnings("unchecked")
	private void ctrlZOfEditNode(GraphUI graph){
		int num = (int)properties.get("num");
		HashMap<String, Object> props = (HashMap<String, Object>)properties.get("old_props");
		NodeUI node = graph.getNode(num);
		node.getNode().setProperties((HashMap<String, Object>) props.clone());
		node.refreshProperties();
	}
	
	/**
	 * Fonction permettant d'annuler le déplacement d'un noeud.
	 * Dans ce cas, on recupère les anciennes coordonnées du noeud dans le map des propriétés et on les attribue au noeud.</br>
	 * Le map des propriétés devra contenir :
	 * <ul>
	 * <li>le numéro du noeud (clé = num) : int</li>
	 * <li>les coordonnées de l'ancien point (clé = old_center) : Point</li>
	 * </ul>
	 * @param graph
	 */
	private void ctrlZOfMoveNode(GraphUI graph){
		int num = (int)properties.get("num");
		Point old_centre = (Point)properties.get("old_centre");
		NodeUI node = graph.getNode(num);
		node.drag((Point) old_centre.clone());
	}
	
	/**
	 * Fonction permettant d'annuler la suppression d'un noeud.
	 * Dans ce cas, on recupère les coordonnées et les propriétés du noeud supprimé.
	 * On recupère également les propriétés des anciens liens directement associés au noeud supprimé (vue qu'ils ont été supprimés avec lui).
	 * Ensuite, on recrée le noeud et ses liens.</br>
	 * Le map des propriétés devra contenir :
	 * <ul>
	 * <li>le numéro du noeud à recréer (clé = num) : int</li>
	 * <li>les coordonnées du centre du noeud à recréer (clé = centre) : Point</li>
	 * <li>les propriétés du noeud à recréer (clé = props) : HashMap'<'String, Object'>'</li>
	 * <li>les paramètres des anciens liens associés au noeud qu'on recrée (clé = liens) : HashMap'<'String, HashMap'<'String, Object'>''>'</li>
	 * </ul>
	 * @param graph
	 */
	@SuppressWarnings({ "unchecked"})
	private void ctrlZOfDeleteNode(GraphUI graph){
		int num = (int)properties.get("num");
		Point centre = (Point)properties.get("centre");
		HashMap<String, Object> props = (HashMap<String, Object>)properties.get("props");
		HashMap<String, HashMap<String, Object>> liens = (HashMap<String, HashMap<String, Object>>)properties.get("liens");
		
		NodeUI node = new NodeUI((Point) centre.clone(), num);
		node.getNode().setProperties((HashMap<String, Object>) props.clone());
		graph.addNode(node);
		if (liens != null) {
			int n1, n2;
			for (String key : liens.keySet()) {
				n1 = Integer.valueOf(key.split("#")[0]);
				n2 = Integer.valueOf(key.split("#")[1]);
				graph.addLink(graph.getNode(n1), graph.getNode(n2), liens.get(key));
			}
		}
	}
	
	/**
	 * Fonction permettant d'annuler la création d'un lien.
	 * Dans ce cas, on recupère les coordonnées du lien dans le map des propriétés, on le récupère et on le supprime.</br>
	 * Le map des propriétés devra contenir :
	 * <ul>
	 * <li>le numéro du noeud de départ (clé = num_origin) : int</li>
	 * <li>le numéro du noeud de départ (clé = num_end) : int</li>
	 * </ul>
	 * @param graph
	 */
	private void ctrlZOfCreateLink(GraphUI graph){
		int n1 = (int)properties.get("num_origin");
		int n2 = (int)properties.get("num_end");
		graph.removeLink(graph.getLink(graph.getNode(n1), graph.getNode(n2)));
	}
	
//	/**
//	 * Fonction permettant d'annuler le vidage d'un graphe.
//	 * On recupère une version sauvegardée du graphe dans les propriétés et on restaure le graphe.</br>
//	 * Le map des propriétés devra contenir :
//	 * <ul>
//	 * <li>Une sauvegarde du graphe (clé = graphe) : GraphUI</li>
//	 * </ul>
//	 * @param graph
//	 */
//	private void ctrlZOfClearGraph(GraphUI graph) {
//		graph = ((GraphUI)properties.get("graphe")).clone();
//	}
	
	
	
	
	
	
	/**
	 * Fonction permettant de repeter la suppression d'un lien.
	 * Dans ce cas, on recupère les coordonnées du lien dans le map des propriétés, on le récupère et on le supprime.</br>
	 * Le map des propriétés devra contenir :
	 * <ul>
	 * <li>le numéro du noeud de départ (clé = num_origin) : int</li>
	 * <li>le numéro du noeud de départ (clé = num_end) : int</li>
	 * </ul>
	 * @param graph
	 * @see GraphState#ctrlZOfCreateLink(GraphUI)
	 */
	private void ctrlYOfDeleteLink(GraphUI graph) {
		ctrlZOfCreateLink(graph);
	}

	/**
	 * Fonction permettant de repeter l'édition des propriétés d'un lien.
	 * Dans ce cas, on recupère les coordonnées du lien dans le map des propriétés ainsi que ses propriétés, on le récupère et on met à jour ses propriétés.</br>
	 * Le map des propriétés devra contenir :
	 * <ul>
	 * <li>le numéro du noeud de départ (clé = num_origin) : int</li>
	 * <li>le numéro du noeud de départ (clé = num_end) : int</li>
	 * <li>les nouvelles propriétés du lien à reéditer (clé = new_props) : HashMap'<'String, Object'>'</li>
	 * </ul>
	 * @param graph
	 */
	@SuppressWarnings("unchecked")
	private void ctrlYOfEditLink(GraphUI graph) {
		int n1 = (int)properties.get("num_origin");
		int n2 = (int)properties.get("num_end");
		HashMap<String, Object> props = (HashMap<String, Object>)properties.get("new_props");
		LinkUI lnk = graph.getLink(graph.getNode(n1), graph.getNode(n2));
		lnk.getLink().setProperties((HashMap<String, Object>) props.clone());
		lnk.refreshProperties();
	}

	/**
	 * Fonction permettant de repeter la création d'un lien.
	 * Dans ce cas, on recupère les coordonnées du lien dans le map des propriétés ainsi que ses propriétés, on le récupère et on le recrée.</br>
	 * Le map des propriétés devra contenir :
	 * <ul>
	 * <li>le numéro du noeud de départ (clé = num_origin) : int</li>
	 * <li>le numéro du noeud de départ (clé = num_end) : int</li>
	 * <li>les propriétés du lien créé (clé = props) : HashMap'<'String, Object'>'</li>
	 * </ul>
	 * @param graph
	 * @see GraphState#ctrlZOfDeleteLink(GraphUI)
	 */
	private void ctrlYOfCreateLink(GraphUI graph) {
		ctrlZOfDeleteLink(graph);
	}
	
	/**
	 * Fonction permettant de repeter l'édition d'un noeud.
	 * Dans ce cas, on recupère les nouveaux paramètres du noeud et on les lui réattribut.</br>
	 * Le map des propriétés devra contenir :
	 * <ul>
	 * <li>le numéro du noeud à déplacer (clé = num) : int</li>
	 * <li>les nouvelles propriétés du noeud (clé = new_props) : HashMap'<'String, Object'>'</li>
	 * </ul>
	 * @param graph
	 */
	@SuppressWarnings("unchecked")
	private void ctrlYOfEditNode(GraphUI graph){
		int num = (int)properties.get("num");
		HashMap<String, Object> props = (HashMap<String, Object>)properties.get("new_props");
		NodeUI node = graph.getNode(num);
		node.getNode().setProperties((HashMap<String, Object>) props.clone());
		node.refreshProperties();
	}

	/**
	 * Fonction permettant de repeter le déplacement d'un noeud.
	 * Dans ce cas, on recupère les nouvelles coordonnées du noeud et on le déplace à cette position.</br>
	 * Le map des propriétés devra contenir :
	 * <ul>
	 * <li>le numéro du noeud à déplacer (clé = num) : int</li>
	 * <li>les nouvelles coordonnées du centre (clé = new_centre) : Point</li>
	 * </ul>
	 * @param graph
	 */
	private void ctrlYOfMoveNode(GraphUI graph){
		int num = (int)properties.get("num");
		Point new_centre = (Point)properties.get("new_centre");
		NodeUI node = graph.getNode(num);
		node.drag((Point) new_centre.clone());
	}
	
	/**
	 * Fonction permettant de repeter la création d'un noeud.
	 * Dans ce cas, on recupère les  propriétés du noeud dans le map des propriétés et les liens associés à ces noeuds pour pouvoir les supprimer à nouveau.</br>
	 * Le map des propriétés devra contenir :
	 * <ul>
	 * <li>le numéro du noeud à supprimer (clé = num) : int</li>
	 * <li>les propriétés des liens directement associés au noeud qu'on veut suppriemr (clé = liens) : HashMap'<'String, HashMap'<'String, Object'>''>', la première clé (String) est sous la forme n1#n2 et la deuxième clé représente une propriété d'un lien n1#n2</li>
	 * </ul>
	 * @param graph
	 */
	@SuppressWarnings("unchecked")
	private void ctrlYOfDeleteNode(GraphUI graph){
		int num = (int)properties.get("num");
		HashMap<String, HashMap<String, Object>> liens = (HashMap<String, HashMap<String, Object>>)properties.get("liens");
		if (liens != null) {
			int n1, n2;
			for (String key : liens.keySet()) {
				n1 = Integer.valueOf(key.split("#")[0]);
				n2 = Integer.valueOf(key.split("#")[1]);
				graph.removeLink(graph.getLink(graph.getNode(n1), graph.getNode(n2)));
			}
		}
		graph.removeNode(graph.getNode(num));
	}
	
	/**
	 * Fonction permettant de repeter la création d'un noeud.
	 * Dans ce cas, on recupère les propriétés du noeud et on le recrée.</br>
	 * Le map des propriétés devra contenir :
	 * <ul>
	 * <li>le numéro du noeud (clé = num) : int</li>
	 * <li>les coordonnées du centre (clé = centre) : Point</li>
	 * <li>les propriétés du noeud créé (clé = props) : HashMap'<'String, Object'>'</li>
	 * </ul>
	 * @param graph
	 */
	@SuppressWarnings("unchecked")
	private void ctrlYOfCreateNode(GraphUI graph){
		int num = (int)properties.get("num");
		Point centre = (Point)properties.get("centre");
		HashMap<String, Object> props = (HashMap<String, Object>)properties.get("props");
		NodeUI node = new NodeUI((Point) centre.clone(), num);
		node.getNode().setProperties((HashMap<String, Object>) props.clone());
		graph.addNode(node);
	}
	
//	/**
//	 * Fonction permettant de repeter l'action de vidage du graphe entier
//	 * @param graph
//	 */
//	private void ctrlYOfClearGraph(GraphUI graph) {
//		graph.clear();
//	}
}