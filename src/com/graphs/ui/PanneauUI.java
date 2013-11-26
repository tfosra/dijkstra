package com.graphs.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import com.graphs.model.solver.GraphSolver.SolverMethod;
import com.graphs.ui.FormUI.Etats;

public class PanneauUI extends JPanel implements MouseMotionListener, MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Liste des modes d'utilisation du panneau principal
	 * @author Souleyman
	 *
	 */
	public enum DrawingState {
		INSERT_NODE, INSERT_LINK, DELETE_ELEMENT, NORMAL, SOLVER
	}
	
	// Création des curseurs de l'application
	private final static Toolkit tk = Toolkit.getDefaultToolkit();
	public final static Cursor DELETE_ELEMENT_CURSOR = tk.createCustomCursor(tk.createImage("image/delete.png"), new Point(16, 16), "Suppression");
	public final static Cursor INSERT_LINK_CURSOR = tk.createCustomCursor(tk.createImage("image/insert-link.png"), new Point(16, 16), "Insertion_lien");
	public final static Cursor INSERT_NODE_CURSOR = tk.createCustomCursor(tk.createImage("image/original/noeud_vert.png"), new Point(16, 16), "Insertion_noeud");
	public final static Cursor NORMAL_CURSOR = tk.createCustomCursor(tk.createImage("image/default.png"), new Point(2, 2), "Normal");
	
	
	private GraphUI graph;
	private FormUI previousForm;
	private FormUI selectedForm;
	private DrawingState state;
	private Point previousOrign;
	
	public PanneauUI() {
		setPreferredSize(new Dimension(500, 500));
		initParameters();
		initComponents();
		addMouseListener(this);
		addMouseMotionListener(this);
		setState(DrawingState.NORMAL);
	}
	
	private void initParameters() {
		graph = new GraphUI();
	}

	private void initComponents() {
		this.setLayout(null);
		this.setBackground(Color.white);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		graph.renderGraph(g);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		switch(state) {
		case DELETE_ELEMENT:
			mouseClickedDeleteElement(e);
			break;
		case INSERT_LINK:
			mouseClickedInsertLink(e);
			break;
		case INSERT_NODE:
			mouseClickedInsertNode(e);
			break;
		case NORMAL:
			mouseClickedNormal(e);
			break;
		case SOLVER:
			mouseClickedSolver(e);
			break;
		}
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		switch(state) {
		case DELETE_ELEMENT:
			mousePressedDeleteElement(e);
			break;
		case INSERT_LINK:
			mousePressedInsertLink(e);
			break;
		case INSERT_NODE:
			mousePressedInsertNode(e);
			break;
		case NORMAL:
			mousePressedNormal(e);
			break;
		case SOLVER:
			mousePressedSolver(e);
			break;
		}
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		switch(state) {
		case DELETE_ELEMENT:
			mouseReleasedDeleteElement(e);
			break;
		case INSERT_LINK:
			mouseReleasedInsertLink(e);
			break;
		case INSERT_NODE:
			mouseReleasedInsertNode(e);
			break;
		case NORMAL:
			mouseReleasedNormal(e);
			break;
		case SOLVER:
			mouseReleasedSolver(e);
			break;
		}
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		switch(state) {
		case DELETE_ELEMENT:
			mouseDraggedDeleteElement(e);
			break;
		case INSERT_LINK:
			mouseDraggedInsertLink(e);
			break;
		case INSERT_NODE:
			mouseDraggedInsertNode(e);
			break;
		case NORMAL:
			mouseDraggedNormal(e);
			break;
		case SOLVER:
			mouseDraggedSolver(e);
			break;
		}
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		switch(state) {
		case DELETE_ELEMENT:
			mouseMovedDeleteElement(e);
			break;
		case INSERT_LINK:
			mouseMovedInsertLink(e);
			break;
		case INSERT_NODE:
			mouseMovedInsertNode(e);
			break;
		case NORMAL:
			mouseMovedNormal(e);
			break;
		case SOLVER:
			mouseMovedSolver(e);
			break;
		}
		repaint();
	}
	
	public void setState(DrawingState state) {
		switch (state) {
		case DELETE_ELEMENT:;
			setCursor(DELETE_ELEMENT_CURSOR);
			break;
		case INSERT_LINK:
			setCursor(INSERT_LINK_CURSOR);
			break;
		case INSERT_NODE:
			setCursor(INSERT_NODE_CURSOR);
			break;
		case NORMAL:
			setCursor(NORMAL_CURSOR);
			break;
		case SOLVER:
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			ArrayList<NodeUI> list = graph.getNodes();
			String[] vals = new String[list.size()];
			for (int i = 0; i < vals.length; i++) vals[i] = list.get(i).getNode().getNumber() + " - " + list.get(i).getNode().getProperty("name");
			JComboBox<String> combo1 = new JComboBox<>(vals);
			JComboBox<String> combo2 = new JComboBox<>(vals);
			JPanel pan1 = new JPanel(new GridLayout(1, 0));
			pan1.add(combo1);
			pan1.add(combo2);
			int option = JOptionPane.showConfirmDialog(null, pan1, "Shortest path", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (option == JOptionPane.OK_OPTION) {
				String str1 = (String)combo1.getSelectedItem();
				String str2 = (String)combo2.getSelectedItem();
				str1 = str1.split("-")[0].trim();
				str2 = str2.split("-")[0].trim();
				int nbr1 = Integer.valueOf(str1);
				int nbr2 = Integer.valueOf(str2);
				NodeUI n1 = graph.getNode(nbr1);
				NodeUI n2 = graph.getNode(nbr2);
				graph.solvePath(n1, n2, SolverMethod.A_STAR_METHOD);
				repaint();
			}
			break;
		}
		this.state = state;
	}
	
	public void saveGraph() {
		BufferedImage bf = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = bf.createGraphics();
		paintComponent(g);
		try {
			ImageIO.write(bf, "png", new File("output.png"));
			JOptionPane.showMessageDialog(null, "Image registered successfully");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Liste des méthodes liées à la gestion des évènements de la souris sur le panneau
	 */
	
	// Evènements quand on appuie sur une touche de la souris
	private void mousePressedNormal(MouseEvent e) {
		FormUI f = graph.getPointedForm(e.getPoint());
		previousOrign = new Point(e.getX(),e.getY());
		if (previousForm != null) {
			previousForm.setState(Etats.NORMAL);
		}
		previousForm = f;
		if (f != null) {
			f.setState(Etats.CLICKED);
		}
	}
	
	private void mousePressedDeleteElement(MouseEvent e) {
		// Nothing here
	}
	
	private void mousePressedInsertLink(MouseEvent e) {
		// Nothing here
	}
	
	private void mousePressedInsertNode(MouseEvent e) {
		// Nothing here
	}
	
	private void mousePressedSolver(MouseEvent e) {
		// Nothing here
	}
	
	// Evènements quand on relâche la souris
	private void mouseReleasedNormal(MouseEvent e) {
		FormUI f = graph.getPointedForm(e.getPoint());
		if (previousForm != null) {
			previousForm.setState(Etats.NORMAL);
		}
		previousForm = f;
		if (f != null) {
			f.setState(Etats.OVER);
		}
		repaint();
	}
	
	private void mouseReleasedDeleteElement(MouseEvent e) {
		// Nothing here
	}
	
	private void mouseReleasedInsertLink(MouseEvent e) {
		// Nothing here
	}
	
	private void mouseReleasedInsertNode(MouseEvent e) {
		// Nothing here
	}
	
	private void mouseReleasedSolver(MouseEvent e) {
		// Nothing here
	}
	
	// Evènements quand on déplace la souris
	private void mouseDraggedNormal(MouseEvent e) {
		FormUI f = graph.getPointedForm(e.getPoint());
		if (previousForm != null) {
			previousForm.setState(Etats.NORMAL);
			previousForm = f;
		}
		if (f != null) {
			f.setState(Etats.CLICKED);
			f.drag(e.getPoint());
		}
	}
	
	private void mouseDraggedDeleteElement(MouseEvent e) {
		// Nothing here
	}
	
	private void mouseDraggedInsertLink(MouseEvent e) {
		FormUI f = graph.getPointedForm(e.getPoint());
		if (previousForm != null) {
			previousForm.setState(Etats.NORMAL);
			previousForm = f;
		}
		if (f != null) {
			f.setState(Etats.CLICKED);
			f.drag(e.getPoint());
		}
	}
	
	private void mouseDraggedInsertNode(MouseEvent e) {
		// Nothing here
	}
	
	private void mouseDraggedSolver(MouseEvent e) {
		FormUI f = graph.getPointedForm(e.getPoint());
//		if (previousForm != null) {
//			previousForm.setState(Etats.NORMAL);
//			previousForm = f;
//		}
		if (f != null) {
//			f.setState(Etats.CLICKED);
			f.drag(e.getPoint());
		}
	}
	
	// Evènements quand on bouge la souris sans toutefois cliquer
	private void mouseMovedNormal(MouseEvent e) {
		FormUI f = graph.getPointedForm(e.getPoint());
		if (previousForm != null && previousForm != selectedForm) {
			previousForm.setState(Etats.NORMAL);
		}
		previousForm = f;
		if (f != null && f != selectedForm) {
			f.setState(Etats.OVER);
		}
		repaint();
	}
	
	private void mouseMovedDeleteElement(MouseEvent e) {
		mouseMovedNormal(e);
	}
	
	private void mouseMovedInsertLink(MouseEvent e) {
		mouseMovedNormal(e);
	}
	
	private void mouseMovedInsertNode(MouseEvent e) {
		mouseMovedNormal(e);
	}
	
	private void mouseMovedSolver(MouseEvent e) {
		// Nothing here
	}
	
	// Evènements quand on clique sur le panneau
	private void mouseClickedNormal(MouseEvent e) {
		final FormUI f = graph.getPointedForm(e.getPoint());
		if (e.getButton() == MouseEvent.BUTTON3) {
			contextualMenu(e.getPoint(), f);
			return;
		}
		if (e.getClickCount() == 2) {
			if (previousForm != null) {
				previousForm.setState(Etats.NORMAL);
			}
			previousForm = f;
			if (f != null) f.setState(Etats.EDITING);
		}
	}
	
	private void mouseClickedDeleteElement(MouseEvent e) {
		final FormUI f = graph.getPointedForm(e.getPoint());
		if (e.getButton() == MouseEvent.BUTTON3) {
			contextualMenu(e.getPoint(), f);
			return;
		}
		if (e.getClickCount() != 1) return;
		if (f == null) {
			setState(DrawingState.NORMAL);
		}
		else {
			graph.removeForm(f);
			if (previousForm == selectedForm) previousForm = null;
			selectedForm = null;
		}
	}
	
	private void mouseClickedInsertLink(MouseEvent e) {
		final FormUI f = graph.getPointedForm(e.getPoint());
		if (e.getButton() == MouseEvent.BUTTON3) {
			contextualMenu(e.getPoint(), f);
			return;
		}
		if (e.getClickCount() != 1) return;
		if (f == null) {
			if (selectedForm != null) selectedForm.setState(Etats.NORMAL);
			previousForm = selectedForm = null;
			setState(DrawingState.NORMAL);
		}
		else {
			if (f instanceof NodeUI) {
				if (selectedForm != null && !selectedForm.equals(f)) {
					graph.addLink((NodeUI)selectedForm, (NodeUI)f);
					selectedForm.setState(Etats.NORMAL);
					selectedForm = null;
				} else if (!f.equals(selectedForm)) {
					//selectedForm.setState(Etats.NORMAL);
					selectedForm = f;
					selectedForm.setState(Etats.SELECTED);
				}
			}
			else {
				setState(DrawingState.NORMAL);
			}
		}
	}
	
	private void mouseClickedInsertNode(MouseEvent e) {
		final FormUI f = graph.getPointedForm(e.getPoint());
		if (e.getButton() == MouseEvent.BUTTON3) {
			contextualMenu(e.getPoint(), f);
			return;
		}
		if (e.getClickCount() != 1) return;
		if (f == null) {
			// Ajout d'une nouvelle forme
			NodeUI node = new NodeUI(new Point(e.getX(), e.getY()));
			graph.addNode(node);
		}
	}
	
	private void mouseClickedSolver(MouseEvent e) {
		final FormUI f = graph.getPointedForm(e.getPoint());
		if (e.getButton() == MouseEvent.BUTTON3) {
			contextualMenu(e.getPoint(), f);
			return;
		}
	}
	
	private void contextualMenu(Point p, final FormUI f) {
		setState(DrawingState.NORMAL);
		if (f != null) {		// Menu contextuel d'une forme
			if (previousForm != null) {
				previousForm.setState(Etats.NORMAL);
			}
			previousForm = f;
			JPopupMenu menu = new JPopupMenu();
			JMenuItem deleteItem = new JMenuItem("Delete form");
			JMenuItem editItem = new JMenuItem("Edit form");
			menu.add(deleteItem);
			menu.add(editItem);
			menu.show(this, p.x, p.y);
			menu.setCursor(NORMAL_CURSOR);
			deleteItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					graph.removeForm(f);
				}
			});
			editItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					f.setState(Etats.EDITING);
				}
			});
		} else {		// Menu contextuel du panneau
			if (previousForm != null) {
				previousForm.setState(Etats.NORMAL);
				previousForm = null;
			}
			JPopupMenu menu = new JPopupMenu();
			JMenuItem saveItem = new JMenuItem("Save graph");
			menu.add(saveItem);
			menu.show(this, p.x, p.y);
			menu.setCursor(NORMAL_CURSOR);
			saveItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					saveGraph();
				}
			});
		}
	}
	
}
