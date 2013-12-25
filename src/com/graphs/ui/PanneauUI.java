package com.graphs.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import com.graphs.model.solver.GraphSolver.SolverMethod;
import com.graphs.ui.FormUI.Etats;
import com.graphs.ui.GraphState.EnumAction;

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
		INSERT_NODE, INSERT_LINK, DELETE_ELEMENT, NORMAL, SOLVER, SELECTION
	}
	
	// Création des curseurs de l'application
	private final static int UNDO_LIST_MAX_SIZE = 20;
	private final static Toolkit tk = Toolkit.getDefaultToolkit();
	public final static Cursor DELETE_ELEMENT_CURSOR = tk.createCustomCursor(tk.createImage(ImageIcon.class.getResource("/image/new/cross32.png")), new Point(16, 16), "Suppression");
	public final static Cursor INSERT_LINK_CURSOR = tk.createCustomCursor(tk.createImage(ImageIcon.class.getResource("/image/insert-link.png")), new Point(16, 16), "Insertion_lien");
	public final static Cursor INSERT_NODE_CURSOR = tk.createCustomCursor(tk.createImage(ImageIcon.class.getResource("/image/new/noeud_vert32.png")), new Point(16, 16), "Insertion_noeud");
	public final static Cursor NORMAL_CURSOR = tk.createCustomCursor(tk.createImage(ImageIcon.class.getResource("/image/new/default32.png")), new Point(2, 2), "Normal");
	
	public final static ImageIcon UNDO_ICONE = new ImageIcon(ImageIcon.class.getResource("/image/undo-icone2.png"));
	public final static ImageIcon REDO_ICONE = new ImageIcon(ImageIcon.class.getResource("/image/redo-icone2.png"));
	
	private Image bufferImage = null;						// The buffer image used for double buffering the panel display
	private Graphics bufferImageGraphics;					// The graphics object of the bufferImage
	
	private ArrayList<FormUI> selectionList;
	private LinkedList<GraphState> undoList;
	private LinkedList<GraphState> redoList;
	private boolean groupSelection;
	private boolean hasDraggedNode;
	private GraphUI graph;
	private FormUI previousForm;
	private FormUI selectedForm;
	private FormUI draggedForm;
	private DrawingState state;
	private Point previousOrign, nextOrigin;
	private Image fond;
	
	private JTextField stateField;
	public JButton annulerButton;
	public JButton repeterButton;
	public JCheckBox nodeLabelCheckbox;
	public JCheckBox linkLabelCheckbox;
	private GraphFrame gf;
	
	public PanneauUI(GraphFrame gf, JTextField stateField) {
		this.gf = gf;
		this.stateField = stateField;
		setPreferredSize(new Dimension(500, 500));
		initParameters();
		initComponents();
		addMouseListener(this);
		addMouseMotionListener(this);
		setState(DrawingState.NORMAL);
		fond = new ImageIcon(ImageIcon.class.getResource("/image/yaounde2.jpg")).getImage();
		setPreferredSize(new Dimension(fond.getWidth(this), fond.getHeight(this)));
		setSize(getPreferredSize());
	}
	
	private void initParameters() {
		JCheckBox check = new JCheckBox("Oriented graph");
		JOptionPane.showMessageDialog(null, check);
		graph = new GraphUI(check.isSelected());
		selectionList = new ArrayList<>();
		undoList = new LinkedList<>();
		redoList = new LinkedList<>();
	}

	private void initComponents() {
		this.setLayout(null);
		this.setBackground(Color.white);
		
		annulerButton = new JButton(UNDO_ICONE);
		repeterButton = new JButton(REDO_ICONE);
		
		annulerButton.setBorder(BorderFactory.createEtchedBorder());
		annulerButton.setBorderPainted(false);
		annulerButton.setToolTipText("Annuler");
		annulerButton.setFocusable(false);
		
		repeterButton.setBorder(BorderFactory.createEtchedBorder());
		repeterButton.setBorderPainted(false);
		repeterButton.setToolTipText("Repeter");
		repeterButton.setFocusable(false);
		
		annulerButton.setEnabled(isUndoable());
		repeterButton.setEnabled(isRedoable());
		
		nodeLabelCheckbox = new JCheckBox("Node labels", true);
		linkLabelCheckbox = new JCheckBox("Link labels", true);
		
		annulerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrlZ();
			}
		});
		repeterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrlY();
			}
		});
		nodeLabelCheckbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graph.setDisplayNodeLabels(nodeLabelCheckbox.isSelected());
				repaint();
			}
		});
		linkLabelCheckbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graph.setDisplayLinkLabels(linkLabelCheckbox.isSelected());
				repaint();
			}
		});
	}
	
	public JButton getUndoButton() {
		return annulerButton;
	}
	
	public JButton getRedoButton() {
		return repeterButton;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		refreshDisplay();
		if (bufferImage != null) {
			g.drawImage(bufferImage, 0, 0, null);
		}
		annulerButton.setEnabled(isUndoable());
		repeterButton.setEnabled(isRedoable());
	}
	
	private void refreshDisplay() {
		if (bufferImage == null) {
			bufferImage = createImage(getWidth(), getHeight());
			bufferImageGraphics = bufferImage.getGraphics();
		}
		bufferImageGraphics.drawImage(fond, 0, 0, this);
		graph.renderGraph(bufferImageGraphics);
		if (state == DrawingState.SELECTION) {
			if (previousOrign == null || nextOrigin == null) return;
			bufferImageGraphics.setColor(Color.red);
			int x = Math.min(previousOrign.x, nextOrigin.x);
			int y = Math.min(previousOrign.y, nextOrigin.y);
			int width = Math.abs(previousOrign.x - nextOrigin.x);
			int height = Math.abs(previousOrign.y - nextOrigin.y);
			bufferImageGraphics.drawRect(x, y, width, height);
		}
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
		case SELECTION:
			mouseClickedSelection(e);
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
		case SELECTION:
			mousePressedSelection(e);
			break;
		default :
			mousePressedNormal(e);
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
		case SELECTION:
			mouseReleasedSelection(e);
			break;
		default :
			mouseReleasedNormal(e);
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
		case SELECTION :
			mouseDraggedSelection(e);
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
		case SELECTION:
			mouseMovedSelection(e);
			break;
		default :
			mousePressedNormal(e);
			break;
		}
		repaint();
	}
	
	public void setState(DrawingState state, Object...params) {
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
			solveGraph((SolverMethod)params[0]);
			break;
		case SELECTION:
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			break;
		}
		this.state = state;
	}
	
	public void solveGraph(SolverMethod method) {
		switch (method) {
		case A_STAR_METHOD:
			if (!graph.isOriented()) {
				JOptionPane.showMessageDialog(null, "The Dijsktra algorithm only works on oriented graphs");
				return;
			}
			ArrayList<NodeUI> list = graph.getNodes();
			if (list.isEmpty()) return;
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
				long pathLength = graph.solvePath(SolverMethod.A_STAR_METHOD, n1.getNode(), n2.getNode());
				stateField.setText("Path length : " + pathLength);
				repaint();
			}
			break;
		case KRUSKAL_METHOD:
			if (graph.isOriented()) {
				JOptionPane.showMessageDialog(null, "The Kruskal algorithm only works on non-oriented graphs");
				return;
			}
			long pathLength = graph.solvePath(SolverMethod.KRUSKAL_METHOD);
			stateField.setText("Path length : " + pathLength);
			repaint();
			break;
		}
		
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
		nextOrigin = previousOrign = e.getPoint();
		if (previousForm != null) {
			previousForm.setState(Etats.NORMAL);
		}
		previousForm = f;
		if (f != null) {
			draggedForm = f;
			f.setState(Etats.CLICKED);
		}
		else { // Si on veut effectuer une selection (click dans le vide)
			setState(DrawingState.SELECTION);
			if (selectionList != null) for (FormUI form : selectionList) form.setState(Etats.NORMAL);
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
	
	private void mousePressedSelection(MouseEvent e) {
		FormUI f = graph.getPointedForm(e.getPoint());
		if (f != null) {
			// XXX
		}
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
		if (hasDraggedNode) {
			GraphState gs = new GraphState(EnumAction.MOVE_NODE);
			gs.setProperty("num", ((NodeUI)draggedForm).getNode().getNumber());
			gs.setProperty("old_centre", previousOrign.clone());
			gs.setProperty("new_centre", ((NodeUI)draggedForm).getCenter().clone());
			saveUndoAction(gs);
			hasDraggedNode = false;
		}
		draggedForm = null;
		previousOrign = nextOrigin = null;
		setState(DrawingState.NORMAL);
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
	
	private void mouseReleasedSelection(MouseEvent e) {
		// Nothing here
	}
	
	// Evènements quand on déplace la souris
	private void mouseDraggedNormal(MouseEvent e) {
		FormUI f = graph.getPointedForm(e.getPoint());
		nextOrigin = e.getPoint();
		if (previousForm != null) {
			previousForm.setState(Etats.NORMAL);
			previousForm = f;
		}
		if (f != null) {
			if (!hasDraggedNode) hasDraggedNode = true;
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
			if (!hasDraggedNode) hasDraggedNode = true;
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
	
	private void mouseDraggedSelection(MouseEvent e) {
		// TODO Rechercher toutes les formes prises dans le rectangle de la sélection
		nextOrigin = e.getPoint();
		if (selectionList != null) {
			for (FormUI form : selectionList) {
				form.setState(Etats.NORMAL);
			}
		}
		if (previousOrign != null && nextOrigin != null) {
			selectionList = graph.getSelectedForms(new Line2D.Double(previousOrign, nextOrigin).getBounds());
		}
		for (FormUI form : selectionList) {
			form.setState(Etats.SELECTED);
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
		if (f != null) {
			stateField.setText(f.toString());
		}
		else {
			stateField.setText("");
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
	
	private void mouseMovedSelection(MouseEvent e) {
		// XXX
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
			if (f != null) editForm(f);
			
		}
		else if (e.isControlDown()) {
			setState(DrawingState.SELECTION);
			selectionList = new ArrayList<>();
			selectionList.add(f);
			f.setState(Etats.SELECTED);
		}
		repaint();
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
			deleteForm(f);
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
					
					NodeUI n1 = (NodeUI)selectedForm;
					NodeUI n2 = (NodeUI)f;
					GraphState gs = new GraphState(EnumAction.CREATE_LINK);
					gs.setProperty("num_origin", n1.getNode().getNumber());
					gs.setProperty("num_end", n2.getNode().getNumber());
					gs.setProperty("props", graph.getLink(n1, n2).getLink().getProperties());
					saveUndoAction(gs);
					
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
			NodeUI node = new NodeUI(e.getPoint());
			graph.addNode(node);
			GraphState gs = new GraphState(EnumAction.CREATE_NODE);
			gs.setProperty("num", node.getNode().getNumber());
			gs.setProperty("props", node.getNode().getProperties());
			gs.setProperty("centre", e.getPoint());
			saveUndoAction(gs);
		}
	}
	
	private void mouseClickedSolver(MouseEvent e) {
		final FormUI f = graph.getPointedForm(e.getPoint());
		if (e.getButton() == MouseEvent.BUTTON3) {
			contextualMenu(e.getPoint(), f);
			return;
		}
	}
	
	private void mouseClickedSelection(MouseEvent e) {
		final FormUI f = graph.getPointedForm(e.getPoint());
		if (e.getButton() == MouseEvent.BUTTON3) {
			contextualMenu(e.getPoint(), f);
			return;
		}
		if (f == null) {
			groupSelection = false;
			if (selectionList != null) for (FormUI form : selectionList) form.setState(Etats.NORMAL);
			setState(DrawingState.NORMAL);
		}
		else {
			if (e.isControlDown()) { // Si on a cliqué en maintenant la touche ctrl appuyée
				if (selectionList.contains(f)){
					selectionList.remove(f);
					f.setState(Etats.NORMAL);
				}
				else {
					selectionList.add(f);
					f.setState(Etats.SELECTED);
				}
			}
			else {
				if (selectionList != null) for (FormUI form : selectionList) form.setState(Etats.SELECTED);
				f.setState(Etats.SELECTED);
				selectionList.clear();
				selectionList.add(f);
			}
		}
		repaint();
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
					deleteForm(f);
				}
			});
			editItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
//					f.setState(Etats.EDITING);
					editForm(f);
				}
			});
		} else {		// Menu contextuel du panneau
			if (previousForm != null) {
				previousForm.setState(Etats.NORMAL);
				previousForm = null;
			}
			JPopupMenu menu = new JPopupMenu();
			JMenuItem openItem = new JMenuItem("Open map...");
			JMenuItem saveItem = new JMenuItem("Save graph");
			menu.add(openItem);
			menu.add(saveItem);
			menu.show(this, p.x, p.y);
			menu.setCursor(NORMAL_CURSOR);
			openItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser filechooser = new JFileChooser();
					filechooser.setFileFilter(new FileFilter() {
						public String getDescription() {
							return "Fichiers image";
						}
						public boolean accept(File f) {
							for (String str : ImageIO.getReaderFileSuffixes()) if (f.getName().endsWith("." + str)) return true;
							return f.isDirectory();
						}
					});
					if (filechooser.showOpenDialog(PanneauUI.this) == JFileChooser.APPROVE_OPTION) {
						File f = filechooser.getSelectedFile();
						try {
							changeBackgroundImage(ImageIO.read(f));
							repaint();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			});
			saveItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					saveGraph();
				}
			});
		}
	}
	
	private void changeBackgroundImage(Image image) {
		fond = image;
		int old_width = getPreferredSize().width;
		int old_height = getPreferredSize().height;
		int new_width = fond.getWidth(PanneauUI.this);
		int new_height = fond.getHeight(PanneauUI.this); 
		setSize(new_width, new_height);
		setPreferredSize(new Dimension(Math.max(old_width, new_width), Math.max(old_height, new_height)));
		gf.changeSize();
	}
	
	@SuppressWarnings("unchecked")
	private void editForm(FormUI f) {
		HashMap<String, Object> old_props = null;
		if (f instanceof NodeUI) old_props = (HashMap<String, Object>) ((NodeUI)f).getNode().getProperties().clone();
		if (f instanceof LinkUI) old_props = (HashMap<String, Object>) ((LinkUI)f).getLink().getProperties().clone();
		if (!f.editForm()) return;
		GraphState gs = null;
		if (f instanceof NodeUI) {
			gs = new GraphState(EnumAction.EDIT_NODE);
			gs.setProperty("num", ((NodeUI)f).getNode().getNumber());
			gs.setProperty("new_props", (HashMap<String, Object>) ((NodeUI)f).getNode().getProperties().clone());
		}
		else if (f instanceof LinkUI) {
			gs = new GraphState(EnumAction.EDIT_LINK);
			gs.setProperty("num_origin", ((LinkUI)f).getOriginNode().getNode().getNumber());
			gs.setProperty("num_end", ((LinkUI)f).getEndNode().getNode().getNumber());
			gs.setProperty("new_props", (HashMap<String, Object>) ((LinkUI)f).getLink().getProperties().clone());
		}
		gs.setProperty("old_props", old_props);
		saveUndoAction(gs);
	}
	
	@SuppressWarnings("unchecked")
	private void deleteForm(FormUI f) {
		GraphState gs = null;
		if (f instanceof NodeUI) {
			gs = new GraphState(EnumAction.DELETE_NODE);
			gs.setProperty("num", ((NodeUI)f).getNode().getNumber());
			gs.setProperty("centre", ((NodeUI)f).getCenter().clone());
			gs.setProperty("props", ((NodeUI)f).getNode().getProperties().clone());
			// Préparation du map des liens
			HashMap<String, HashMap<String, Object>> links = new HashMap<>();
			for (LinkUI lnk : graph.getRelatedLinks((NodeUI)f)) {
				links.put(lnk.getKey(), (HashMap<String, Object>) lnk.getLink().getProperties().clone());
			}
			gs.setProperty("liens", links);
		}
		else if (f instanceof LinkUI) {
			gs = new GraphState(EnumAction.DELETE_LINK);
			gs.setProperty("num_origin", ((LinkUI)f).getOriginNode().getNode().getNumber());
			gs.setProperty("num_end", ((LinkUI)f).getEndNode().getNode().getNumber());
			gs.setProperty("props", ((LinkUI)f).getLink().getProperties());
		}
		graph.removeForm(f);
		saveUndoAction(gs);
	}
	
	public boolean isUndoable() {
		return !undoList.isEmpty();
	}
	
	public boolean isRedoable() {
		return !redoList.isEmpty();
	}
	
	public void ctrlZ() {
		GraphState gs = undoList.pollLast();
		if (gs == null) return;
		gs.ctrlZ(graph);
		redoList.addLast(gs);
		annulerButton.setEnabled(isUndoable());
		repeterButton.setEnabled(isRedoable());
		repaint();
	}
	
	public void ctrlY() {
		GraphState gs = redoList.pollLast();
		if (gs == null) return;
		gs.ctrlY(graph);
		undoList.addLast(gs);
		annulerButton.setEnabled(isUndoable());
		repeterButton.setEnabled(isRedoable());
		repaint();
	}
	
	private void saveUndoAction(GraphState gs) {
		if (undoList.size() >= UNDO_LIST_MAX_SIZE) undoList.pollFirst();
		undoList.addLast(gs);
		redoList.clear();
	}
	
	public void clearWorkspace() {
		undoList.clear();
		redoList.clear();
		graph.clear();
		repaint();
	}
	
}
